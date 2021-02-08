package com.kim.api.scatter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 뿌리기 모델
 */
@IdClass(ScatterId.class)
@Table(name = "scatter")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Scatter {
    @Id
    @Column(name = "token")
    private String token;
    @Id
    @Column(name = "user_id")
    private String userId;
    @Id
    @Column(name = "room_id")
    private String roomId;

    @CreationTimestamp
    @Column(name = "reg_date")
    private Date regDate;
    @UpdateTimestamp
    @Column(name = "mod_date")
    private Date modDate;

    @Column
    private int amount;
    @Column
    private int count;

    public Scatter(String token, String userId, String roomId) {
        this.token = token;
        this.userId = userId;
        this.roomId = roomId;
    }
}
