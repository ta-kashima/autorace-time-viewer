package com.example.demo;

public class RaceResult {
    private String raceDate;  // レース日
    private String condition;  // 走路状態（良、湿など）
    private double raceTime;  // 競走タイム
    private double handicap;  // ハンデ
    private double st;  // スタートタイミング

    // ゲッターとセッター
    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getRaceTime() {
        return raceTime;
    }

    public void setRaceTime(double raceTime) {
        this.raceTime = raceTime;
    }

    public double getHandicap() {
        return handicap;
    }

    public void setHandicap(double handicap) {
        this.handicap = handicap;
    }

    public double getSt() {
        return st;
    }

    public void setSt(double st) {
        this.st = st;
    }
}
