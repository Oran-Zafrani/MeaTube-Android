package com.example.footube.localDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.footube.BasicClasses.User;
import com.example.footube.convertors.MovieListConverter;
import com.example.footube.dao.UserDao;

@Database(entities = {User.class}, version = 3)
@TypeConverters({MovieListConverter.class})
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
}
