package com.tsl.creditcircle.model.event;

import com.tsl.creditcircle.model.objects.Friend;
import com.tsl.creditcircle.model.objects.Vouch;
import com.tsl.creditcircle.model.objects.user.User;

import java.util.List;

public class VouchEvent {
    private Friend friend;
    private int vouchAmount;
    private int investAmount;

    public VouchEvent(Friend friend, int vouchAmount, int investAmount) {
        this.friend = friend;
        this.vouchAmount = vouchAmount;
        this.investAmount = investAmount;
    }

    public Friend getFriend() {
        return friend;
    }

    public int getVouchAmount() {
        return vouchAmount;
    }

    public int getInvestAmount() {
        return investAmount;
    }
}
