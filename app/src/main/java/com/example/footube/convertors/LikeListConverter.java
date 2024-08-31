package com.example.footube.convertors;

import androidx.room.TypeConverter;
import com.example.footube.BasicClasses.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class LikeListConverter {
    private static final Gson gson = new Gson();
    private static final Type listType = new TypeToken<List<String>>() {}.getType();

    @TypeConverter
    public static List<String> fromString(String value) {
        return gson.fromJson(value, listType);
    }

    @TypeConverter
        public static String fromList(List<String> list) {
        return gson.toJson(list);
    }
}
