package com.tsl.baseapp.model.objects;

/**
 * Created by Kevin on 9/17/15.
 */
public class Player {

    //pojo getters and setters

    public String mName;
    public String mPosition;
    public String mTeam;
    public String mStandardScoring;
    public String mPprScoring;


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPosition() {
        return mPosition;
    }

    public void setPosition(String position) {
        mPosition = position;
    }

    public String getTeam() {
        return mTeam;
    }

    public void setTeam(String team) {
        mTeam = team;
    }

    public String getStandardScoring() {
        return mStandardScoring;
    }

    public void setStandardScoring(String standardScoring) {
        mStandardScoring = standardScoring;
    }

    public String getPprScoring() {
        return mPprScoring;
    }

    public void setPprScoring(String pprScoring) {
        mPprScoring = pprScoring;
    }


}
