package com.bookbase.app.activity.viewBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bookbase.app.R;
import com.bookbase.app.activity.addBook.AddEditBookActivity;
import com.bookbase.app.database.entity.Book;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewBookFragment extends Fragment implements ViewBookViewInterface {

    private final ViewBookPresenter presenter;
    @BindView(R.id.view_book_title) TextView title;
    @BindView(R.id.view_book_author) TextView author;
    @BindView(R.id.view_book_rating) RatingBar rating;
    @BindView(R.id.view_book_cover) ImageView cover;
    @BindView(R.id.view_book_genre) TextView genre;
    @BindView(R.id.view_book_lbl_review) TextView reviewLabel;
    @BindView(R.id.view_book_review) TextView review;
    private AppCompatActivity activity;

    public ViewBookFragment() {
        presenter = new ViewBookPresenter(this);
    }

    public static ViewBookFragment newInstance(Bundle bundle) {
        ViewBookFragment fragment = new ViewBookFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            presenter.setBook(bundle.getInt("id"));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ((AppCompatActivity) getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_book_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View menuActionItem = null;
        if (getActivity() != null) {
            menuActionItem = (getActivity()).findViewById(R.id.view_book_options);
        }
        PopupMenu popupMenu = new PopupMenu(getActivity(), menuActionItem);
        popupMenu.getMenuInflater().inflate(R.menu.view_book_action_items, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch ((String) item.getTitle()) {
                    case "Изменить":
                        presenter.startEditActivity();
                        break;
                    case "Удалить":
                        presenter.deleteBook();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
        return false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_book, container, false);
        ButterKnife.bind(this, view);

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setHasOptionsMenu(true);
        presenter.updateBook();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.updateBook();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void populateDetails(Book book) {
        if (book.getReview().getContent().isEmpty()) {
            reviewLabel.setVisibility(View.GONE);
            review.setVisibility(View.GONE);
        }

        title.setText(book.getTitle());
        author.setText(book.getAuthor().getName());
        rating.setRating(book.getRating());
        genre.setText(book.getGenre().getName());
        review.setText(book.getReview().getContent());

        if (book.getCoverImage() != null) {
            Picasso.with(getActivity())
                    .load(new File(book.getCoverImage()))
                    .placeholder(R.mipmap.no_cover)
                    .error(R.mipmap.no_cover)
                    .into(cover);
        } else {
            Picasso.with(getActivity())
                    .load(R.mipmap.no_cover)
                    .into(cover);
        }
    }

    @Override
    public void closeScreen() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void editBook(Book book) {
        Intent intent = new Intent(getActivity(), AddEditBookActivity.class);
        intent.putExtra("BookId", book.getId());
        intent.putExtra("isEdit", true);
        startActivity(intent);
    }
}
