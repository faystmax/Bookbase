package com.bookbase.app.activity.addBook;

import com.bookbase.app.database.entity.Book;

import java.util.List;

interface AddEditBookViewInterface {

    void initEdit(Book book);

    void initAdd(Book book);

    void onBookSave();

    void onBookSaveError(Throwable error);

    void titleInvalid();

    void authorInvalid();

    void genreInvalid();

    void setupAuthorAutoFill(List<String> authorNames);

    void setupGenreAutoFill(List<String> genreNames);
}
