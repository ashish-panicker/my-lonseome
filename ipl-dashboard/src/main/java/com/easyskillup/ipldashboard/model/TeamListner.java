package com.easyskillup.ipldashboard.model;

import javax.persistence.PostPersist;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TeamListner {

    @PostPersist
    public void postPersistTeam(Team team){
        log.info("Team persisted: " + team);
    }
    
}
