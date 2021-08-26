package com.phonepe.cricketscorecard.service;

import com.phonepe.cricketscorecard.entity.Player;
import org.springframework.stereotype.Component;

@Component
public interface PlayerService {

    Player createPlayer(String playerName, int teamId);

}
