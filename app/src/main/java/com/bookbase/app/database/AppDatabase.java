package com.bookbase.app.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.bookbase.app.database.convert.Converters;
import com.bookbase.app.database.dao.AuthorDao;
import com.bookbase.app.database.dao.BookDao;
import com.bookbase.app.database.dao.GenreDao;
import com.bookbase.app.database.dao.ReviewDao;
import com.bookbase.app.database.entity.Author;
import com.bookbase.app.database.entity.Book;
import com.bookbase.app.database.entity.Genre;
import com.bookbase.app.database.entity.Review;

@Database(entities = {Book.class, Author.class, Genre.class, Review.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookDao bookDao();

    public abstract AuthorDao authorDao();

    public abstract GenreDao genreDao();

    public abstract ReviewDao reviewDao();

}
