package com.phonepe.cricketscorecard.exception;

import com.phonepe.cricketscorecard.enums.ErrorCode;
import lombok.Data;

@Data
public class CricketScorecardException extends RuntimeException {
    private final ErrorCode errorCode;
    public CricketScorecardException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
