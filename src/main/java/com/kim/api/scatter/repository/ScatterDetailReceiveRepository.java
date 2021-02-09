package com.kim.api.scatter.repository;

import com.kim.api.scatter.model.ScatterDetailReceive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScatterDetailReceiveRepository extends JpaRepository<ScatterDetailReceive, String> {
    int countByTokenAndUserId(String token, String userId);

    ScatterDetailReceive findByDetailSequence(long detailSequence);
}
