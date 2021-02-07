package com.kim.api.scatter;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 뿌리기 모델
 */
@Getter
@Setter
@IdClass(ScatterId.class)
@Table(name = "scatter")
@Entity
public class Scatter {
    @Id
    @Column(name = "token")
    private String token;
    @Id
    @Column(name = "userId")
    private String userId;
    @Id
    @Column(name = "roomId")
    private String roomId;

    private Date regDate;
    private Date modDate;
}
