package com.ghostappi.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ghostappi.backend.model.Reward;

public interface RewardRepository extends JpaRepository <Reward, Integer> {

}