package com.example.GotNext.Requests;

public class CreateTeamRequest {

    private String leaderId;
    private String teamName;
    private int size;

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    private String court;

    public String getLeaderId() {
        return leaderId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }
}
