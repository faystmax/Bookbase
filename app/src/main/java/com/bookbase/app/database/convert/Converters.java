package com.bookbase.app.database.convert;

import android.arch.persistence.room.TypeConverter;

import com.bookbase.app.App;
import com.bookbase.app.database.AppDatabase;
import com.bookbase.app.database.entity.Author;
import com.bookbase.app.database.entity.Genre;
import com.bookbase.app.database.entity.Review;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Converters {

    @TypeConverter
    public static Date toDate(String date) {
        try {
            return DateUtils.parseDate(date, "dd.MM.yyyy");
        } catch (ParseException e) {
            log.error("Date parse exception", e);
        }
        return null;
    }

    @TypeConverter
    public static String dateToString(Date date) {
        if (date == null) {
            date = new Date();
        }
        final DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        return df.format(date);
    }

    @TypeConverter
    public static int authorAsId(Author author) {
        AppDatabase db = App.getInstance().getDatabase();
        if (db.authorDao().getById(author.getId()) != null) {
            return author.getId();
        }
        return (int) db.authorDao().insert(author);
    }

    @TypeConverter
    public static Author intToAuthor(int id) {
        AppDatabase db = App.getInstance().getDatabase();
        return db.authorDao().getById(id);
    }

    @TypeConverter
    public static int genreAsId(Genre genre) {
        AppDatabase db = App.getInstance().getDatabase();
        if (db.genreDao().getById(genre.getId()) != null) {
            return genre.getId();
        }
        return (int) db.genreDao().insert(genre);
    }

    @TypeConverter
    public static Genre intToGenre(int id) {
        AppDatabase db = App.getInstance().getDatabase();
        return db.genreDao().getById(id);
    }

    @TypeConverter
    public static int reviewAsId(Review review) {
        AppDatabase db = App.getInstance().getDatabase();
        return (int) db.reviewDao().insert(review);
    }

    @TypeConverter
    public static Review intToReview(int id) {
        AppDatabase db = App.getInstance().getDatabase();
        return db.reviewDao().getById(id);
    }

}
