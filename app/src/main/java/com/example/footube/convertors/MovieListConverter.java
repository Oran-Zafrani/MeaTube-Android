package com.example.footube.convertors;

import androidx.room.TypeConverter;

import com.example.footube.BasicClasses.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MovieListConverter {
    private static final Gson gson = new Gson();
    private static final Type listType = new TypeToken<List<Movie>>() {}.getType();

    @TypeConverter
    public static List<Movie> fromString(String value) {
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Movie> list) {
        return gson.toJson(list);
    }
}

