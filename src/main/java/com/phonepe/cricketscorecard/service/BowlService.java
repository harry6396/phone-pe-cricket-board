package com.phonepe.cricketscorecard.service;

import com.phonepe.cricketscorecard.entity.Bowling;
import com.phonepe.cricketscorecard.entity.Over;
import com.phonepe.cricketscorecard.enums.OverStatus;
import com.phonepe.cricketscorecard.enums.ScoreType;
import org.springframework.stereotype.Component;

@Component
public interface BowlService {

    Bowling bowl(ScoreType scoreType, int matchId, int batingTeamId, int bowlingTeamId, int bowlerId);

    int validBallBowled(int matchId, int teamId, int bowlerId, OverStatus overStatus);

    int validBallBowled(Over latestOver);
}
