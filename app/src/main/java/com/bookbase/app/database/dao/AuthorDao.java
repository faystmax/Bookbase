package com.bookbase.app.database.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bookbase.app.database.entity.Author;

import java.util.List;

@Dao
public interface AuthorDao {

    @Insert
    long insert(Author author);

    @Query("SELECT * FROM Author")
    List<Author> getAll();

    @Query("SELECT * FROM Author WHERE id=:id")
    Author getById(int id);

    @Query("SELECT * FROM Author WHERE name=:name")
    Author getByName(String name);

    @Query("SELECT name FROM Author")
    List<String> getAuthorNames();
}
