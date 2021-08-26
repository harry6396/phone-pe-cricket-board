package com.phonepe.cricketscorecard.repository;

import com.phonepe.cricketscorecard.entity.Bowling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BowlRepository extends JpaRepository<Bowling, Integer> {

    List<Bowling> findByOverId(int overId);
    List<Bowling> findByMatchIdAndBatsmanTeamIdAndBatsmanId(int matchId, int batsmanTeamId, int batsmanId);
    List<Bowling> findByMatchIdAndBatsmanTeamId(int matchId, int batsmanTeamId);
    List<Bowling> findByMatchIdAndBowlingTeamId(int matchId, int bowlingTeamId);

}
