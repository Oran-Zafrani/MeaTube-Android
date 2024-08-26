package com.example.footube.convertors;

import androidx.room.TypeConverter;

import com.example.footube.BasicClasses.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CommentListConverter {
    private static final Gson gson = new Gson();
    private static final Type listType = new TypeToken<List<Comment>>() {}.getType();

    @TypeConverter
    public static List<Comment> fromString(String value) {
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Comment> list) {
        return gson.toJson(list);
    }
}

