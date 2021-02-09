package com.kim.api.scatter.model;

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
    @Column(name = "sequence")
    private long sequence;

    @Column(name = "token")
    private String token;
    @Column
    private int amount;
    @Column(name = "receive_yn")
    private String receiveYn;

    public ScatterDetail(String token, int amount) {
        this.token = token;
        this.amount = amount;
    }
}
