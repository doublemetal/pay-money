package com.kim.api.scatter;

import lombok.Getter;

/**
 * 뿌리기 API 요청 모델
 */
@Getter
public class ScatterApiRequest {
    /**
     * 뿌릴 금액, 정수로 가정함
     */
    private int amount;

    /**
     * 뿌릴 인원
     */
    private int count;
}
