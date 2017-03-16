package com.tsl.baseapp.api.expandable;

import com.google.gson.annotations.SerializedName;
import com.tsl.baseapp.api.expandable.annotations.DefaultExpandableFieldConverter;
import com.tsl.baseapp.api.expandable.annotations.ExpandableField;
import com.tsl.baseapp.api.expandable.annotations.ExpandableFieldConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Manu on 6/1/17.
 */

/**
 * Class which store the relevant info for a Expanded Field
 */
class ExpandableFieldInfo {

    private String serializedName;
    private Type fieldType;
    private Class<? extends ExpandableFieldConverter> converterClass;

    /**
     * Get the info of all the expandable fields for the given type
     * @param type the type we want to get the info from
     * @return an ArrayList with the info.
     */
    public static ArrayList<ExpandableFieldInfo> getInfo(Type type) {
        return Cache.getCachedInfo(type);
    }

    private ExpandableFieldInfo(String serializedName, Type fieldType, Class<? extends ExpandableFieldConverter> converterClass) {
        this.serializedName = serializedName;
        this.fieldType = fieldType;
        this.converterClass = converterClass;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public Type getFieldType() {
        return fieldType;
    }

    ExpandableFieldConverter getConverter() throws InstantiationException, IllegalAccessException {
        return converterClass.newInstance();
    }

    /**
     * Class which use reflection to get the expandable fields of the Expandable classes. It stores
     * the data in order to abuse of reflection.
     */
    static class Cache {

        private static HashMap<Type, ArrayList<ExpandableFieldInfo>> cacheHashMap;

        private static ArrayList<ExpandableFieldInfo> getCachedInfo(Type t) {

            if (cacheHashMap == null) {
                cacheHashMap = new HashMap<>();
            }

            ArrayList<ExpandableFieldInfo> cachedValue = cacheHashMap.get(t);
            if (cachedValue != null) {
                return cachedValue;
            }

            return addToCache(t);
        }

        private static ArrayList<ExpandableFieldInfo> addToCache(Type type) {

            ArrayList<ExpandableFieldInfo> value = new ArrayList<>();
            cacheHashMap.put(type, value);

            // if not a class, return an empty array
            if (!(type instanceof Class)) {
                return value;
            }

            Class clazz = (Class) type;

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                boolean isExpandable = false;
                Class<? extends ExpandableFieldConverter> converterClass = DefaultExpandableFieldConverter.class;
                String serializedName = field.getName();

                Annotation[] annotations = field.getAnnotations();

                for (Annotation annotation : annotations) {

                    if (annotation instanceof SerializedName) {
                        serializedName = ((SerializedName) annotation).value();
                    } else if (annotation instanceof ExpandableField) {
                        isExpandable = true;
                        converterClass = ((ExpandableField) annotation).converterClass();
                    }
                }

                if (isExpandable) {
                    ExpandableFieldInfo info = new ExpandableFieldInfo(serializedName, field.getType(), converterClass);
                    value.add(info);
                }
            }

            return value;
        }
    }
}
