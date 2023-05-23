package com.fineelyframework.config.core.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fineelyframework.config.core.entity.ConfigSupport;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static String toJsonString(Object configValue) {
        String value;
        if (Objects.isNull(configValue)) {
            value = null;
        } else if (configValue instanceof List) {
            value = com.alibaba.fastjson2.JSONArray.toJSONString(configValue);
        } else if (configValue instanceof Map) {
            value = com.alibaba.fastjson2.JSONObject.toJSONString(configValue);
        } else if (configValue instanceof Set) {
            value = com.alibaba.fastjson2.JSONObject.toJSONString(configValue);
        } else if (TypeJudgmentUtil.isBasicType(configValue.getClass())) {
            value = configValue.toString();
        } else {
            value = com.alibaba.fastjson2.JSONObject.from(configValue).toString();
        }
        return value;
    }

    public static boolean isBasicType(Class typeClass) {
        return typeClass == int.class || typeClass == short.class || typeClass == byte.class ||
                typeClass == float.class || typeClass == double.class || typeClass == long.class ||
                typeClass == String.class || typeClass == boolean.class || typeClass.isEnum() ||
                typeClass == LocalDateTime.class || typeClass == LocalDate.class || typeClass == LocalTime.class ||
                typeClass == Integer.class || typeClass == Boolean.class || typeClass == Long.class ||
                typeClass == Double.class || typeClass == Float.class || typeClass == Byte.class ||
                typeClass == Short.class;

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
            } else if (typeClass == char.class) {
                if (configValue != null) {
                    field.setChar(configSupport, configValue.charAt(0));
                } else {
                    field.set(configSupport, null);
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
            } else if (typeClass == LocalTime.class) {
                if (configValue != null) {
                    field.set(configSupport, LocalTime.parse(configValue, DateTimeFormatter.ofPattern("HH:mm:ss")));
                } else {
                    field.set(configSupport, null);
                }
            } else if (typeClass == List.class) {
                if (configValue != null && !"[]".equals(configValue)) {
                    field.set(configSupport, JSONArray.parseArray(configValue, Object.class));
                } else {
                    field.set(configSupport, new ArrayList<>());
                }
            } else if (typeClass == Map.class) {
                if (configValue != null) {
                    field.set(configSupport, com.alibaba.fastjson2.JSONObject.parseObject(configValue, Map.class));
                } else {
                    field.set(configSupport, new HashMap<>());
                }
            } else if (!isBasicType(typeClass)) {
                field.set(configSupport, null);
//                if (configValue != null) {
//                    field.set(configSupport, JSON.parseObject(configValue,typeClass));
//                } else {
//                    field.set(configSupport, null);
//                }
            } else if (typeClass == Set.class) {
                if (configValue != null) {
                    field.set(configSupport, JSONObject.parseObject(configValue, Set.class));
                } else {
                    field.set(configSupport, new HashMap<>());
                }
            } else {
                field.set(configSupport, configValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
