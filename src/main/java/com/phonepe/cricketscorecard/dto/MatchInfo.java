package com.phonepe.cricketscorecard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchInfo {

    @Min(message =  "Team members cannot be less than 2", value = 2)
    private Integer teamMembers;

    @Min(message =  "Over cannot be less than 1", value = 1)
    private Integer over;

    @NotEmpty(message = "Team name cannot be empty")
    private String teamA;

    @NotEmpty(message = "Team name cannot be empty")
    private String teamB;

    @NotEmpty(message = "Venue cannot be empty")
    private String venue;
}
