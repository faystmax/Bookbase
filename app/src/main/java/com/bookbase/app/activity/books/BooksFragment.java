package com.bookbase.app.activity.books;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookbase.app.App;
import com.bookbase.app.R;
import com.bookbase.app.activity.addBook.AddEditBookActivity;
import com.bookbase.app.activity.viewBook.ViewBookFragment;
import com.bookbase.app.database.entity.Book;
import com.bookbase.app.repo.Repository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BooksFragment extends Fragment {

    private List<Book> books;
    private TextView emptyView;
    private BooksAdapter bookAdapter;
    private RecyclerView bookRecyclerView;
    private Repository repository = App.getInstance().getRepository();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookAdapter = new BooksAdapter(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        showBooks();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.library_menu, menu);
        final TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_title);

        MenuItem searchMenuItem = menu.findItem(R.id.searchButton);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchQueryTextListener(books, bookAdapter, bookRecyclerView));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarTitle.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                toolbarTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchButton:
                if (getActivity() != null) {
                    TransitionManager.beginDelayedTransition((ViewGroup) getActivity().findViewById(R.id.toolbar));
                }
                item.expandActionView();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        emptyView = view.findViewById(R.id.empty_view);
        bookRecyclerView = view.findViewById(R.id.books_list);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookRecyclerView.setAdapter(bookAdapter);
        showBooks();

        FloatingActionButton fab = view.findViewById(R.id.add_book_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditBookActivity.class);
                startActivity(intent);
            }
        });

        bookRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), bookRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    Fragment fragment = (ViewBookFragment.class).newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", ((BooksAdapter) bookRecyclerView.getAdapter()).getBookIdAt(position));
                    fragment.setArguments(bundle);
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                .replace(R.id.content_frame, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Snackbar.make(view, "Long touch", Snackbar.LENGTH_SHORT).show();
            }
        }));

        return view;
    }

    private void showBooks() {
        books = repository.getBookList();
        bookAdapter.add(books);
        if (books.isEmpty()) {
            bookRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.getRootView().setBackgroundColor(Color.WHITE);
            Log.i("bookFragment", "hide " + books.size());
        } else {
            emptyView.setVisibility(View.GONE);
            bookRecyclerView.setVisibility(View.VISIBLE);
            bookAdapter.notifyItemRangeInserted(0, books.size());
            bookAdapter.notifyItemRangeChanged(0, books.size());
            bookAdapter.notifyDataSetChanged();
            Log.i("bookFragment", "show " + books.size());
        }
    }

}
