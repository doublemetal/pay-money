package com.kim.api.scatter.repository;

import com.kim.api.scatter.model.Scatter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScatterRepository extends JpaRepository<Scatter, String> {
    Scatter findByToken(String token);
}
