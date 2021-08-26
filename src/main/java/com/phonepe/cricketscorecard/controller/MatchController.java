package com.phonepe.cricketscorecard.controller;

import com.phonepe.cricketscorecard.dto.MatchInfo;
import com.phonepe.cricketscorecard.dto.Response;
import com.phonepe.cricketscorecard.enums.APIStatus;
import com.phonepe.cricketscorecard.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/match")
public class MatchController {

    @Autowired
    MatchService matchService;

    @PostMapping
    public ResponseEntity<Response> createMatch(@Valid @RequestBody MatchInfo matchInfo) {
        Response response = Response.builder()
                .response(matchService.createMatch(matchInfo))
                .apiStatus(APIStatus.SUCCESS)
                .httpStatus(HttpStatus.CREATED)
                .build();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Response> getMatchs(@RequestParam("matchId") int matchId) {
        Response response = Response.builder()
                .response(matchService.getMatchs(matchId))
                .apiStatus(APIStatus.SUCCESS)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
