package com.tsl.money2020.api.expandable.annotations;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Created by Manu on 6/1/17.
 */

/**
 * A converterClass which will convert primitive objects in a json objects with the key "id".
 * For instance, if we have `1`, it will be converted to {"id": 1}.
 */
public class DefaultExpandableFieldConverter extends ExpandableFieldConverter {

    private final static String ID_KEY = "id";

    public DefaultExpandableFieldConverter() {}

    @Override
    public JsonElement convert(JsonPrimitive primitive) {
        JsonObject object = new JsonObject();
        object.add(ID_KEY, primitive);
        return object;
    }
}
