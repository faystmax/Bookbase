package com.bookbase.app.activity.addBook;

public interface AddEditBookCallback {
    void inProgress();

    void onSuccess();

    void onFailure();
}