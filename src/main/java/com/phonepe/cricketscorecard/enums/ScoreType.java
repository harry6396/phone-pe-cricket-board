package com.phonepe.cricketscorecard.enums;

public enum ScoreType {
    ZERO("0", BatsmanEnd.STRIKER, BatsmanEnd.NON_STRIKER, 0),
    ONE("1", BatsmanEnd.NON_STRIKER, BatsmanEnd.STRIKER, 0),
    TWO("2", BatsmanEnd.STRIKER, BatsmanEnd.NON_STRIKER, 0),
    THREE("3", BatsmanEnd.NON_STRIKER, BatsmanEnd.STRIKER, 0),
    FOUR("4", BatsmanEnd.STRIKER, BatsmanEnd.NON_STRIKER, 0),
    FIVE("5", BatsmanEnd.STRIKER, BatsmanEnd.NON_STRIKER, 0),
    SIX("6", BatsmanEnd.STRIKER, BatsmanEnd.NON_STRIKER, 0),
    W("W", BatsmanEnd.OUT, BatsmanEnd.NON_STRIKER, 0),
    Wd("Wd", BatsmanEnd.STRIKER, BatsmanEnd.NON_STRIKER, 1),
    NB("NB", BatsmanEnd.STRIKER, BatsmanEnd.NON_STRIKER, 1);

    private final String value;
    private final BatsmanEnd batsmanEnd1;
    private final BatsmanEnd batsmanEnd2;
    private final Integer extra;

    ScoreType(String value, BatsmanEnd batsmanEnd1, BatsmanEnd batsmanEnd2, Integer extra) {
        this.value = value;
        this.batsmanEnd1 = batsmanEnd1;
        this.batsmanEnd2 = batsmanEnd2;
        this.extra = extra;
    }

    public String getValue() {
        return value;
    }

    public BatsmanEnd getBatsmanEnd1() {
        return batsmanEnd1;
    }

    public BatsmanEnd getBatsmanEnd2() {
        return batsmanEnd2;
    }

    public Integer getExtra() {
        return extra;
    }

    public boolean canBeCalculated() {
        return !(this.equals(ScoreType.NB) ||
                this.equals(ScoreType.W) ||
                this.equals(ScoreType.Wd));
    }
}
