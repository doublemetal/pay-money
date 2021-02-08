package com.kim.api.scatter;

import com.kim.api.core.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
     * @return 응답
     */
    @PostMapping
    public CommonResponse scatter(@RequestHeader("X-USER-ID") String userId,
                                  @RequestHeader("X-ROOM-ID") String roomId,
                                  @Valid @RequestBody ScatterApiRequest scatterApiRequest) {
        if (scatterApiRequest.getAmount() < scatterApiRequest.getCount()) {
            throw new RuntimeException("Not enough amount");
        }

        Scatter scatter = scatterApiBO.createScatter(userId, roomId, scatterApiRequest);
        scatterApiBO.createScatterDetail(scatter);
        return new CommonResponse<>("success", "Request is succeed.", scatter.getToken());
    }

    /**
     * 받기 API
     * TODO token
     *
     * @return
     */
    @PostMapping("/{token}")
    public CommonResponse receive(HttpServletRequest request, @PathVariable String token,
                                  @Valid @RequestBody ScatterApiRequest scatterApiRequest) {
        // Check user, room

        // 토큰 체크
        // 이미 받았는지 체크
        // 본인이 뿌린건인지 체크
        // 뿌린 room 인지 체크
        // 10분이 지났는지 체크

        // 랜덤 혹은 순서대로 분배건 할당 <- TODO 여기가 중요
        // TODO 남은 뿌리기건의 분배건과 유저를 맵핑하여 데이터 생성 (맵핑 테이블)

        // 금액을 리턴
        return null;
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
