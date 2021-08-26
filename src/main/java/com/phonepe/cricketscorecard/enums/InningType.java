package com.phonepe.cricketscorecard.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InningType {
    INNING_1("Inning 1"),
    INNING_2("Inning 2"),
    MATCH_FINISHED("Match Finished");

    private final String value;

    public String getValue() {
        return value;
    }
}
