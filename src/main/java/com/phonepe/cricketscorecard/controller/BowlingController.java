package com.phonepe.cricketscorecard.controller;

import com.phonepe.cricketscorecard.dto.BowlingInfo;
import com.phonepe.cricketscorecard.dto.Response;
import com.phonepe.cricketscorecard.enums.APIStatus;
import com.phonepe.cricketscorecard.service.BowlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bowl")
public class BowlingController {

    @Autowired
    BowlService bowlService;

    @PostMapping
    public ResponseEntity<Response> bowl(@RequestParam("matchId") int matchId, @RequestParam("batingTeamId") int batingTeamId, @RequestParam("bowlingTeamId") int bowlingTeamId, @RequestParam("bowlerId") int bowlerId, @RequestBody BowlingInfo bowlingInfo) {

        return new ResponseEntity(Response.builder()
                .httpStatus(HttpStatus.CREATED)
                .apiStatus(APIStatus.SUCCESS)
                .response(bowlService.bowl(bowlingInfo.getScoreType(), matchId, batingTeamId, bowlingTeamId, bowlerId))
                .build(), HttpStatus.CREATED);
    }

}
