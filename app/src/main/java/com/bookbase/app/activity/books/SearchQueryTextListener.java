package com.bookbase.app.activity.books;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.bookbase.app.database.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchQueryTextListener implements SearchView.OnQueryTextListener {


    private List<Book> books;
    private BooksAdapter adapter;
    private RecyclerView bookRecyclerView;

    SearchQueryTextListener(List<Book> books, BooksAdapter adapter, RecyclerView bookRecyclerView) {
        this.books = books;
        this.adapter = adapter;
        this.bookRecyclerView = bookRecyclerView;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Book> filteredModelList = filter(books, newText);
        adapter.replaceAll(filteredModelList);
        bookRecyclerView.scrollToPosition(0);
        return true;
    }

    private static List<Book> filter(List<Book> books, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Book> filteredBookList = new ArrayList<>();
        for (Book book : books) {
            final String text = book.getTitle().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredBookList.add(book);
            }
        }
        return filteredBookList;
    }

}
