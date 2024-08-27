package com.example.footube.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.footube.BasicClasses.Movie;
import com.example.footube.BasicClasses.User;

import java.util.List;

@Dao
public interface MovieDao {
        @Query("SELECT * FROM movie")
        List<Movie> index();

        @Query("SELECT * FROM movie WHERE id = :id")
        Movie get(String id);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Movie... movies);
        @Update
        void update(Movie... movies);

        @Delete
        void delete(Movie... movies);

        @Query("DELETE FROM movie")
        void clear();

        @Query("DELETE FROM Movie")
        void deleteAllMovies();
}
