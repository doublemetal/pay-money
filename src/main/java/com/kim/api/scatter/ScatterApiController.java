package com.kim.api.scatter;

import com.kim.api.core.CommonResponse;
import com.kim.api.scatter.model.Scatter;
import com.kim.api.scatter.model.ScatterApiRequest;
import com.kim.api.scatter.model.ScatterDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 뿌리기 API
 */
@Slf4j
@RequestMapping("/api/scatter")
@RestController
public class ScatterApiController {
    private final ScatterApiBO scatterApiBO;

    public ScatterApiController(ScatterApiBO scatterApiBO) {
        this.scatterApiBO = scatterApiBO;
    }

    /**
     * 뿌리기 API
     * 잔액은 항상 충분히 있는 것으로 가정
     *
     * @return Token
     */
    @PostMapping
    public CommonResponse scatter(@RequestHeader("X-USER-ID") String userId,
                                  @RequestHeader("X-ROOM-ID") String roomId,
                                  @Valid @RequestBody ScatterApiRequest scatterApiRequest) {
        if (scatterApiRequest.getAmount() < scatterApiRequest.getCount()) {
            throw new RuntimeException("Not enough amount");
        }

        Scatter scatter = scatterApiBO.createScatter(userId, roomId, scatterApiRequest);
        return new CommonResponse<>("success", "Request is succeed.", scatter.getToken());
    }

    /**
     * 받기 API
     *
     * @return 받은 금액
     */
    @PostMapping("/{token}")
    public CommonResponse receive(@RequestHeader("X-USER-ID") String userId,
                                  @RequestHeader("X-ROOM-ID") String roomId,
                                  @PathVariable String token) {
        if (token.length() != Scatter.TOKEN_LENGTH) {
            throw new RuntimeException("Invalid token");
        }

        Scatter scatter = scatterApiBO.getScatter(token);

        // TODO Validation 집합으로 분리
        if (!scatter.getRoomId().equals(roomId)) {
            throw new RuntimeException("Invalid room");
        } else if (scatter.getUserId().equals(userId)) {
            throw new RuntimeException("You can't get the amount you started.");
        } else if (scatterApiBO.hasReceive(token, userId)) {
            throw new RuntimeException("You already got it");
        } else if (isTimeout(scatter.getRegDate())) {
            throw new RuntimeException("10 minutes exceeded and cannot proceed");
        }

        ScatterDetail receive = scatterApiBO.receive(token, userId);
        return new CommonResponse<>("success", "Request is succeed.", receive.getAmount());
    }

    private boolean isTimeout(LocalDateTime regDate) {
        return regDate.plusMinutes(10).isAfter(LocalDateTime.now());
    }

    @GetMapping("/{token}")
    public CommonResponse getScatter(HttpServletRequest request, @PathVariable String token) {
        // Check user, room

        // 토큰 체크
        // 본인이 뿌린건인지 체크
        // 7일 이내 체크

        // 뿌리기 현재 상태 리턴
        // 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보(받은 금액, 받은 사용자 아이디 리스트)
        // 뿌리기 테이블, 상세 테이블 필요
        return null;
    }
}
