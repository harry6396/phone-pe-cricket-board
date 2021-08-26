package com.phonepe.cricketscorecard.service.impl;

import com.phonepe.cricketscorecard.entity.Bowling;
import com.phonepe.cricketscorecard.entity.Match;
import com.phonepe.cricketscorecard.entity.Over;
import com.phonepe.cricketscorecard.enums.ErrorCode;
import com.phonepe.cricketscorecard.enums.InningType;
import com.phonepe.cricketscorecard.enums.OverStatus;
import com.phonepe.cricketscorecard.exception.CricketScorecardException;
import com.phonepe.cricketscorecard.repository.OverRepository;
import com.phonepe.cricketscorecard.service.OverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.PersistenceException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OverServiceImpl implements OverService {

    @Autowired
    OverRepository overRepository;
    @Autowired
    BowlServiceImpl bowlService;
    @Autowired
    ScorecardServiceImpl scorecardService;
    @Autowired
    MatchServiceImpl matchService;

    @Value("${cricket.maxBallAllowed}")
    private Integer maxBallAllowed;

    @Override
    public Over createOver(int matchId, int teamId, int bowlerId) {
        Optional<Over> optionalOver = overRepository.findFirstByMatchIdAndTeamIdAndStatusOrderByCreatedAtDesc(matchId, teamId, OverStatus.IN_PROGRESS);
        if(optionalOver.isPresent()) {
            return optionalOver.get();
        }
        optionalOver = overRepository.findFirstByMatchIdAndTeamIdAndStatusOrderByCreatedAtDesc(matchId, teamId, OverStatus.COMPLETED);
        if(optionalOver.isPresent()) {
            if(optionalOver.get().getBowlerId() == bowlerId) {
                throw new CricketScorecardException(ErrorCode.OVER_NOT_ALLOWED);
            }
        }
        Over over = Over.builder()
                .bowlerId(bowlerId)
                .overNumber(totalOverBowled(matchId, teamId)+1)
                .matchId(matchId)
                .teamId(teamId)
                .totalBowls(maxBallAllowed)
                .createdAt(Instant.now())
                .status(OverStatus.IN_PROGRESS)
                .build();
        return saveDataToRepository(over);
    }

    @Override
    public Over getOver(int matchId, int teamId, int bowlerId) {
        Optional<Over> optionalOver = overRepository.findByMatchIdAndTeamIdAndBowlerId(matchId, teamId, bowlerId);
        if(optionalOver.isPresent()) {
            return optionalOver.get();
        }
        throw new CricketScorecardException(ErrorCode.OVER_NOT_FOUND);
    }

    @Override
    public int totalOverBowled(int matchId, int teamId) {
        int ballsBowled = bowlService.getTotalBalls(matchId, teamId);
        return ballsBowled/6;
    }

    @Override
    public Over latestOverBowled(int matchId, int teamId, OverStatus overStatus) {
        Optional<Over> optionalOver = overRepository.findFirstByMatchIdAndTeamIdAndStatusOrderByCreatedAtDesc(matchId, teamId, overStatus);
        if(optionalOver.isPresent()) {
            return optionalOver.get();
        }
        return null;
    }

    @Override
    public boolean markOverCompleted(int matchId, int teamId) {
        Over latestOver = latestOverBowled(matchId, teamId, OverStatus.IN_PROGRESS);
        int validBowlBowled = bowlService.validBallBowled(latestOver.getMatchId(), latestOver.getTeamId(), latestOver.getBowlerId(), OverStatus.IN_PROGRESS);
        if(validBowlBowled == maxBallAllowed) {
            latestOver.setStatus(OverStatus.COMPLETED);
            saveDataToRepository(latestOver);
            markInningsOver(matchId, teamId);
            return true;
        }
        return false;
    }

    private boolean markInningsOver(int matchId, int teamId) {
        boolean areInningsOver = bowlService.isMaxOverBowled(matchId, teamId);
        if(areInningsOver) {
            Match match = matchService.getMatchs(matchId);
            if(InningType.INNING_1.equals(match.getInningType())) {
                match.setInningType(InningType.INNING_2);
            } else {
                match.setInningType(InningType.MATCH_FINISHED);
            }
            matchService.saveDataToRepository(match);
            return true;
        }
        return false;
    }


    private Over saveDataToRepository(Over over) {
        try {
            return overRepository.save(over);
        }
        catch(DataIntegrityViolationException e) {
            log.error("DataIntegrity error ", e);
            throw new CricketScorecardException(ErrorCode.OVER_CREATE_FAILURE);
        }
        catch(PersistenceException e) {
            log.error("Failed to save data ", e);
            throw new CricketScorecardException(ErrorCode.OVER_CREATE_FAILURE);
        }
    }

}
