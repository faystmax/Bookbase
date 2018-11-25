package com.bookbase.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bookbase.app.database.entity.Genre;

import java.util.List;

@Dao
public interface GenreDao {

    @Query("SELECT * FROM Genre")
    List<Genre> getAll();

    @Query("SELECT * FROM Genre where id=:id")
    Genre getById(int id);

    @Query("SELECT * FROM Genre where name=:name")
    Genre getByName(String name);

    @Query("SELECT name from Genre")
    List<String> getNames();

    @Insert
    long insert(Genre genre);
}

