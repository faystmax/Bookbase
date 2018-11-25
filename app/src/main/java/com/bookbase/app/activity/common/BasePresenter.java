package com.bookbase.app.activity.common;

import com.bookbase.app.App;
import com.bookbase.app.repo.Repository;

public abstract class BasePresenter {
    protected Repository repository;

    public BasePresenter() {
        this.repository = App.getInstance().getRepository();
    }
}
