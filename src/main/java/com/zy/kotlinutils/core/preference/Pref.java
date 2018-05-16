package com.zy.kotlinutils.core.preference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zy on 17-11-6.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Pref {
    String key();
    Type type();

    enum Type {
        INTEGER,
        FLOAT,
        LONG,
        BOOLEAN,
        STRING,
    }

}