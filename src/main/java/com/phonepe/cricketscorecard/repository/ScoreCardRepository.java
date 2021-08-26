package com.phonepe.cricketscorecard.repository;

import com.phonepe.cricketscorecard.entity.Scorecard;
import com.phonepe.cricketscorecard.enums.BatsmanEnd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreCardRepository extends JpaRepository<Scorecard, Integer> {
    List<Scorecard> findByMatchIdAndTeamId(int matchId, int teamId);
    List<Scorecard> findByMatchId(int matchId);
    Scorecard findByMatchIdAndTeamIdAndBatsmanEnd(int matchId, int teamId, BatsmanEnd batsmanEnd);
    Scorecard findByMatchIdAndTeamIdAndPlayerLineUpNumber(int matchId, int teamId, int playerLineUpNumber);
}
