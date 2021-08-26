package com.phonepe.cricketscorecard.service;

import com.phonepe.cricketscorecard.entity.Over;
import com.phonepe.cricketscorecard.enums.OverStatus;
import org.springframework.stereotype.Component;

@Component
public interface OverService {

    Over createOver(int matchId, int teamId, int bowlerId);
    Over getOver(int matchId, int teamId, int bowlerId);
    int totalOverBowled(int matchId, int teamId);
    Over latestOverBowled(int matchId, int teamId, OverStatus overStatus);
    boolean markOverCompleted(int matchId, int teamId);

}
