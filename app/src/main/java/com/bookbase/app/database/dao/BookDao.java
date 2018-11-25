package com.bookbase.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bookbase.app.database.entity.Book;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    long insert(Book book);

    @Query("SELECT * FROM Book")
    List<Book> getAll();

    @Query("SELECT * FROM Book where id = :id")
    Book getById(int id);

    @Update
    int update(Book book);

    @Delete
    void delete(Book book);

}
