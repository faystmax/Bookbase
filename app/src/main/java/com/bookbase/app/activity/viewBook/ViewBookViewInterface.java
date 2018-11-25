package com.bookbase.app.activity.viewBook;

import com.bookbase.app.database.entity.Book;

public interface ViewBookViewInterface {

    void closeScreen();

    void populateDetails(Book book);

    void editBook(Book book);
}
