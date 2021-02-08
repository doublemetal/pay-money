package com.kim.api.scatter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScatterRepository extends JpaRepository<Scatter, String> {
}
