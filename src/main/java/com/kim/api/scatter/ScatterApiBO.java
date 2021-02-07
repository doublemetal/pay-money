package com.kim.api.scatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScatterApiBO {
    private final ScatterApiRepository scatterApiRepository;

    public ScatterApiBO(ScatterApiRepository scatterApiRepository) {
        this.scatterApiRepository = scatterApiRepository;
    }

    /**
     * 뿌리기 메인 데이터 생성
     *
     * @return Token
     */
    public Scatter createScatter(String userId, String roomId) {
        // Random token
        // None PK AI column is needed

        return null;
    }

    /**
     * 뿌리기 상세 데이터 생성(금액 분배)
     */
    public void createScatterDetail(Scatter scatter) {
        // Random amount
    }
}
