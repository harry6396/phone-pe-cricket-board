package com.phonepe.cricketscorecard.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    MATCH_CREATE_FAILURE("CRICKET-1001", "Match could not be created", HttpStatus.BAD_REQUEST),
    MATCH_NOT_FOUND("CRICKET-1002", "Match could not be found", HttpStatus.NOT_FOUND),
    MATCH_ALREADY_FINISHED("CRICKET-1003", "Match already finished", HttpStatus.BAD_REQUEST),

    PLAYER_CREATE_FAILURE("CRICKET-2001", "Player could not be created", HttpStatus.BAD_REQUEST),
    PLAYER_NOT_FOUND("CRICKET-2002", "Player could not be found", HttpStatus.NOT_FOUND),

    SCORECARD_CREATE_FAILURE("CRICKET-3001", "Scorecard could not be created", HttpStatus.BAD_REQUEST),
    SCORECARD_NOT_FOUND("CRICKET-3002", "Scorecard could not be found", HttpStatus.NOT_FOUND),

    OVER_CREATE_FAILURE("CRICKET-4001", "Over could not be created", HttpStatus.BAD_REQUEST),
    OVER_NOT_FOUND("CRICKET-4003", "Over could not be found", HttpStatus.NOT_FOUND),
    OVER_NOT_ALLOWED("CRICKET-4004", "Could not bowl overs more than allowed", HttpStatus.BAD_REQUEST),

    BALL_BOWLED_FAILURE("CRICKET-5001", "Ball could not be bowled", HttpStatus.BAD_REQUEST),

    INNINGS_OVER("CRICKET-6001", "Innings over", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String description, HttpStatus httpStatus){
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
