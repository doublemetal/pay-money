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

    private final ScatterRepository scatterApiRepository;
    private final ScatterDetailRepository scatterDetailRepository;

    public ScatterApiBO(ScatterRepository scatterApiRepository, ScatterDetailRepository scatterDetailRepository) {
        this.scatterApiRepository = scatterApiRepository;
        this.scatterDetailRepository = scatterDetailRepository;
    }

    /**
     * 뿌리기 생성
     *
     * @return Token
     */
    @Transactional
    public Scatter createScatter(String userId, String roomId, @Valid ScatterApiRequest scatterApiRequest) {
        String token = RandomStringUtils.randomAlphanumeric(TOKEN_LENGTH);
        Scatter scatter = new Scatter(token, userId, roomId);
        scatter.setAmount(scatterApiRequest.getAmount());
        scatter.setCount(scatterApiRequest.getCount());

        Scatter save = scatterApiRepository.save(scatter);
        createScatterDetail(scatter);

        return save;
    }

    /**
     * 뿌리기 상세 데이터 생성(금액 분배)
     */
    public void createScatterDetail(Scatter scatter) {
        int remainAmount = scatter.getAmount();
        for (int i = scatter.getCount() - 1; i >= 0; i--) {
            int amount = rand(1, remainAmount - i);
            remainAmount -= amount;
            scatterDetailRepository.save(new ScatterDetail(scatter, amount));
        }
    }

    private int rand(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
