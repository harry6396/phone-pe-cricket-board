package com.phonepe.cricketscorecard.repository;

import com.phonepe.cricketscorecard.entity.Over;
import com.phonepe.cricketscorecard.enums.OverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OverRepository extends JpaRepository<Over, Integer>, JpaSpecificationExecutor<Over> {

    List<Over> findByMatchIdAndTeamId(int matchId, int teamId);

    Optional<Over> findByMatchIdAndTeamIdAndBowlerId(int matchId, int teamId, int bowlerId);

    Optional<Over> findFirstByMatchIdAndTeamIdAndStatusOrderByCreatedAtDesc(int matchId, int teamId, OverStatus overStatus);

}
