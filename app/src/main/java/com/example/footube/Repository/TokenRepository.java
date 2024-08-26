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
        return "Bearer " + dao.getToken().getToken();
    }

    public void delete() {
        dao.deleteAllTokens();
    }
}

