package com.kim.api.scatter;

import com.kim.api.core.CommonResponse;
import com.kim.api.utils.StringUtils;
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
    public CommonResponse scatter(@RequestAttribute("X-USER-ID") String userId,
                                  @RequestAttribute("X-ROOM-ID") String roomId,
                                  @Valid @RequestBody ScatterApiRequest scatterApiRequest) {
        // TODO 가능하면 validator 로 분리
        // userId 체크, 검증불가 존재만 체크
        // roomId 체크, 검증불가 존재만 체크
        // 뿌릴 금액 체크 > 사람당 최소 1원, 4명이면 최소 4원
        // 뿌릴 인원 체크 > 0
        // token 을 반환, 3자리 문자열, 3자리 UUID??
        // 3자리이기 때문에 userId + roomId + token 으로 키를 잡음
        // 요구사항에서도 token 만 언급되고, 그 외의 값들로 컨트롤되면 복잡해질 거 같다
        // 토큰은 10분이면 만료되기 때문에 대화방 내에서 짧은 시간 내에 조합수만큼의 토큰 생성 가능 (활성화된 토큰을 검증) <- 멀티스레드 대응 필요
        // 위에서는 복합키로 언급했지만, 토큰 중복 가능성 때문에
        // 그냥 시퀀스를 키로 하고 대화방 내에서는 단시간 내에는 999개까지만 생성가능하도록 설정
        // 멀티스레드 대응을 위해 복합키로 다시 변경 (이렇게 하면 토큰 중복 검사만 하면 됨)

        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("UserId is empty");
        }

        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("roomId is empty");
        }

        if (scatterApiRequest.getAmount() <= 0 || scatterApiRequest.getCount() <= 0) {
            throw new RuntimeException("Request is invalid");
        }

        if (scatterApiRequest.getAmount() / scatterApiRequest.getCount() > 0) {
            throw new RuntimeException("Not enough amount");
        }

        // 메인 데이터 생성
        // 분배건 데이터 생성
        Scatter scatter = scatterApiBO.createScatter(userId, roomId);
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
