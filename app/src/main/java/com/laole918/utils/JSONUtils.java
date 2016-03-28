package com.laole918.utils;

import com.alibaba.fastjson.JSON;

/**
 * Created by laole918 on 2016/3/28 0028.
 */
public class JSONUtils {
    public static String toJSONString(Object object) {
        if (object != null) {
            return JSON.toJSONString(object);
        }
        return "{}";
    }
}
