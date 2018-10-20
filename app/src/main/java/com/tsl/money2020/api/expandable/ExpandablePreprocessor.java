package com.tsl.money2020.api.expandable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tsl.money2020.api.expandable.annotations.ExpandableFieldConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.gsonfire.PreProcessor;

/**
 * Created by Manu on 6/1/17.
 */


public class ExpandablePreprocessor implements PreProcessor<Expandable>  {
    @Override
    public void preDeserialize(Class<? extends Expandable> clazz, JsonElement src, Gson gson) {
        if (src.isJsonObject()) {
            modifyJson(src.getAsJsonObject(), clazz);
        }
    }

    private void modifyJson(JsonObject jsonObject, Type typeOfT) throws JsonParseException {
        ArrayList<ExpandableFieldInfo> infoForType = ExpandableFieldInfo.getInfo(typeOfT);

        for (ExpandableFieldInfo info : infoForType) {
            String key = info.getSerializedName();
            if (jsonObject.has(key)) {
                JsonElement element = jsonObject.get(key);
                JsonElement modifiedElement = getJsonByReplacingExpandableField(element, info);
                jsonObject.add(key, modifiedElement);
            }
        }
    }

    private JsonElement getJsonByReplacingExpandableField(JsonElement jsonElement, ExpandableFieldInfo info) throws JsonParseException {

        try {

            JsonElement returnValue = jsonElement;
            ExpandableFieldConverter converter = info.getConverter();

            // if is a primitive, convert to json object
            if (jsonElement.isJsonPrimitive()) {
                return converter.convert(jsonElement.getAsJsonPrimitive());
            }

            // if is an array of primitives we convert to an array of objects
            else if (jsonElement.isJsonArray() &&
                    jsonElement.getAsJsonArray().size() > 0 &&
                    jsonElement.getAsJsonArray().get(0).isJsonPrimitive()) {

                JsonArray array = new JsonArray();
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    JsonElement convertedElement = converter.convert(element.getAsJsonPrimitive());
                    array.add(convertedElement);
                }
                return array;
            }

            // otherwise, return the object as is
            else  {
                return jsonElement;
            }

        }

        catch (InstantiationException e) {
            throw new JsonParseException(e);
        }

        catch (IllegalAccessException e) {
            throw new JsonParseException(e);
        }
    }

}
