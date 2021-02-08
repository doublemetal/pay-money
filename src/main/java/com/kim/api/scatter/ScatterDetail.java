package com.kim.api.scatter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 뿌리기 상세 모델
 */
@Table(name = "scatter_detail")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScatterDetail {
    @Id
    @GeneratedValue
    private long sequence;

    @Column(name = "token")
    private String token;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "room_id")
    private String roomId;

    @Column
    private int amount;

    public ScatterDetail(Scatter scatter, int amount) {
        this.token = scatter.getToken();
        this.userId = scatter.getUserId();
        this.roomId = scatter.getRoomId();
        this.amount = amount;
    }
}
