package com.kim.api.scatter.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * ScatterDetailReceive 복합키 선언
 */
@Getter
@Setter
public class ScatterDetailReceiveId implements Serializable {
    private long detailSequence;
    private String token;
}
