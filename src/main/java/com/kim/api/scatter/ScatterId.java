package com.kim.api.scatter;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Scatter 복합키 선언
 */
@Getter
@Setter
public class ScatterId implements Serializable {
    private String token;
    private String userId;
    private String roomId;
}
