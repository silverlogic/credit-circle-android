package com.tsl.baseapp.model.event;

import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.User;

/**
 * Created by Kevin Lavi on 3/30/17.
 */

public class TokenEvent {
    private Token mToken;

    public TokenEvent(Token token) {
        mToken = token;
    }

    public String getToken() {
        return mToken.getToken();
    }
}
