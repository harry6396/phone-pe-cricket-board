package com.phonepe.cricketscorecard.service;

import com.phonepe.cricketscorecard.entity.Team;
import org.springframework.stereotype.Component;

@Component
public interface TeamService {

    Team createTeam(String teamName);
}
