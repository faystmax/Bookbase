package com.bookbase.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.bookbase.app.database.AppDatabase;
import com.bookbase.app.repo.Repository;

import lombok.Getter;

public class App extends Application {

    @Getter public static App instance;

    @Getter private AppDatabase database;
    @Getter private Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "bookbase-db")
                .allowMainThreadQueries().build();
        repository = new Repository();
    }

}