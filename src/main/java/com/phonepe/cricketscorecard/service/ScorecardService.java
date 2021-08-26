package com.phonepe.cricketscorecard.service;

import com.phonepe.cricketscorecard.dto.TeamLineUp;
import com.phonepe.cricketscorecard.entity.Scorecard;
import com.phonepe.cricketscorecard.enums.BatsmanEnd;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ScorecardService {

    List<Scorecard> createScoreCard(int matchId, String teamName, List<TeamLineUp> teamLineUpList);

    List<Scorecard> getScoreCard(int matchId, String teamName);

    Scorecard getScoreCard(int matchId, int teamId, BatsmanEnd batsmanEnd);

    Scorecard handleOutCase(int matchId, int teamId, int teamId1);

    void printScoreCard(int matchId, int teamId);
}
