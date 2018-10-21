package com.tsl.money2020.model.objects;

import com.google.gson.annotations.SerializedName;

public class InviteVouche {

    @SerializedName("vouching_user")
    int vouchingUser;

    @SerializedName("loan")
    int loan;

    public InviteVouche(int vouchingUser, int loan) {
        this.vouchingUser = vouchingUser;
        this.loan = loan;
    }
}
