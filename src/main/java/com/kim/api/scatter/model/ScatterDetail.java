package com.kim.api.scatter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;

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
    @Column(name = "amount")
    private int amount;
    @Column(name = "receive_yn")
    private String receiveYn = "N";

    @Transient
    private ScatterDetailReceive scatterDetailReceive;

    public ScatterDetail(String token, int amount) {
        this.token = token;
        this.amount = amount;
    }

    public static ScatterDto.Receive convert(ScatterDetail scatterDetail) {
        return new ScatterDto.Receive(scatterDetail.getAmount(), Optional
                .ofNullable(scatterDetail.getScatterDetailReceive())
                .orElseGet(ScatterDetailReceive::new).getUserId());
    }
}
