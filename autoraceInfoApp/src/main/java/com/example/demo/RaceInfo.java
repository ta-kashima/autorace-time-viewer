package com.example.demo;

import java.util.List;

public class RaceInfo {
    private String carNo;
    private String name;
    private String affiliation;
    private double handicap;
    private double tryTime;
    private double deviation;
    private double assumedTime;
    private double expectedFinishTime;
    private String goodWinRate2;
    private String goodWinRate3;
    private String wetWinRate2;
    private String wetWinRate3;

    // 新たに追加されたフィールド
    private List<RaceResult> recentResults;  // 前5走成績

    // ゲッターとセッター
    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public double getHandicap() {
        return handicap;
    }

    public void setHandicap(double handicap) {
        this.handicap = handicap;
    }

    public double getTryTime() {
        return tryTime;
    }

    public void setTryTime(double tryTime) {
        this.tryTime = tryTime;
    }

    public double getDeviation() {
        return deviation;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }

    public double getAssumedTime() {
        return assumedTime;
    }

    public void setAssumedTime(double assumedTime) {
        this.assumedTime = assumedTime;
    }

    public double getExpectedFinishTime() {
        return expectedFinishTime;
    }

    public void setExpectedFinishTime(double expectedFinishTime) {
        this.expectedFinishTime = expectedFinishTime;
    }

    public String getGoodWinRate2() {
        return goodWinRate2;
    }

    public void setGoodWinRate2(String goodWinRate2) {
        this.goodWinRate2 = goodWinRate2;
    }

    public String getGoodWinRate3() {
        return goodWinRate3;
    }

    public void setGoodWinRate3(String goodWinRate3) {
        this.goodWinRate3 = goodWinRate3;
    }

    public String getWetWinRate2() {
        return wetWinRate2;
    }

    public void setWetWinRate2(String wetWinRate2) {
        this.wetWinRate2 = wetWinRate2;
    }

    public String getWetWinRate3() {
        return wetWinRate3;
    }

    public void setWetWinRate3(String wetWinRate3) {
        this.wetWinRate3 = wetWinRate3;
    }

    public List<RaceResult> getRecentResults() {
        return recentResults;
    }

    public void setRecentResults(List<RaceResult> recentResults) {
        this.recentResults = recentResults;
    }
}