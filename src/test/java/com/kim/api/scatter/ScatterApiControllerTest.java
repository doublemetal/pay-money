package com.kim.api.scatter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

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

    private ResultActions callPost(String url, String userId, String roomId, Object request) throws Exception {
        return mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.toString())
                .header("X-USER-ID", userId)
                .header("X-ROOM-ID", roomId)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Test
    void 뿌리기성공() throws Exception {
        callPost("/api/scatter", "testUser", "9999", new ScatterApiRequest(1000, 4))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.info").exists());
    }
}
