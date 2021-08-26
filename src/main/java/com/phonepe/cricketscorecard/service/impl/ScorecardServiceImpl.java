package com.phonepe.cricketscorecard.service.impl;

import com.phonepe.cricketscorecard.dto.TeamLineUp;
import com.phonepe.cricketscorecard.entity.*;
import com.phonepe.cricketscorecard.enums.BatsmanEnd;
import com.phonepe.cricketscorecard.enums.ErrorCode;
import com.phonepe.cricketscorecard.enums.InningType;
import com.phonepe.cricketscorecard.enums.ScoreType;
import com.phonepe.cricketscorecard.exception.CricketScorecardException;
import com.phonepe.cricketscorecard.repository.ScoreCardRepository;
import com.phonepe.cricketscorecard.service.PlayerService;
import com.phonepe.cricketscorecard.service.ScorecardService;
import com.phonepe.cricketscorecard.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
@Slf4j
public class ScorecardServiceImpl implements ScorecardService {

    @Autowired
    private MatchServiceImpl matchService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ScoreCardRepository scoreCardRepository;
    @Autowired
    private BowlServiceImpl bowlService;

    @Override
    public List<Scorecard> createScoreCard(int matchId, String teamName, List<TeamLineUp> teamLineUpList) {
        Match match = matchService.getMatchs(matchId);
        Team team = teamService.createTeam(teamName);
        List<Scorecard> scorecardList = scoreCardRepository.findByMatchId(matchId);
        int count = 1;
        for(TeamLineUp teamLineUp : teamLineUpList) {
            Player player = playerService.createPlayer(teamLineUp.getPlayerName(), team.getId());
            Scorecard scorecard = Scorecard.builder()
                    .matchId(match.getId())
                    .playerId(player.getId())
                    .teamId(team.getId())
                    .playerLineUpNumber(count)
                    .build();
            if(count == 1) {
                scorecard.setBatsmanEnd(BatsmanEnd.STRIKER);
            } else if(count == 2) {
                scorecard.setBatsmanEnd(BatsmanEnd.NON_STRIKER);
            }
            if(CollectionUtils.isEmpty(scorecardList)) {
                scorecard.setInningType(InningType.INNING_1);
            } else {
                match.setInningType(InningType.INNING_2);
                matchService.saveDataToRepository(match);
                scorecard.setInningType(InningType.INNING_2);
            }
            count++;
            saveDataToRepository(scorecard);
        }
        return getScoreCard(matchId, teamName);
    }

    @Override
    public List<Scorecard> getScoreCard(int matchId, String teamName) {
        Team team = teamService.createTeam(teamName);
        List<Scorecard> scorecardList = scoreCardRepository.findByMatchIdAndTeamId(matchId, team.getId());
        if (CollectionUtils.isEmpty(scorecardList)) {
            throw new CricketScorecardException(ErrorCode.SCORECARD_NOT_FOUND);
        }
        return scorecardList;
    }

    @Override
    public Scorecard getScoreCard(int matchId, int teamId, BatsmanEnd batsmanEnd) {
        return scoreCardRepository.findByMatchIdAndTeamIdAndBatsmanEnd(matchId, teamId, batsmanEnd);
    }

    @Override
    public Scorecard handleOutCase(int matchId, int teamId, int teamId1) {
        Scorecard scorecard = getScoreCard(matchId, teamId, BatsmanEnd.STRIKER);
        scorecard.setBatsmanEnd(BatsmanEnd.OUT);
        saveDataToRepository(scorecard);
        int sequence1 = scorecard.getPlayerLineUpNumber();
        scorecard = getScoreCard(matchId, teamId, BatsmanEnd.NON_STRIKER);
        int sequence2 = scorecard.getPlayerLineUpNumber();
        int maxSequence = Math.max(sequence1, sequence2)+1;
        Match match = matchService.getMatchs(matchId);
        if(match.getNoOfTeamMembers() < maxSequence) {
            if(InningType.INNING_1.equals(match.getInningType())) {
                match.setInningType(InningType.INNING_2);
                matchService.saveDataToRepository(match);
            } else {
                match.setInningType(InningType.MATCH_FINISHED);
                matchService.saveDataToRepository(match);
                printWinner(matchId, teamId1, teamId);
            }
            throw new CricketScorecardException(ErrorCode.INNINGS_OVER);
        }
        scorecard = scoreCardRepository.findByMatchIdAndTeamIdAndPlayerLineUpNumber(matchId, teamId, maxSequence);
        scorecard.setBatsmanEnd(BatsmanEnd.STRIKER);
        return saveDataToRepository(scorecard);
    }

    @Override
    public void printScoreCard(int matchId, int teamId) {
        System.out.println("Player Name\tScore\t4s\t6s\tBalls");
        List<Scorecard> scorecardList = scoreCardRepository.findByMatchIdAndTeamId(matchId, teamId);
        for(Scorecard scorecard : scorecardList) {
            Player player = scorecard.getPlayer();
            String playerName = player.getName();
            if(BatsmanEnd.STRIKER.equals(scorecard.getBatsmanEnd())) {
                playerName += "*";
            }
            int score = bowlService.calculateScore(matchId, teamId, player.getId());
            int total4s = bowlService.getTotalBoundaries(matchId, teamId, player.getId(), ScoreType.FOUR);
            int total6s = bowlService.getTotalBoundaries(matchId, teamId, player.getId(), ScoreType.SIX);
            int totalBalls = bowlService.getTotalBalls(matchId, teamId, player.getId());
            System.out.println(playerName + "\t" + score + "\t" + total4s + "\t" + total6s + "\t" + totalBalls);
        }
    }

    public Scorecard saveDataToRepository(Scorecard scorecard) {
        try {
            return scoreCardRepository.save(scorecard);
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

    public void printWinner(int matchId, int teamIdA, int teamIdB) {
        Match match = matchService.getMatchs(matchId);
        int totalScoreA = bowlService.getTotalScore(matchId, teamIdA);
        int totalScoreB = bowlService.getTotalScore(matchId, teamIdB);
        if(totalScoreA < totalScoreB && InningType.INNING_2.equals(match.getInningType())) {
            System.out.println("Team B won by " + (match.getNoOfTeamMembers() - bowlService.getTotalOutPlayers(matchId, teamIdB)) + " wickets");
            match.setInningType(InningType.MATCH_FINISHED);
            matchService.saveDataToRepository(match);
        } else if (totalScoreA > totalScoreB && InningType.MATCH_FINISHED.equals(match.getInningType())) {
            System.out.println("Team A won by " + (totalScoreA - totalScoreB) + " runs");
            match.setInningType(InningType.MATCH_FINISHED);
            matchService.saveDataToRepository(match);
        }
    }

}
