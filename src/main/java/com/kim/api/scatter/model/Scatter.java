package com.kim.api.scatter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 뿌리기 모델
 */
@Table(name = "scatter")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Scatter {
    public static final int TOKEN_LENGTH = 3;

    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private String userId;
    @Column(name = "room_id")
    private String roomId;

    @CreationTimestamp
    @Column(name = "reg_date")
    private LocalDateTime regDate;
    @UpdateTimestamp
    @Column(name = "mod_date")
    private LocalDateTime modDate;

    @Column
    private int amount;
    @Column
    private int count;

    public Scatter(String token) {
        this.token = token;
    }

    @OneToMany
    @JoinColumn(name = "token")
    private List<ScatterDetail> scatterDetail;
}
