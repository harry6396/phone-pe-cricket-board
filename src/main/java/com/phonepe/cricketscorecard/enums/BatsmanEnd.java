package com.phonepe.cricketscorecard.enums;

public enum BatsmanEnd {

    STRIKER("Striker"),
    NON_STRIKER("Non striker"),
    OUT("Out");

    BatsmanEnd(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
