package com.phonepe.cricketscorecard.enums;

public enum OverStatus {
    COMPLETED("Completed"),
    IN_PROGRESS("In Progress");

    private final String value;

    OverStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
