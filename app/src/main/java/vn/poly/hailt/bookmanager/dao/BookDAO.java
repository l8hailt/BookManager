package vn.poly.hailt.bookmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.database.DatabaseHelper;
import vn.poly.hailt.bookmanager.model.Book;
import vn.poly.hailt.bookmanager.model.BookIDItem;

public class BookDAO implements Constant {

    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public BookDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertBook(Book book) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BK_COLUMN_BOOK_ID, book.book_id);
        values.put(BK_COLUMN_CATEGORY_ID, book.category_id);
        values.put(BK_COLUMN_BOOK_NAME, book.book_name);
        values.put(BK_COLUMN_AUTHOR, book.author);
        values.put(BK_COLUMN_PUBLISHER, book.publisher);
        values.put(BK_COLUMN_PRICE, book.price);
        values.put(BK_COLUMN_QUANTITY, book.quantity);

        long id = db.insert(BOOK_TABLE, null, values);

        if (isDEBUG) Log.e("insertBook", "insertBook ID: " + id);

        db.close();
    }

    public void deleteBook(Book book) {
        db = dbHelper.getWritableDatabase();

        long id = db.delete(BOOK_TABLE,
                BK_COLUMN_BOOK_ID + " = ?",
                new String[]{book.book_id});

        if (isDEBUG) Log.e("deleteBook", "deleteBook ID: " + id);

        db.close();
    }

    public void updateBook(Book book) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BK_COLUMN_CATEGORY_ID, book.category_id);
        values.put(BK_COLUMN_BOOK_NAME, book.book_name);
        values.put(BK_COLUMN_AUTHOR, book.author);
        values.put(BK_COLUMN_PUBLISHER, book.publisher);
        values.put(BK_COLUMN_PRICE, book.price);
        values.put(BK_COLUMN_QUANTITY, book.quantity);

        long id = db.update(BOOK_TABLE, values,
                BK_COLUMN_BOOK_ID + " = ?",
                new String[]{book.book_id});

        if (isDEBUG) Log.e("updateBook", "updateBook ID: " + id);

        db.close();
    }

    public void updateQuantityBook(Book book, int quantity) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BK_COLUMN_QUANTITY, (book.quantity - quantity));

        long id = db.update(BOOK_TABLE, values,
                BK_COLUMN_BOOK_ID + " = ?",
                new String[]{book.book_id});

        if (isDEBUG) Log.e("updateQuantityBook", "updateQuantityBook ID: " + id);

        db.close();
    }

    public Book getBook(String book_id) {
        Book book = null;
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(BOOK_TABLE,
                new String[]{BK_COLUMN_BOOK_ID, BK_COLUMN_CATEGORY_ID, BK_COLUMN_BOOK_NAME,
                        BK_COLUMN_AUTHOR, BK_COLUMN_PUBLISHER, BK_COLUMN_PRICE, BK_COLUMN_QUANTITY},
                BK_COLUMN_BOOK_ID + " = ?",
                new String[]{book_id}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            book = new Book(cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_ID)),
                    cursor.getString(cursor.getColumnIndex(BK_COLUMN_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_NAME)),
                    cursor.getString(cursor.getColumnIndex(BK_COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(BK_COLUMN_PUBLISHER)),
                    cursor.getDouble(cursor.getColumnIndex(BK_COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(BK_COLUMN_QUANTITY)));

        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return book;

    }

    public List<Book> getAllBook() {
        db = dbHelper.getReadableDatabase();
        List<Book> books = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + BOOK_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.book_id = cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_ID));
                book.category_id = cursor.getString(cursor.getColumnIndex(BK_COLUMN_CATEGORY_ID));
                book.book_name = cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_NAME));
                book.author = cursor.getString(cursor.getColumnIndex(BK_COLUMN_AUTHOR));
                book.publisher = cursor.getString(cursor.getColumnIndex(BK_COLUMN_PUBLISHER));
                book.price = cursor.getDouble(cursor.getColumnIndex(BK_COLUMN_PRICE));
                book.quantity = cursor.getInt(cursor.getColumnIndex(BK_COLUMN_QUANTITY));

                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return books;
    }

    public boolean checkBook(String bookID) {

        String[] columns = {
                BK_COLUMN_BOOK_ID
        };
        db = dbHelper.getReadableDatabase();

        String selection = BK_COLUMN_BOOK_ID + " = ?";

        String[] selectionArgs = {bookID};

        Cursor cursor = db.query(BOOK_TABLE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }

    public List<BookIDItem> getAllBookID() {
        db = dbHelper.getReadableDatabase();
        List<BookIDItem> books = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + BOOK_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                BookIDItem book = new BookIDItem();
                book.bookID = cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_ID));

                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return books;
    }

    public List<Book> getAllBookOfCategory(String categoryID) {
        db = dbHelper.getReadableDatabase();
        List<Book> books = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + BOOK_TABLE + " WHERE " + BK_COLUMN_CATEGORY_ID + " = ?";
        String[] selectArgs = {categoryID};
        Cursor cursor = db.rawQuery(selectQuery, selectArgs);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.book_id = cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_ID));
                book.category_id = cursor.getString(cursor.getColumnIndex(BK_COLUMN_CATEGORY_ID));
                book.book_name = cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_NAME));
                book.author = cursor.getString(cursor.getColumnIndex(BK_COLUMN_AUTHOR));
                book.publisher = cursor.getString(cursor.getColumnIndex(BK_COLUMN_PUBLISHER));
                book.price = cursor.getDouble(cursor.getColumnIndex(BK_COLUMN_PRICE));
                book.quantity = cursor.getInt(cursor.getColumnIndex(BK_COLUMN_QUANTITY));

                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return books;
    }

}


