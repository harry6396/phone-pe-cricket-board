package com.phonepe.cricketscorecard.service.impl;

import com.phonepe.cricketscorecard.entity.Team;
import com.phonepe.cricketscorecard.enums.ErrorCode;
import com.phonepe.cricketscorecard.exception.CricketScorecardException;
import com.phonepe.cricketscorecard.repository.TeamRepository;
import com.phonepe.cricketscorecard.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Override
    @Transactional
    public Team createTeam(String teamName) {
        Optional<Team> teamOptional = teamRepository.findByName(teamName);
        if(teamOptional.isPresent()) {
            return teamOptional.get();
        }
        Team team = Team.builder()
                .name(teamName)
                .build();
        return saveDataToRepository(team);
    }

    private Team saveDataToRepository(Team team) {
        try {
            return teamRepository.save(team);
        }
        catch(DataIntegrityViolationException e) {
            log.error("DataIntegrity error ", e);
            throw new CricketScorecardException(ErrorCode.MATCH_CREATE_FAILURE);
        }
        catch(PersistenceException e) {
            log.error("Failed to save data ", e);
            throw new CricketScorecardException(ErrorCode.MATCH_CREATE_FAILURE);
        }
    }
}
