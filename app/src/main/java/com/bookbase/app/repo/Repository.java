package com.bookbase.app.repo;

import com.bookbase.app.App;
import com.bookbase.app.activity.addBook.AddEditBookCallback;
import com.bookbase.app.database.dao.AuthorDao;
import com.bookbase.app.database.dao.BookDao;
import com.bookbase.app.database.dao.GenreDao;
import com.bookbase.app.database.entity.Author;
import com.bookbase.app.database.entity.Book;
import com.bookbase.app.database.entity.Genre;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Repository {

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final ThreadPoolExecutor pool;

    public Repository() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        pool = new ThreadPoolExecutor(NUM_CORES, NUM_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, workQueue);

        bookDao = App.getInstance().getDatabase().bookDao();
        genreDao = App.getInstance().getDatabase().genreDao();
        authorDao = App.getInstance().getDatabase().authorDao();
    }

    public List<Book> getBookList() {
        return bookDao.getAll();
    }

    public Book getBook(final int bookId) {
        return bookDao.getById(bookId);
    }

    public void insertBook(final Book book, boolean isEdit, final AddEditBookCallback callback) {
        callback.inProgress();
        long success = isEdit ? bookDao.update(book) : bookDao.insert(book);
        if (success > 0) {
            callback.onSuccess();
        } else {
            callback.onFailure();
        }
    }

    public void deleteBook(final Book book) {
        bookDao.delete(book);
        ;
    }

    public List<String> getAuthorNames() {
        return authorDao.getAuthorNames();
    }

    public List<String> getGenreNames() {
        return genreDao.getNames();
    }

    public Author getAuthorByName(final String name) {
        Author author = authorDao.getByName(name);
        return author == null ? new Author(name) : author;
    }

    public Genre getGenreByName(String name) {
        Genre genre = genreDao.getByName(name);
        return genre == null ? new Genre(name) : genre;
    }
}
