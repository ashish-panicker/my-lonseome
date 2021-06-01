package com.easyskillup.ipldashboard.repository;

import java.util.List;

import com.easyskillup.ipldashboard.model.Match;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>{

    List<Match> findByTeam1OrTeam2OrderByMatchDateDesc(String team1, String team2, Pageable pageable);

    default List<Match> findLatestMatchesByTeam(String team, int page, int size){
        return findByTeam1OrTeam2OrderByMatchDateDesc(team, team, PageRequest.of(page, size));
    }
    
}
