package com.phonepe.cricketscorecard.service.impl;

import com.phonepe.cricketscorecard.entity.Player;
import com.phonepe.cricketscorecard.enums.ErrorCode;
import com.phonepe.cricketscorecard.exception.CricketScorecardException;
import com.phonepe.cricketscorecard.repository.PlayerRepository;
import com.phonepe.cricketscorecard.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Service
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player createPlayer(String playerName, int teamId) {
        Optional<Player> optionalPlayer = playerRepository.findByNameAndTeamId(playerName, teamId);
        if (optionalPlayer.isPresent()) {
            optionalPlayer.get();
        }
        Player player = Player.builder()
                .name(playerName)
                .teamId(teamId)
                .build();
        return saveDataToRepository(player);
    }

    private Player saveDataToRepository(Player player) {
        try {
            return playerRepository.save(player);
        }
        catch(DataIntegrityViolationException e) {
            log.error("DataIntegrity error ", e);
            throw new CricketScorecardException(ErrorCode.PLAYER_CREATE_FAILURE);
        }
        catch(PersistenceException e) {
            log.error("Failed to save data ", e);
            throw new CricketScorecardException(ErrorCode.PLAYER_CREATE_FAILURE);
        }
    }
}
