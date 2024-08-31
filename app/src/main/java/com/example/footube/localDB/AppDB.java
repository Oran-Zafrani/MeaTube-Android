package com.example.footube.localDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.Token;
import com.example.footube.BasicClasses.User;
import com.example.footube.convertors.CommentListConverter;
import com.example.footube.convertors.LikeListConverter;
import com.example.footube.convertors.MovieListConverter;
import com.example.footube.convertors.TimeConvertor;
import com.example.footube.dao.MovieDao;
import com.example.footube.dao.TokenDao;
import com.example.footube.dao.UserDao;

@Database(entities = {User.class, Token.class, Movie.class}, version = 20)
@TypeConverters({MovieListConverter.class, TimeConvertor.class, CommentListConverter.class})
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TokenDao tokenDao();
    public abstract MovieDao movieDao();
}
