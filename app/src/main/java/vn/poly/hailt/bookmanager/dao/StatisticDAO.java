package vn.poly.hailt.bookmanager.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.database.DatabaseHelper;
import vn.poly.hailt.bookmanager.model.Book;

public class StatisticDAO implements Constant {

    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public StatisticDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<Book> getBookTop10(String month) {
        db = dbHelper.getReadableDatabase();
        List<Book> listBooks = new ArrayList<>();
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }

        String sSQL = "SELECT books.book_id, books.category_id, books.book_name, books.author, " +
                "books.publisher, books.price, books.quantity, SUM(bill_details.quantity) as quantity " +
                "FROM bills INNER JOIN bill_details ON bills.bill_id = bill_details.bill_id " +
                "INNER JOIN books ON bill_details.book_id = books.book_id " +
                "WHERE strftime('%m', date / 1000, 'unixepoch') =   '" + month + "' " +
                "GROUP BY books.book_id " +
                "ORDER BY quantity DESC LIMIT 10";
//        Log.e("sSQL", sSQL);
        Cursor cursor = db.rawQuery(sSQL, null);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.book_id = cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_ID));
                book.quantity = cursor.getInt(cursor.getColumnIndex(BK_COLUMN_QUANTITY));
                book.price = cursor.getDouble(cursor.getColumnIndex(BK_COLUMN_PRICE));
                book.category_id = cursor.getString(cursor.getColumnIndex(BK_COLUMN_CATEGORY_ID));
                book.book_name = cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_NAME));
                book.author = cursor.getString(cursor.getColumnIndex(BK_COLUMN_AUTHOR));
                book.publisher = cursor.getString(cursor.getColumnIndex(BK_COLUMN_PUBLISHER));

                listBooks.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listBooks;
    }

    //        String BOOK_TABLE = "books";
//        String BK_COLUMN_BOOK_ID = "book_id";
//        String BK_COLUMN_CATEGORY_ID = "category_id";
//        String BK_COLUMN_BOOK_NAME = "book_name";
//        String BK_COLUMN_AUTHOR = "author";
//        String BK_COLUMN_PUBLISHER = "publisher";
//        String BK_COLUMN_PRICE = "price";
//        String BK_COLUMN_QUANTITY = "quantity";

//        String BILL_TABLE = "bills";
//        String BL_COLUMN_ID = "bill_id";
//        String BL_COLUMN_DATE = "date";

//        String BILL_DETAIL_TABLE = "bill_details";
//        String BD_BILL_DETAIL_ID = "bill_details_id";
//        String BD_BILL_ID = "bill_id";
//        String BD_BOOK_ID = "book_id";
//        String BD_QUANTITY = "quantity";

    public double getStatisticByDay() {
        db = dbHelper.getReadableDatabase();

        double total = 0;

        String sSQL = "SELECT SUM(total) FROM (SELECT SUM(books.price * bill_details.quantity) as 'total' " +
                "FROM bills INNER JOIN bill_details on bills.bill_id = bill_details.bill_id " +
                "INNER JOIN books on bill_details.book_id = books.book_id " +
                "WHERE strftime('%d', bills.date / 1000, 'unixepoch') = strftime('%d','now') " +
                "GROUP BY bill_details.book_id)tmp";

        Cursor cursor = db.rawQuery(sSQL, null);

        if (cursor.moveToFirst()) {
            Log.e("SIZE", cursor.getCount() + "");
            do {
                total = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return total;
    }

    public double getStatisticByMonth() {
        db = dbHelper.getReadableDatabase();

        double total = 0;

        String sSQL = "SELECT SUM(total) FROM (SELECT SUM(books.price * bill_details.quantity) as 'total' " +
                "FROM bills INNER JOIN bill_details on bills.bill_id = bill_details.bill_id " +
                "INNER JOIN books on bill_details.book_id = books.book_id " +
                "WHERE strftime('%m', bills.date / 1000, 'unixepoch') = strftime('%m','now') " +
                "GROUP BY bill_details.book_id)tmp";

        Cursor cursor = db.rawQuery(sSQL, null);

        if (cursor.moveToFirst()) {
            Log.e("SIZE", cursor.getCount() + "");
            do {
                total = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return total;
    }

    public double getStatisticByYear() {
        db = dbHelper.getReadableDatabase();

        double total = 0;

        String sSQL = "SELECT SUM(total) FROM (SELECT SUM(books.price * bill_details.quantity) as 'total' " +
                "FROM bills INNER JOIN bill_details on bills.bill_id = bill_details.bill_id " +
                "INNER JOIN books on bill_details.book_id = books.book_id " +
                "WHERE strftime('%Y', bills.date / 1000, 'unixepoch') = strftime('%Y','now') " +
                "GROUP BY bill_details.book_id)tmp";

        Cursor cursor = db.rawQuery(sSQL, null);

        if (cursor.moveToFirst()) {
            Log.e("SIZE", cursor.getCount() + "");
            do {
                total = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return total;
    }


}
