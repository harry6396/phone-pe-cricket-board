package com.phonepe.cricketscorecard.exception;

import com.phonepe.cricketscorecard.dto.Response;
import com.phonepe.cricketscorecard.enums.APIStatus;
import com.phonepe.cricketscorecard.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CricketScorecardExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CricketScorecardException.class)
    protected ResponseEntity<Response> handleCustomException(CricketScorecardException exception) {
        log.error("Custom exception ", exception);
        return buildResponseEntity(exception.getErrorCode());
    }

    private ResponseEntity<Response> buildResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(Response.builder()
                .httpStatus(errorCode.getHttpStatus())
                .apiStatus(APIStatus.FAILURE)
                .response(errorCode.getDescription())
                .build(), errorCode.getHttpStatus());
    }
}
