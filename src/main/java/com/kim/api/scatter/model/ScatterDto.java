package com.kim.api.scatter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 뿌리기 조회 모델
 */
@Getter
@Setter
public class ScatterDto {
    @JsonIgnore
    private String userId;
    @JsonIgnore
    private String roomId;

    private LocalDateTime regDate; // 뿌린 시각
    private int amount; // 뿌린 금액
    private int receivedAmount; // 받기 완료된 금액
    private List<Receive> receives;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Receive {
        private int amount;
        private String userId;

        public Receive(int amount, String userId) {
            this.amount = amount;
            this.userId = userId;
        }
    }
}
