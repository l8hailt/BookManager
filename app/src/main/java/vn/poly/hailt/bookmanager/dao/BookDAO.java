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

public class BookDAO implements Constant {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public BookDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertBook(Book book) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(B_COLUMN_BOOK_ID, book.book_id);
        values.put(B_COLUMN_CATEGORY_ID, book.category_id);
        values.put(B_COLUMN_BOOK_NAME, book.book_name);
        values.put(B_COLUMN_AUTHOR, book.author);
        values.put(B_COLUMN_PUBLISHER, book.publisher);
        values.put(B_COLUMN_PRICE, book.price);
        values.put(B_COLUMN_QUANTITY, book.quantity);

        long id = db.insert(BOOK_TABLE, null, values);

        if (isDEBUG) Log.e("insertBook", "insertBook ID: " + id);

        db.close();
    }

    public void deleteBook(Book book) {
        db = dbHelper.getWritableDatabase();

        long id = db.delete(BOOK_TABLE,
                B_COLUMN_BOOK_ID + " = ?",
                new String[]{book.book_id});

        if (isDEBUG) Log.e("deleteBook", "deleteBook ID: " + id);

        db.close();
    }

    public void updateBook(Book book) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(B_COLUMN_CATEGORY_ID, book.category_id);
        values.put(B_COLUMN_BOOK_NAME, book.book_name);
        values.put(B_COLUMN_AUTHOR, book.author);
        values.put(B_COLUMN_PUBLISHER, book.publisher);
        values.put(B_COLUMN_PRICE, book.price);
        values.put(B_COLUMN_QUANTITY, book.quantity);

        long id = db.update(BOOK_TABLE, values,
                B_COLUMN_BOOK_ID + " = ?",
                new String[]{book.book_id});

        if (isDEBUG) Log.e("updateBook", "updateBook ID: " + id);

        db.close();
    }

    public Book getBook(String book_id) {
        Book book = null;
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(BOOK_TABLE,
                new String[]{B_COLUMN_BOOK_ID, B_COLUMN_CATEGORY_ID, B_COLUMN_BOOK_NAME,
                        B_COLUMN_AUTHOR, B_COLUMN_PUBLISHER, B_COLUMN_PRICE, B_COLUMN_QUANTITY},
                B_COLUMN_BOOK_ID + " = ?",
                new String[]{book_id}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            book = new Book(cursor.getString(cursor.getColumnIndex(B_COLUMN_BOOK_ID)),
                    cursor.getString(cursor.getColumnIndex(B_COLUMN_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndex(B_COLUMN_BOOK_NAME)),
                    cursor.getString(cursor.getColumnIndex(B_COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(B_COLUMN_PUBLISHER)),
                    cursor.getDouble(cursor.getColumnIndex(B_COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(B_COLUMN_QUANTITY)));

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
                book.book_id = cursor.getString(cursor.getColumnIndex(B_COLUMN_BOOK_ID));
                book.category_id = cursor.getString(cursor.getColumnIndex(B_COLUMN_CATEGORY_ID));
                book.book_name = cursor.getString(cursor.getColumnIndex(B_COLUMN_BOOK_NAME));
                book.author = cursor.getString(cursor.getColumnIndex(B_COLUMN_AUTHOR));
                book.publisher = cursor.getString(cursor.getColumnIndex(B_COLUMN_PUBLISHER));
                book.price = cursor.getDouble(cursor.getColumnIndex(B_COLUMN_PRICE));
                book.quantity = cursor.getInt(cursor.getColumnIndex(B_COLUMN_QUANTITY));

                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return books;
    }

    public boolean checkBook(String bookID) {

        String[] columns = {
                B_COLUMN_BOOK_ID
        };
        db = dbHelper.getReadableDatabase();

        String selection = B_COLUMN_BOOK_ID + " = ?";

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

}
