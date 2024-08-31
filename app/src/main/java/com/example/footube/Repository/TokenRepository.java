package com.example.footube.Repository;

import androidx.room.Room;

import com.example.footube.MyApplication;
import com.example.footube.dao.TokenDao;
import com.example.footube.localDB.AppDB;
import com.example.footube.BasicClasses.Token;

public class TokenRepository {
    private TokenDao dao;

    public TokenRepository() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "tokenDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        this.dao = db.tokenDao();
    }

    public void add(Token token) {
        dao.insertToken(token);
    }

    public String get() {
        Token token = dao.getToken(); // Retrieve the token from the database
        if (token == null) {
            return null;
//            throw new IllegalStateException("Token is not available in the database.");
        }
        return "Bearer " + token.getToken();
    }


    public void delete() {
        dao.deleteAllTokens();
    }
}

