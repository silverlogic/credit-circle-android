package com.tsl.creditcircle.model.event;

import com.tsl.creditcircle.model.objects.token.Token;

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
