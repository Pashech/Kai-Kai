package com.example.test_app_kai.utils;

import com.google.gson.Gson;

public class LazyJson {

    private static final Gson gson = new Gson();
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
