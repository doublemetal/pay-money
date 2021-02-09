package com.kim.api.scatter;

import com.kim.api.scatter.model.*;
import com.kim.api.scatter.repository.ScatterDetailReceiveRepository;
import com.kim.api.scatter.repository.ScatterDetailRepository;
import com.kim.api.scatter.repository.ScatterRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
     * @return 뿌리기 현재 상태
     */
    public ScatterDto getCurrentScatter(String token) {
        Scatter scatter = getScatter(token);
        if (scatter == null) {
            throw new RuntimeException("Not exists");
        }

        ScatterDto scatterDto = new ScatterDto();
        scatterDto.setUserId(scatter.getUserId());
        scatterDto.setRoomId(scatter.getRoomId());
        scatterDto.setRegDate(scatter.getRegDate());
        scatterDto.setDate(scatter.getRegDate().format(DateTimeFormatter.ISO_DATE_TIME));
        scatterDto.setAmount(scatter.getAmount());

        List<ScatterDto.Receive> receives = getReceives(scatter.getScatterDetail());
        scatterDto.setReceives(receives);
        scatterDto.setReceivedAmount(getReceiveAmount(receives));

        return scatterDto;
    }

    private List<ScatterDto.Receive> getReceives(List<ScatterDetail> scatterDetail) {
        if (CollectionUtils.isEmpty(scatterDetail)) {
            return Collections.emptyList();
        }

        return scatterDetail.stream()
                .filter(detail -> StringUtils.equals(detail.getReceiveYn(), "Y"))
                .peek(detail -> detail.setScatterDetailReceive(scatterDetailReceiveRepository.findByDetailSequence(detail.getSequence())))
                .map(ScatterDetail::convert).collect(Collectors.toList());
    }

    private int getReceiveAmount(List<ScatterDto.Receive> receives) {
        return receives.stream().mapToInt(ScatterDto.Receive::getAmount).sum();
    }

    /**
     * 뿌리기 생성
     *
     * @return Token
     */
    @Transactional
    public Scatter createScatter(String userId, String roomId, ScatterApiRequest scatterApiRequest) {
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
