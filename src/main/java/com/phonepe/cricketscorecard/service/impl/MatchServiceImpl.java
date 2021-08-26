package com.phonepe.cricketscorecard.service.impl;

import com.phonepe.cricketscorecard.dto.MatchInfo;
import com.phonepe.cricketscorecard.entity.Match;
import com.phonepe.cricketscorecard.enums.ErrorCode;
import com.phonepe.cricketscorecard.enums.InningType;
import com.phonepe.cricketscorecard.exception.CricketScorecardException;
import com.phonepe.cricketscorecard.repository.MatchRepository;
import com.phonepe.cricketscorecard.service.MatchService;
import com.phonepe.cricketscorecard.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class MatchServiceImpl implements MatchService {

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    TeamService teamService;

    @Override
    @Transactional
    public Match createMatch(MatchInfo matchInfo) {
        Match match = Match.builder()
                .teamA(matchInfo.getTeamA())
                .teamB(matchInfo.getTeamB())
                .overs(matchInfo.getOver())
                .noOfTeamMembers(matchInfo.getTeamMembers())
                .createdTime(Instant.now())
                .venue(matchInfo.getVenue())
                .inningType(InningType.INNING_1)
                .build();

        teamService.createTeam(matchInfo.getTeamA());
        teamService.createTeam(matchInfo.getTeamB());

        return saveDataToRepository(match);
    }

    @Override
    public Match getMatchs(int matchId) {
        Optional<Match> optionalMatch = matchRepository.findById(matchId);
        if(!optionalMatch.isPresent()) {
            throw new CricketScorecardException(ErrorCode.MATCH_NOT_FOUND);
        }
        return optionalMatch.get();
    }

    public Match saveDataToRepository(Match Match) {
        try {
            return matchRepository.save(Match);
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
