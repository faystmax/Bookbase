package com.bookbase.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bookbase.app.database.entity.Review;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM Review WHERE id = :id")
    Review getById(int id);

    @Insert
    long insert(Review review);
}
