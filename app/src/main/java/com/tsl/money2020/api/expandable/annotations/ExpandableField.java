package com.tsl.money2020.api.expandable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Manu on 6/1/17.
 */

/**
 * Annotations which must be added to the fields that can be marked as Expandable in the api requests.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExpandableField {
    Class<? extends ExpandableFieldConverter> converterClass() default DefaultExpandableFieldConverter.class;
}
