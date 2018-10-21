package com.tsl.creditcircle.model.objects;

import com.google.gson.annotations.SerializedName;

public class Loan {
    @SerializedName("id")
    int id;

    @SerializedName("original_amount")
    int originalAmount;

    public int getId() {
        return id;
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(int originalAmount) {
        this.originalAmount = originalAmount;
    }
}
