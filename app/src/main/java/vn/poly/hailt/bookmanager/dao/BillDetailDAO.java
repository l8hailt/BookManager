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
import vn.poly.hailt.bookmanager.model.Bill;
import vn.poly.hailt.bookmanager.model.BillDetail;
import vn.poly.hailt.bookmanager.model.Book;

public class BillDetailDAO implements Constant {
    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public BillDetailDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertBillDetail(BillDetail billDetail) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BD_BILL_ID, billDetail.bill.billID);
        values.put(BD_BOOK_ID, billDetail.book.book_id);
        values.put(BD_QUANTITY, billDetail.quantity);

        long id = db.insert(BILL_DETAIL_TABLE, null, values);

        Log.e("insertBillDetail", "insertBillDetail ID: " + id);

        db.close();

    }

    public List<BillDetail> getAllBillDetail(String billID) {
        db = dbHelper.getReadableDatabase();
        List<BillDetail> billDetails = new ArrayList<>();

        // SELECT MAHOADONCHITIET, HOADON.MAHOADON, HOADON.NGAYMUA, SACH.MASACH, SACH.MATHELOAI, SACH.TENSACH, SACH.TACGIA, SACH.NXB, SACH.GIABIA, SACH.SOLUONG, HOADONCHITIET.SOLUONG
        // FROM HOADON INNER JOIN HOADONCHITIET ON HOADON.MAHOADON = HOADONCHITIET.MAHOADON INNER JOIN SACH ON HOADONCHITIET.MASACH = SACH.MASACH
        // WHERE HOADONCHITIET.MAHOADON =?


        String selectQuery =
                "SELECT bill_details_id, bills.bill_id, bills.date, " +
                        "books.book_id, books.category_id, books.book_name, books.author, books.publisher, books.price, books.quantity, " +
                        "bill_details.quantity " +
                        "FROM bills INNER JOIN bill_details on bills.bill_id = bill_details.bill_id INNER JOIN books on bill_details.book_id = books.book_id " +
                        "WHERE bill_details.bill_id = ?";

        String[] selectionArgs = {billID};

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                BillDetail billDetail = new BillDetail();
                billDetail.billDetailID = cursor.getInt(cursor.getColumnIndex(BD_BILL_DETAIL_ID));

                billDetail.bill = new Bill(cursor.getString(cursor.getColumnIndex(BL_COLUMN_ID)),
                        cursor.getLong(cursor.getColumnIndex(BL_COLUMN_DATE)));

                billDetail.book = new Book(cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndex(BK_COLUMN_CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndex(BK_COLUMN_BOOK_NAME)),
                        cursor.getString(cursor.getColumnIndex(BK_COLUMN_AUTHOR)),
                        cursor.getString(cursor.getColumnIndex(BK_COLUMN_PUBLISHER)),
                        cursor.getDouble(cursor.getColumnIndex(BK_COLUMN_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(BK_COLUMN_QUANTITY)));

                billDetail.quantity = cursor.getInt(cursor.getColumnIndex(BD_QUANTITY));

                billDetails.add(billDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return billDetails;
    }

//    public void deleteBillDetail(String billDetailID) {
//        db = dbHelper.getWritableDatabase();
//
//
//        long id = db.delete(BILL_DETAIL_TABLE,
//                BD_BILL_DETAIL_ID + " = ?",
//                new String[]{billDetailID});
//
//        if (isDEBUG) Log.e("deleteBillDetail", "deleteBillDetail ID: " + id);
//
//        db.close();
//    }

}
