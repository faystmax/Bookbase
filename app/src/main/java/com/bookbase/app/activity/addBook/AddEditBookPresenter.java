package com.bookbase.app.activity.addBook;

import android.graphics.Bitmap;

import com.bookbase.app.activity.common.BasePresenter;
import com.bookbase.app.database.entity.Book;
import com.bookbase.app.database.entity.Review;
import com.bookbase.app.utils.SaveImageHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Double.parseDouble;

@Slf4j
class AddEditBookPresenter extends BasePresenter {

    private boolean isEdit;
    private AddEditBookViewInterface view;
    private SaveImageHelper imageHelper = new SaveImageHelper();

    AddEditBookPresenter(AddEditBookViewInterface view) {
        super();
        this.view = view;
    }

    public void start(int bookId, boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            view.initEdit(repository.getBook(bookId));
        } else {
            view.initAdd(new Book());
        }
    }

    void saveBook(int id, String title, String author, String genre, String review, String purchaseDate, String price, float rating, Bitmap cover) {

        if (validateMandatoryFields(title, author, genre)) {

            Book book = null;
            if (isEdit) {
                book = repository.getBook(id);
                view.onBookSave();
            } else {
                book = new Book();
            }

            book.setTitle(title);
            book.setRating(rating);
            book.setPurchaseDate(parseDate(purchaseDate));
            book.setReview(new Review(new Date(), review));
            book.setGenre(repository.getGenreByName(genre));
            book.setAuthor(repository.getAuthorByName(author));
            book.setPurchasePrice(price.isEmpty() ? 0 : parseDouble(price));
            if (cover != null) {
                book.setCoverImage(imageHelper.saveImageToInternalStorage(cover, book));
            }

            repository.insertBook(book, isEdit, new AddEditBookCallback() {
                @Override
                public void inProgress() {
                }

                @Override
                public void onSuccess() {
                    view.onBookSave();
                }

                @Override
                public void onFailure() {
                    IOException e = new IOException("Unable to save book.");
                    view.onBookSaveError(e);
                }
            });
        }
    }


    private boolean validateMandatoryFields(String title, String author, String genre) {
        if (StringUtils.isEmpty(title)) {
            view.titleInvalid();
            return false;
        }
        if (StringUtils.isEmpty(author)) {
            view.authorInvalid();
            return false;
        }
        if (StringUtils.isEmpty(genre)) {
            view.genreInvalid();
            return false;
        }
        return true;
    }

    private Date parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            log.error("Parse date exception: date = " + date, e);
        }
        return null;
    }

    void setupAutoCompleteFields() {
        view.setupAuthorAutoFill(repository.getAuthorNames());
        view.setupGenreAutoFill(repository.getGenreNames());
    }
}
