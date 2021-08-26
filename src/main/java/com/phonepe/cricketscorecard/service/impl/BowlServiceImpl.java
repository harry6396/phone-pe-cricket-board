package com.phonepe.cricketscorecard.service.impl;

import com.phonepe.cricketscorecard.entity.Bowling;
import com.phonepe.cricketscorecard.entity.Match;
import com.phonepe.cricketscorecard.entity.Over;
import com.phonepe.cricketscorecard.entity.Scorecard;
import com.phonepe.cricketscorecard.enums.*;
import com.phonepe.cricketscorecard.exception.CricketScorecardException;
import com.phonepe.cricketscorecard.repository.BowlRepository;
import com.phonepe.cricketscorecard.service.BowlService;
import com.phonepe.cricketscorecard.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
@Slf4j
public class BowlServiceImpl implements BowlService {

    @Autowired
    BowlRepository bowlRepository;
    @Autowired
    ScorecardServiceImpl scorecardService;
    @Autowired
    OverServiceImpl overService;
    @Autowired
    MatchService matchService;

    @Value("${cricket.maxBallAllowed}")
    private Integer maxBallAllowed;

    @Override
    public Bowling bowl(ScoreType scoreType, int matchId, int batingTeamId, int bowlingTeamId, int bowlerId) {

        validate(matchId, bowlingTeamId, bowlerId);

        Over over = overService.createOver(matchId, bowlingTeamId, bowlerId);

        Scorecard scorecard1 = scorecardService.getScoreCard(matchId, batingTeamId, BatsmanEnd.STRIKER);
        Scorecard scorecard2 = scorecardService.getScoreCard(matchId, batingTeamId, BatsmanEnd.NON_STRIKER);

        Bowling bowling = Bowling.builder()
                .matchId(matchId)
                .bowlerId(bowlerId)
                .scoreType(scoreType)
                .extra(scoreType.getExtra())
                .batsmanId(scorecard1.getPlayerId())
                .bowlingTeamId(bowlingTeamId)
                .batsmanTeamId(batingTeamId)
                .overId(over.getId())
                .build();

        bowling = saveDataToRepository(bowling);

        if(!ScoreType.W.equals(scoreType)) {
            scorecard1.setBatsmanEnd(scoreType.getBatsmanEnd1());
            scorecard2.setBatsmanEnd(scoreType.getBatsmanEnd2());

            scorecardService.saveDataToRepository(scorecard1);
            scorecardService.saveDataToRepository(scorecard2);
        }

        if(ScoreType.W.equals(scoreType)) {
            scorecardService.handleOutCase(matchId, batingTeamId, bowlingTeamId);
        }

        InningType inningType = matchService.getMatchs(matchId).getInningType();
        boolean isOverCompleted = overService.markOverCompleted(matchId, bowlingTeamId);

        if(isOverCompleted) {
            scorecard1.setBatsmanEnd(scoreType.getBatsmanEnd2());
            scorecard2.setBatsmanEnd(scoreType.getBatsmanEnd1());

            scorecardService.saveDataToRepository(scorecard1);
            scorecardService.saveDataToRepository(scorecard2);

            scorecardService.printScoreCard(matchId, batingTeamId);
        }
        if(inningType.equals(InningType.INNING_2)) {
            scorecardService.printWinner(matchId, bowlingTeamId, batingTeamId);
        }
        return bowling;
    }

    @Override
    public int validBallBowled(int matchId, int teamId, int bowlerId, OverStatus overStatus) {
        Over latestOver = overService.latestOverBowled(matchId, teamId, overStatus);
        List<Bowling> bowlingList = bowlRepository.findByOverId(latestOver.getId());
        int counter = 0;
        for(Bowling bowling : bowlingList) {
            if(bowling.isValidDelivery()) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public int validBallBowled(Over latestOver) {
        if(ObjectUtils.isEmpty(latestOver)) {
            return 0;
        }
        List<Bowling> bowlingList = latestOver.getBowlingList();
        if(CollectionUtils.isEmpty(bowlingList)) {
            return 0;
        }
        int counter = 0;
        for(Bowling bowling : bowlingList) {
            if(bowling.isValidDelivery()) {
                counter++;
            }
        }
        return counter;
    }

    private Bowling saveDataToRepository(Bowling bowling) {
        try {
            return bowlRepository.save(bowling);
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

    private void validate(int matchId, int teamId, int bowlerId) {
        Match match = matchService.getMatchs(matchId);
        if (InningType.MATCH_FINISHED.equals(match.getInningType())) {
            throw new CricketScorecardException(ErrorCode.MATCH_ALREADY_FINISHED);
        }
        boolean isInningOver = isMaxOverBowled(matchId, teamId);
        if(isInningOver) {
            throw new CricketScorecardException(ErrorCode.INNINGS_OVER);
        }
        Over latestOver = overService.latestOverBowled(matchId, teamId, OverStatus.IN_PROGRESS);
        if(latestOver == null) {
            return;
        }
        int validBallBowled = validBallBowled(latestOver);
        if(validBallBowled == maxBallAllowed) {
            throw new CricketScorecardException(ErrorCode.BALL_BOWLED_FAILURE);
        }
        if(latestOver.getBowlerId() != bowlerId) {
            throw new CricketScorecardException(ErrorCode.OVER_NOT_ALLOWED);
        }
    }

    public boolean isMaxOverBowled(int matchId, int teamId) {
        int maxOverBowled = overService.totalOverBowled(matchId, teamId);
        Match match = matchService.getMatchs(matchId);
        if(maxOverBowled >= match.getOvers()) {
            return true;
        }
        return false;
    }

    public int calculateScore(int matchId, int teamid, int playerId) {
        int score = 0;
        List<Bowling> bowlingList = bowlRepository.findByMatchIdAndBatsmanTeamIdAndBatsmanId(matchId, teamid, playerId);
        for(Bowling bowling : bowlingList) {
            if(bowling.getScoreType().canBeCalculated()) {
                score += Integer.valueOf(bowling.getScoreType().getValue());
            }
        }
        return score;
    }

    public int getTotalBoundaries(int matchId, int teamid, int playerId, ScoreType scoreType) {
        int score = 0;
        List<Bowling> bowlingList = bowlRepository.findByMatchIdAndBatsmanTeamIdAndBatsmanId(matchId, teamid, playerId);
        for(Bowling bowling : bowlingList) {
            if(scoreType.equals(bowling.getScoreType())) {
                score++;
            }
        }
        return score;
    }

    public int getTotalBalls(int matchId, int teamid, int playerId) {
        int score = 0;
        List<Bowling> bowlingList = bowlRepository.findByMatchIdAndBatsmanTeamIdAndBatsmanId(matchId, teamid, playerId);
        for(Bowling bowling : bowlingList) {
            if(bowling.isValidDelivery()) {
                score++;
            }
        }
        return score;
    }

    public int getTotalBalls(int matchId, int teamId) {
        int score = 0;
        List<Bowling> bowlingList = bowlRepository.findByMatchIdAndBowlingTeamId(matchId, teamId);
        for(Bowling bowling : bowlingList) {
            if(bowling.isValidDelivery()) {
                score++;
            }
        }
        return score;
    }

    public int getTotalScore(int matchId, int teamId) {
        int score = 0;
        List<Bowling> bowlingList = bowlRepository.findByMatchIdAndBatsmanTeamId(matchId, teamId);
        for(Bowling bowling : bowlingList) {
            if(bowling.isValidDelivery()) {
                score += Integer.valueOf(bowling.getScoreType().getValue());
            } else {
                score += Integer.valueOf(bowling.getScoreType().getExtra());
            }
        }
        return score;
    }

    public int getTotalOutPlayers(int matchId, int teamId) {
        int score = 0;
        List<Bowling> bowlingList = bowlRepository.findByMatchIdAndBowlingTeamId(matchId, teamId);
        for(Bowling bowling : bowlingList) {
            if(ScoreType.W.equals(bowling.getScoreType())) {
                score++;
            }
        }
        return score;
    }
}
