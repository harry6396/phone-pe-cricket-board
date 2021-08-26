package com.phonepe.cricketscorecard.controller;

import com.phonepe.cricketscorecard.dto.Response;
import com.phonepe.cricketscorecard.dto.TeamLineUp;
import com.phonepe.cricketscorecard.enums.APIStatus;
import com.phonepe.cricketscorecard.service.ScorecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scorecard")
public class ScoreCardController {

    @Autowired
    ScorecardService service;

    @PostMapping
    public ResponseEntity<Response> buildScoreCard(@RequestParam("matchId") int matchId, @RequestParam("teamName") String teamName, @RequestBody List<TeamLineUp> lineUp) {
        return new ResponseEntity(Response.builder()
                .response(service.createScoreCard(matchId, teamName, lineUp))
                .apiStatus(APIStatus.SUCCESS)
                .httpStatus(HttpStatus.CREATED)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Response> getScoreCard(@RequestParam("matchId") int matchId, @RequestParam("teamName") String teamName) {
        return new ResponseEntity(Response.builder()
                .response(service.getScoreCard(matchId, teamName))
                .apiStatus(APIStatus.SUCCESS)
                .httpStatus(HttpStatus.CREATED)
                .build(), HttpStatus.CREATED);
    }

}
