package com.phonepe.cricketscorecard.service;

import com.phonepe.cricketscorecard.dto.MatchInfo;
import com.phonepe.cricketscorecard.entity.Match;
import org.springframework.stereotype.Component;

@Component
public interface MatchService {

    Match createMatch(MatchInfo matchInfo);

    Match getMatchs(int matchId);

}
