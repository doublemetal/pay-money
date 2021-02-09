package com.kim.api.scatter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 뿌리기 분배건 받기 맵핑 테이블
 */
@IdClass(ScatterDetailReceiveId.class)
@Table(name = "scatter_detail_receive")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScatterDetailReceive {
    @Id
    @Column(name = "detail_sequence")
    private long detailSequence;
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private String userId;

    public ScatterDetailReceive(long detailSequence, String token, String userId) {
        this.detailSequence = detailSequence;
        this.token = token;
        this.userId = userId;
    }
}
