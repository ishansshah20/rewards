package com.fetchrewards.rewards.dto;

public class SpentPointsDTO {
    long points;
    String payer;

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }
}
