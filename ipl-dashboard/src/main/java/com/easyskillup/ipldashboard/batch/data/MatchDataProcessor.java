package com.easyskillup.ipldashboard.batch.data;

import java.time.LocalDate;

import com.easyskillup.ipldashboard.model.Match;
import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

// @Slf4j
public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    @Override
    public Match process(MatchInput matchinput) throws Exception {

        Match match = new Match();

        match.setId(Long.parseLong(matchinput.getId()));
        match.setCity(matchinput.getCity());
        match.setMatchDate(LocalDate.parse(matchinput.getDate()));
        match.setPlayerOfMatch(matchinput.getPlayer_of_match());
        match.setVenue(matchinput.getVenue());
        match.setNuetralVenue(Integer.parseInt(matchinput.getNeutral_venue()) == 1 ? true : false);

        /**
         * set team 1 as first innigs team and team 2 as second innigs teams
         */
        String firstInnigsTeam, secondInningsTeam;

        if ("bat".equals(matchinput.getToss_decision())) {
            firstInnigsTeam = matchinput.getToss_winner();
            secondInningsTeam = matchinput.getToss_winner().equals(matchinput.getTeam1()) ? matchinput.getTeam2()
                    : matchinput.getTeam1();
        } else {
            secondInningsTeam = matchinput.getToss_winner();
            firstInnigsTeam = matchinput.getToss_winner().equals(matchinput.getTeam1()) ? matchinput.getTeam2()
                    : matchinput.getTeam1();
        }

        match.setTeam1(firstInnigsTeam);
        match.setTeam2(secondInningsTeam);

        match.setTossWinner(matchinput.getToss_winner());
        match.setTossDecision(matchinput.getToss_decision());
        match.setWinner(matchinput.getWinner());
        match.setResult(matchinput.getResult());
        match.setResultMargin(matchinput.getResult_margin());
        match.setEliminator(matchinput.getEliminator());
        match.setMethod(matchinput.getMethod());
        match.setUmpire1(matchinput.getUmpire1());
        match.setUmpire2(matchinput.getUmpire2());

        // log.info(String.valueOf(match));

        return match;
    }

}
