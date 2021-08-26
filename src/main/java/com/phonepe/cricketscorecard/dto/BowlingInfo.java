package com.phonepe.cricketscorecard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phonepe.cricketscorecard.enums.ScoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BowlingInfo {

    @Enumerated(EnumType.STRING)
    private ScoreType scoreType;

}
