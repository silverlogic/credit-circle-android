package com.tsl.baseapp.api.expandable.annotations;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Created by Manu on 6/1/17.
 */

/**
 * A converterClass which will convert primitive objects into a json element.
 */
public abstract class ExpandableFieldConverter {
    public ExpandableFieldConverter() {
    }
    public abstract JsonElement convert(JsonPrimitive primitive);
}
