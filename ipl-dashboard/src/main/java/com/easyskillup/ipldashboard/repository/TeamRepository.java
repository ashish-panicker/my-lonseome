package com.easyskillup.ipldashboard.repository;

import com.easyskillup.ipldashboard.model.Team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>{

    Team findByName(String name);
    
}
