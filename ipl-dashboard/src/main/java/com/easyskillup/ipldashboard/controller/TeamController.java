package com.easyskillup.ipldashboard.controller;

import com.easyskillup.ipldashboard.model.Team;
import com.easyskillup.ipldashboard.repository.MatchRepository;
import com.easyskillup.ipldashboard.repository.TeamRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/teams/{name}")
    public Team getOneTeam(@PathVariable String name) {

        Team team =  teamRepository.findByName(name);
        team.setMatches(matchRepository.findLatestMatchesByTeam(name, 0, 5));
        return team;
    }

}
