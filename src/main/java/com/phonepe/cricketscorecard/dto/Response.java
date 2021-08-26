package com.phonepe.cricketscorecard.dto;

import com.phonepe.cricketscorecard.enums.APIStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class Response {

    private APIStatus apiStatus;
    private Object response;
    private HttpStatus httpStatus;
}
