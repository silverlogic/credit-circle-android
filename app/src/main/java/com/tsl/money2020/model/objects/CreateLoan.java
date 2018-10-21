package com.tsl.money2020.model.objects;

import com.google.gson.annotations.SerializedName;

public class CreateLoan {
    @SerializedName("original_amount")
    int originalAmount;


    public int getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(int originalAmount) {
        this.originalAmount = originalAmount;
    }
}
