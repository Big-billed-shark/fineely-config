package com.fineelyframework.config.core.utils;

import com.fineelyframework.config.core.entity.ConfigSupport;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TypeJudgmentUtil {

    public static Object[] getConfigCodes(Field[] fields) {
        String[] configCodes = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            configCodes[i] = fields[i].getName();
        }
        return configCodes;
    }

    public static <T extends ConfigSupport> Object get(T configSupport, Field field) {
        Object configValue = null;
        try {
            configValue = field.get(configSupport);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return configValue;
    }

    public static <T extends ConfigSupport> void set(T configSupport, Field field, String configValue) {
        try {
            Class typeClass = field.getType();
            if (typeClass == int.class) {
                if (configValue != null) {
                    field.setInt(configSupport, Integer.parseInt(configValue));
                } else {
                    field.set(configSupport, 0);
                }
            } else if (typeClass == short.class) {
                if (configValue != null) {
                    field.setShort(configSupport, Short.parseShort(configValue));
                } else {
                    field.set(configSupport, 0);
                }
            } else if (typeClass == byte.class) {
                if (configValue != null) {
                    field.setByte(configSupport, Byte.parseByte(configValue));
                } else {
                    field.set(configSupport, 0);
                }
            } else if (typeClass == float.class) {
                if (configValue != null) {
                    field.setFloat(configSupport, Float.parseFloat(configValue));
                } else {
                    field.set(configSupport, 0);
                }
            } else if (typeClass == double.class) {
                if (configValue != null) {
                    field.setDouble(configSupport, Double.parseDouble(configValue));
                } else {
                    field.set(configSupport, 0);
                }
            } else if (typeClass == long.class) {
                if (configValue != null) {
                    field.setLong(configSupport, Long.parseLong(configValue));
                } else {
                    field.set(configSupport, 0);
                }
            } else if (typeClass == String.class) {
                field.set(configSupport, configValue);
            } else if (typeClass == boolean.class) {
                if (configValue != null) {
                    field.setBoolean(configSupport, Boolean.parseBoolean(configValue));
                } else {
                    field.set(configSupport, false);
                }
            } else if (typeClass.isEnum()) {
                if (configValue != null) {
                    field.set(configSupport, Enum.valueOf(typeClass, configValue));
                } else {
                    field.set(configSupport, null);
                }
            } else if (typeClass == LocalDateTime.class) {
                if (configValue != null) {
                    field.set(configSupport, LocalDateTime.parse(configValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                } else {
                    field.set(configSupport, null);
                }
            } else if (typeClass == LocalDate.class) {
                if (configValue != null) {
                    field.set(configSupport, LocalDate.parse(configValue, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else {
                    field.set(configSupport, null);
                }
            } else if (typeClass == Object.class) {
                // todo
            } else {
                field.set(configSupport, configValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
