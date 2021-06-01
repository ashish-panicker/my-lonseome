package com.easyskillup.ipldashboard.batch.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import javax.persistence.EntityManager;

import com.easyskillup.ipldashboard.model.Team;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final EntityManager em;

    @Autowired
    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teams = new HashMap<>();

            log.info("EM QUERY 1");

            em.createQuery("select distinct m.team1, count(*) from Match m group by m.team1", Object[].class)
                    .getResultList().stream().map(e -> new Team((String) e[0], (long) e[1]))
                    .forEach(team -> teams.put(team.getName(), team));

            log.info("EM QUERY 2");
            em.createQuery("select distinct m.team2, count(*) from Match m group by m.team2", Object[].class)
                    .getResultList().stream().forEach(e -> {
                        Team team = teams.get((String) e[0]);
                        team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
                    });
            log.info("EM QUERY 3");
            em.createQuery("select distinct m.winner, count(*) from Match m where m.winner != 'NA' group by m.winner", Object[].class)
                    .getResultList().stream().forEach(e -> {
                        log.info("VALUES: [" + e[0] + " " + e[1] + "]");
                        Team team = teams.get((String) e[0]);
                        team.setTotalWins((long) e[1]);
                    });

            Predicate<Team> isGujaratLions = (t -> t.getName().toLowerCase().contains("gujarat"));
            Predicate<Team> isCochinTuskers = (t -> t.getName().toLowerCase().contains("kerala"));

            teams.values().stream().filter(isCochinTuskers.or(isGujaratLions))
                    .forEach(t -> t.setDefunct(true));
            teams.entrySet().stream().forEach(t -> em.persist(t.getValue()));

            // teams.keySet().stream().filter(isCochinTuskers.or(isGujaratLions))
            // .forEach(t -> log.info("After filter: "+t.toString()));

        }

    }

}
