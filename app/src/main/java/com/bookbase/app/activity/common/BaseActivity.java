package com.bookbase.app.activity.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    BasePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = attachPresenter();
    }

    protected abstract BasePresenter attachPresenter();

    protected void showSnackBar(String message) {
        if (this.getCurrentFocus() != null) {
            Snackbar.make(this.getCurrentFocus(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
