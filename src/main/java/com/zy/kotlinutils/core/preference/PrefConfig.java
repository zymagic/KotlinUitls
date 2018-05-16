package com.zy.kotlinutils.core.preference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zy on 17-12-6.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface PrefConfig {
    String value();
}
