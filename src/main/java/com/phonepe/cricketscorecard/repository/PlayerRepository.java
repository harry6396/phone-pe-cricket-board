package com.phonepe.cricketscorecard.repository;

import com.phonepe.cricketscorecard.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    Optional<Player> findByNameAndTeamId(String name, int teamId);
}
