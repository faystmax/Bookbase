package com.bookbase.app.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(tableName = "Review")
public class Review {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date reviewDate;
    private String content;

    @Ignore
    public Review(Date reviewDate, String content) {
        this.reviewDate = reviewDate;
        this.content = content;
    }

}
