package com.kim.api.scatter.repository;

import com.kim.api.scatter.model.ScatterDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScatterDetailRepository extends JpaRepository<ScatterDetail, String> {
    ScatterDetail findTopByTokenAndReceiveYnNot(String token, String receiveYn);
}
