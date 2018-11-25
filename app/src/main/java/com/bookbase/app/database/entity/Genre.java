package com.bookbase.app.database.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(tableName = "Genre", indices = {@Index(value = "name", unique = true)})
public class Genre {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull private String name;

    @Ignore
    public Genre(@NonNull String name) {
        this.name = name;
    }
}
