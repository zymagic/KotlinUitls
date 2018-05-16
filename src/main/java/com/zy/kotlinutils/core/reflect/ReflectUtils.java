package com.zy.kotlinutils.core.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zy on 17-11-5.
 */

public class ReflectUtils {

    public static <T> T getFieldValue(Object instance, String fieldName) {
        if (instance == null) {
            return null;
        }
        try {
            return (T) getField(instance.getClass(), fieldName).get(instance);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> void setFieldValue(Object instance, String fieldName, T value) {
        try {
            Field field = getField(instance.getClass(), fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            // ignore
        }
    }

    private static Field getField(Class<?> cls, String fieldName) throws NoSuchFieldException {
        Field field = cls.getDeclaredField(fieldName);
        if (field == null) {
            Class<?> superClass = cls.getSuperclass();
            if (superClass == null) {
                throw new NoSuchFieldException();
            }
            return getField(superClass, fieldName);
        }
        return field;
    }

    public static <T> T invokeMethod(Object instance, String methodName, Object ... args) {
        if (instance == null || methodName == null) {
            return null;
        }
        return null;
    }

    private static Method getMethod(Object instance, String methodName, Object ... args) throws NoSuchMethodException {

        return null;
    }
}
