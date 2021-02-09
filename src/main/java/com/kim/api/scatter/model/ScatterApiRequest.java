package com.kim.api.scatter.model;

import lombok.Getter;

import javax.validation.constraints.Min;

/**
 * 뿌리기 API 요청 모델
 */
@Getter
public class ScatterApiRequest {
    /**
     * 뿌릴 금액, 정수로 가정함
     */
    @Min(1)
    private int amount;

    /**
     * 뿌릴 인원
     */
    @Min(1)
    private int count;
}
