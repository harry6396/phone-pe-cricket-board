package com.phonepe.cricketscorecard.repository;

import com.phonepe.cricketscorecard.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    Optional<Match> findById(int id);
}
