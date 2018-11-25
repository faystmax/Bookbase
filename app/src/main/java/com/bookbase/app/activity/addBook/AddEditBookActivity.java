package com.bookbase.app.activity.addBook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bookbase.app.R;
import com.bookbase.app.activity.common.BaseActivity;
import com.bookbase.app.activity.common.BasePresenter;
import com.bookbase.app.database.entity.Book;
import com.bookbase.app.database.entity.Genre;
import com.bookbase.app.database.entity.Review;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddEditBookActivity extends BaseActivity implements AddEditBookViewInterface, View.OnClickListener {

    private static final int COVER_IMAGE_REQUEST = 1;

    @BindView(R.id.add_book_title_field) TextInputLayout titleField;
    @BindView(R.id.add_book_genre_field) TextInputLayout genreField;
    @BindView(R.id.add_book_author_field) TextInputLayout authorField;
    @BindView(R.id.add_book_review_field) TextInputLayout reviewField;
    @BindView(R.id.add_book_purchase_dt_field) TextInputLayout purchaseDateField;
    @BindView(R.id.add_book_purchase_price_field) TextInputLayout purchasePriceField;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.no_image) TextView lblNoImage;
    @BindView(R.id.cover_image) ImageView coverImage;
    @BindView(R.id.add_book_rating_data) RatingBar rating;
    @BindView(R.id.camera_fab) FloatingActionButton cameraFab;
    @BindView(R.id.add_book_title_data) EditText titleEditText;
    @BindView(R.id.add_book_review_data) EditText reviewEditText;
    @BindView(R.id.add_book_purchase_date_data) EditText purchaseDateEditText;
    @BindView(R.id.add_book_genre_data) AutoCompleteTextView genreAutoComplete;
    @BindView(R.id.add_book_purchase_price_data) EditText purchasePriceEditText;
    @BindView(R.id.add_book_author_data) AutoCompleteTextView authorAuthorAutoComplete;

    private Book book;
    private Bitmap imageToStore;
    private AddEditBookPresenter presenter;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.fragment_edit_book);
        ButterKnife.bind(this);

        presenter = new AddEditBookPresenter(this);
        presenter.setupAutoCompleteFields();

        int bookId = getIntent().getIntExtra("BookId", 0);
        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        presenter.start(bookId, isEdit);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        cameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeBookImage();
            }
        });
        setDateTimeField();
    }

    private void setDateTimeField() {
        purchaseDateEditText.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                purchaseDateEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        if (view == purchaseDateEditText || view == purchaseDateField) {
            datePickerDialog.show();
        }
    }


    @Override
    public void initEdit(Book book) {
        toolbarTitle.setText(R.string.title_activity_edit_book);
        this.book = book;
        fillData();
    }

    @Override
    public void initAdd(Book book) {
        toolbarTitle.setText(R.string.title_activity_add_book);
        this.book = book;
    }

    private void fillData() {
        final Context context = this;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                titleEditText.setText(book.getTitle());
                authorAuthorAutoComplete.setText(book.getAuthor().getName());
                Genre genreEntity = book.getGenre();
                if (genreEntity != null) {
                    genreAutoComplete.setText(genreEntity.getName());
                } else {
                    genreAutoComplete.setText(R.string.lbl_unknown_genre);
                }

                if (book.getCoverImage() != null) {
                    lblNoImage.setVisibility(View.GONE);
                    coverImage.setVisibility(View.VISIBLE);
                    File file = new File(book.getCoverImage());
                    Picasso.with(context)
                            .load(file)
                            .placeholder(R.mipmap.no_cover)
                            .error(R.mipmap.no_cover)
                            .into(coverImage);
                }

                Review reviewContent = book.getReview();
                if (reviewContent != null) {
                    reviewEditText.setText(reviewContent.getContent());
                }
                purchaseDateEditText.setText(book.getPurchaseDateString());
                purchasePriceEditText.setText(String.valueOf(book.getPurchasePrice()));
                rating.setRating(book.getRating());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_book_menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                String title = titleEditText.getText().toString();
                String author = authorAuthorAutoComplete.getText().toString();
                String genre = genreAutoComplete.getText().toString();
                String review = reviewEditText.getText().toString();
                String purchaseDate = purchaseDateEditText.getText().toString();
                String price = purchasePriceEditText.getText().toString();
                float rating = this.rating.getRating();

                presenter.saveBook(book.getId(), title, author, genre, review, purchaseDate, price, rating, imageToStore);
                return true;
        }
        return false;
    }

    private void takeBookImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, COVER_IMAGE_REQUEST);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COVER_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap image = null;
            if (extras != null) {
                image = (Bitmap) extras.get("data");
            }
            lblNoImage.setVisibility(View.GONE);
            coverImage.setVisibility(View.VISIBLE);
            if (image != null) {
                coverImage.setImageBitmap(image);
            }
            imageToStore = image;
        }
    }

    @Override
    public void onBookSave() {
        finish();
    }

    @Override
    public void onBookSaveError(Throwable error) {
        log.error("Ошибка сохранения книги", error);
        showSnackBar("Не удалось сохранить книгу");
        finish();
    }

    @Override
    public void setupAuthorAutoFill(final List<String> authorNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, authorNames);
        authorAuthorAutoComplete.setAdapter(adapter);
    }

    @Override
    public void setupGenreAutoFill(final List<String> genreNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genreNames);
        genreAutoComplete.setAdapter(adapter);
    }

    @Override
    protected BasePresenter attachPresenter() {
        return new AddEditBookPresenter(this);
    }

    @Override
    public void titleInvalid() {
        titleField.setError("");
        showSnackBar("Необходимо заполнить название");
    }

    @Override
    public void authorInvalid() {
        authorField.setError("");
        showSnackBar("Необходимо выбрать автора");
    }

    @Override
    public void genreInvalid() {
        genreField.setError("");
        showSnackBar("Необходимо выбрать жанр");
    }

}
