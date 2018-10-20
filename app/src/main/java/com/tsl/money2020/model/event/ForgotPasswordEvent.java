package com.tsl.money2020.model.event;

/**
 * Created by Kevin Lavi on 8/15/16.
 */

public class ForgotPasswordEvent {
    private String message;

    public ForgotPasswordEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
