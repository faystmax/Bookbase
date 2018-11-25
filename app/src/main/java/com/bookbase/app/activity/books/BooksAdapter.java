package com.bookbase.app.activity.books;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bookbase.app.App;
import com.bookbase.app.R;
import com.bookbase.app.database.AppDatabase;
import com.bookbase.app.database.entity.Book;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private final Context context;
    private final AppDatabase db = App.getInstance().getDatabase();
    private final SortedList<Book> sortedList = new SortedList<>(Book.class, new SortedList.Callback<Book>() {

        @Override
        public int compare(Book o1, Book o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Book oldItem, Book newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Book item1, Book item2) {
            return item1.getId() == item2.getId();
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    public BooksAdapter(Context context) {
        this.context = context;
    }

    public void add(List<Book> books) {
        sortedList.clear();
        sortedList.addAll(books);
    }

    public void replaceAll(List<Book> books) {
        sortedList.beginBatchedUpdates();
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            final Book model = sortedList.get(i);
            if (!books.contains(model)) {
                sortedList.remove(model);
            }
        }
        sortedList.addAll(books);
        sortedList.endBatchedUpdates();
    }


    @Override
    @NonNull
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bookView = inflater.inflate(R.layout.item_book, parent, false);
        return new ViewHolder(bookView);
    }

    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder viewHolder, int position) {

        Book book = sortedList.get(position);

        viewHolder.title.setText(book.getTitle());
        viewHolder.rating.setRating(book.getRating());
        viewHolder.author.setText(db.authorDao().getById(book.getAuthor().getId()).getName());

        if (book.getCoverImage() != null) {
            Picasso.with(context)
                    .load(new File(book.getCoverImage()))
                    .placeholder(R.mipmap.no_cover)
                    .error(R.mipmap.no_cover)
                    .into(viewHolder.coverImage);
        } else {
            viewHolder.coverImage.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public int getBookIdAt(int position) {
        return sortedList.get(position).getId();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_list_title) TextView title;
        @BindView(R.id.book_list_author) TextView author;
        @BindView(R.id.book_list_rating) RatingBar rating;
        @BindView(R.id.book_list_image) ImageView coverImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
