package com.kim.api.scatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kim.api.core.CommonResponse;
import com.kim.api.scatter.model.ScatterApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
class ScatterApiControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String SCATTER = "scatter";
    private static final String RECEIVER = "receiver";
    private static final String ROOM_ID = "9999";

    private ResultActions callPost(String url, String userId, String roomId, Object request) throws Exception {
        return mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.toString())
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(objectMapper.writeValueAsString(request)));
    }

    private ResultActions callGet(String url, String userId, String roomId) throws Exception {
        return mvc.perform(get(url)
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId));
    }

    private String scatter(int amount, int count) throws Exception {
        MvcResult result = callPost("/api/scatter", SCATTER, ROOM_ID, new ScatterApiRequest(amount, count))
                .andReturn();
        CommonResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), CommonResponse.class);
        return (String) response.getInfo();
    }

    @Test
    void 뿌리기성공() throws Exception {
        callPost("/api/scatter", SCATTER, ROOM_ID, new ScatterApiRequest(1000, 4))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.info").exists());
    }

    @Test
    void 뿌리기실패_값이0인경우() throws Exception {
        callPost("/api/scatter", SCATTER, ROOM_ID, new ScatterApiRequest(0, 0))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("fail"));
    }

    @Test
    void 뿌리기실패_받을수있는최소금액이1이안되는경우() throws Exception {
        callPost("/api/scatter", SCATTER, ROOM_ID, new ScatterApiRequest(3, 4))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Not enough amount"));
    }

    @Test
    void 받기성공() throws Exception {
        callPost("/api/scatter/" + scatter(1000, 4), RECEIVER, ROOM_ID, null)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Request is succeed."))
                .andExpect(jsonPath("$.info").exists());
    }

    @Test
    void 받기실패_다른대화방에서받기() throws Exception {
        callPost("/api/scatter/" + scatter(1000, 4), RECEIVER, "1111", null)
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid room"));
    }

    @Test
    void 받기실패_뿌린사람이받기() throws Exception {
        callPost("/api/scatter/" + scatter(1000, 4), SCATTER, ROOM_ID, null)
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("You can't get the amount you started."));
    }

    @Test
    void 받기실패_중복받기() throws Exception {
        String token = scatter(1000, 1);
        callPost("/api/scatter/" + token, RECEIVER, ROOM_ID, null);
        callPost("/api/scatter/" + token, RECEIVER, ROOM_ID, null)
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("You already got it"));
    }

    @Test
    void 조회성공() throws Exception {
        String token = scatter(1000, 1);
        callPost("/api/scatter/" + token, RECEIVER, ROOM_ID, null);

        callGet("/api/scatter/" + token, SCATTER, ROOM_ID)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Request is succeed."))
                .andExpect(jsonPath("$.info").exists())
                .andExpect(jsonPath("$.info.date").exists())
                .andExpect(jsonPath("$.info.amount").value(1000))
                .andExpect(jsonPath("$.info.receivedAmount").value(1000))
                .andExpect(jsonPath("$.info.receives").exists())
                .andExpect(jsonPath("$.info.receives[0].amount").value(1000))
                .andExpect(jsonPath("$.info.receives[0].userId").value(RECEIVER))
                .andReturn();
    }

    @Test
    void 조회성공_받기전조회() throws Exception {
        String token = scatter(1000, 1);
        callGet("/api/scatter/" + token, SCATTER, ROOM_ID)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Request is succeed."))
                .andExpect(jsonPath("$.info").exists())
                .andExpect(jsonPath("$.info.date").exists())
                .andExpect(jsonPath("$.info.amount").value(1000))
                .andExpect(jsonPath("$.info.receivedAmount").value(0));
    }

    @Test
    void 조회실패_토큰오류() throws Exception {
        callGet("/api/scatter/" + 123, SCATTER, ROOM_ID)
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Not exists"));
    }

    @Test
    void 조회실패_내것이아닌것을조회() throws Exception {
        String token = scatter(1000, 1);
        callGet("/api/scatter/" + token, "who?", ROOM_ID)
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Invalid user"));
    }
}
