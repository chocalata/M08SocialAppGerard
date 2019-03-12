package com.example.gerard.socialapp.converter;


import com.example.gerard.socialapp.model.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ConverterPostToJSon {
    static Gson gson = new Gson();

    public static Post stringToSomeObject(String data) {
        if (data == null) {
            return new Post();
        }

        Type listType = new TypeToken<Post>() {}.getType();

        return gson.fromJson(data, listType);
    }

    public static String someObjectToString(Post someObjects) {
        return gson.toJson(someObjects);
    }
}
