package com.kim.api.scatter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Transactional(readOnly = true)
@Slf4j
@Service
public class ScatterApiBO {
    private static final int TOKEN_LENGTH = 3;

    private final ScatterApiRepository scatterApiRepository;

    public ScatterApiBO(ScatterApiRepository scatterApiRepository) {
        this.scatterApiRepository = scatterApiRepository;
    }

    /**
     * 뿌리기 메인 데이터 생성
     *
     * @return Token
     */
    @Transactional
    public Scatter createScatter(String userId, String roomId, @Valid ScatterApiRequest scatterApiRequest) {
        String token = RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);
        Scatter scatter = new Scatter(token, userId, roomId);
        scatter.setAmount(scatterApiRequest.getAmount());
        scatter.setCount(scatterApiRequest.getCount());
        return scatterApiRepository.save(scatter);
    }

    /**
     * 뿌리기 상세 데이터 생성(금액 분배)
     */
    public void createScatterDetail(Scatter scatter) {
        // Random amount
    }
}
