package com.bookbase.app.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(tableName = "Book")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Author author;
    private Genre genre;
    private String coverImage;
    private float rating;
    private Review review;
    private Date purchaseDate;
    private double purchasePrice;

    public String getPurchaseDateString() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        if (purchaseDate != null) {
            return df.format(purchaseDate.getTime());
        }
        return "";
    }

}
