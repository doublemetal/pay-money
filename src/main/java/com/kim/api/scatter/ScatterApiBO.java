package com.kim.api.scatter;

import com.kim.api.scatter.model.Scatter;
import com.kim.api.scatter.model.ScatterApiRequest;
import com.kim.api.scatter.model.ScatterDetail;
import com.kim.api.scatter.model.ScatterDetailReceive;
import com.kim.api.scatter.repository.ScatterDetailReceiveRepository;
import com.kim.api.scatter.repository.ScatterDetailRepository;
import com.kim.api.scatter.repository.ScatterRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Transactional(readOnly = true)
@Slf4j
@Service
public class ScatterApiBO {
    private final ScatterRepository scatterRepository;
    private final ScatterDetailRepository scatterDetailRepository;
    private final ScatterDetailReceiveRepository scatterDetailReceiveRepository;

    public ScatterApiBO(ScatterRepository scatterRepository, ScatterDetailRepository scatterDetailRepository, ScatterDetailReceiveRepository scatterDetailReceiveRepository) {
        this.scatterRepository = scatterRepository;
        this.scatterDetailRepository = scatterDetailRepository;
        this.scatterDetailReceiveRepository = scatterDetailReceiveRepository;
    }

    /**
     * @return 뿌리기 데이터
     */
    public Scatter getScatter(String token) {
        return scatterRepository.findByToken(token);
    }

    /**
     * 뿌리기 생성
     *
     * @return Token
     */
    @Transactional
    public Scatter createScatter(String userId, String roomId, @Valid ScatterApiRequest scatterApiRequest) {
        String token = RandomStringUtils.randomAlphanumeric(Scatter.TOKEN_LENGTH);
        Scatter scatter = new Scatter(token);
        scatter.setUserId(userId);
        scatter.setRoomId(roomId);
        scatter.setAmount(scatterApiRequest.getAmount());
        scatter.setCount(scatterApiRequest.getCount());

        Scatter save = scatterRepository.save(scatter);
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
            scatterDetailRepository.save(new ScatterDetail(scatter.getToken(), amount + (i == 0 ? remainAmount : 0)));
        }
    }

    private int rand(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * 뿌린 금액 받기
     *
     * @param userId 이미 받은 userId 일 경우 에러
     */
    @Transactional
    public ScatterDetail receive(String token, String userId) {
        ScatterDetail scatterDetail = scatterDetailRepository.findTopByTokenAndReceiveYnNot(token, "Y");

        scatterDetailReceiveRepository.save(new ScatterDetailReceive(scatterDetail.getSequence(), scatterDetail.getToken(), userId));
        scatterDetail.setReceiveYn("Y");
        return scatterDetail;
    }

    public boolean hasReceive(String token, String userId) {
        return scatterDetailReceiveRepository.countByTokenAndUserId(token, userId) > 0;
    }
}
