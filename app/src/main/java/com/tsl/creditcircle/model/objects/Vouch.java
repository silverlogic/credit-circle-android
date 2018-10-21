package com.tsl.creditcircle.model.objects;

import com.google.gson.annotations.SerializedName;

public class Vouch {

    @SerializedName("status")
    String status;

    @SerializedName("vouch_amount")
    int vouchAmount;

    @SerializedName("invest_amount")
    int investAmount;


    public Vouch(String status, int vouchAmount, int investAmount) {
        this.status = status;
        this.vouchAmount = vouchAmount;
        this.investAmount = investAmount;
    }

    public String getStatus() {
        return status;
    }

    public int getVouchAmount() {
        return vouchAmount;
    }

    public int getInvestAmount() {
        return investAmount;
    }
}
