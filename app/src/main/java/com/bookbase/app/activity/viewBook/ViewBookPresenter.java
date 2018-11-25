package com.bookbase.app.activity.viewBook;

import com.bookbase.app.activity.common.BasePresenter;
import com.bookbase.app.database.entity.Book;

public class ViewBookPresenter extends BasePresenter {

    private Book book;
    private ViewBookViewInterface view;

    ViewBookPresenter(ViewBookViewInterface view) {
        this.view = view;
    }

    void deleteBook() {
        repository.deleteBook(book);
        view.closeScreen();
    }

    void updateBook() {
        book = repository.getBook(book.getId());
        view.populateDetails(book);
    }

    public void setBook(int bookId) {
        this.book = repository.getBook(bookId);
    }

    void startEditActivity() {
        view.editBook(book);
    }
}
