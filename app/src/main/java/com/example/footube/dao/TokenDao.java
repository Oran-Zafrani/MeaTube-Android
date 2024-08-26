package com.example.footube.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.footube.BasicClasses.Token;

@Dao
public interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToken(Token token);

    @Query("SELECT * FROM Token LIMIT 1")
    Token getToken();

    @Query("DELETE FROM Token")
    void deleteAllTokens();

}

