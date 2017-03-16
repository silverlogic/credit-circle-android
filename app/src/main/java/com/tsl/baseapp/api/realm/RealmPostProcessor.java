package com.tsl.baseapp.api.realm;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.tsl.realm_sugar.Merger;

import io.gsonfire.PostProcessor;
import io.realm.RealmObject;

/**
 * Created by Manu on 6/1/17.
 */

public class RealmPostProcessor<T extends RealmObject> implements PostProcessor<T> {

    @Override
    public void postDeserialize(T result, JsonElement src, Gson gson) {
        if (src != null && src.isJsonObject()) {
            Merger.merge(result, src.getAsJsonObject());
        }
    }

    @Override
    public void postSerialize(JsonElement result, T src, Gson gson) {
        // empty, we don't need it.
    }
}
