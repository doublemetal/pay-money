package com.kim.api.scatter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * 뿌리기 API 요청 모델
 */
@Getter
@Setter
@NoArgsConstructor
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

    public ScatterApiRequest(@Min(1) int amount, @Min(1) int count) {
        this.amount = amount;
        this.count = count;
    }
}
