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

public class BillDAO implements Constant {
    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public BillDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertBill(Bill bill) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BL_COLUMN_ID, bill.billID);
        values.put(BL_COLUMN_DATE, bill.date);

        long id = db.insert(BILL_TABLE, null, values);

        if (isDEBUG) Log.e("insertBill", "insertBill ID: " + id);

        db.close();
    }

    public Bill getBill(String billID) {
        Bill bill = null;
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(BILL_TABLE,
                new String[]{BL_COLUMN_ID, BL_COLUMN_DATE},
                BL_COLUMN_ID + " = ?",
                new String[]{billID}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            bill = new Bill(cursor.getString(cursor.getColumnIndex(BL_COLUMN_ID)),
                    cursor.getLong(cursor.getColumnIndex(BL_COLUMN_DATE)));

        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return bill;

    }


    public void deleteBill(Bill bill) {
        db = dbHelper.getWritableDatabase();

        long _id = db.delete(BILL_DETAIL_TABLE, BD_BILL_ID + " = ?", new String[]{bill.billID});

        long id = db.delete(BILL_TABLE,
                BL_COLUMN_ID + " = ?",
                new String[]{bill.billID});

        if (isDEBUG) Log.e("deleteBillDetail", "deleteBillDetail ID: " + _id);
        if (isDEBUG) Log.e("deleteBill", "deleteBill ID: " + id);

        db.close();
    }

//    public void updateBill(Bill bill) {
//        db = dbHelper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(BL_COLUMN_DATE, bill.date);
//
//        long id = db.update(BILL_TABLE, values,
//                CT_COLUMN_CATEGORY_ID + " = ?",
//                new java.lang.String[]{bill.billID});
//
//        if (isDEBUG) Log.e("updateBill", "updateBill ID: " + id);
//
//        db.close();
//    }

    public List<Bill> getAllBill() {
        db = dbHelper.getReadableDatabase();
        List<Bill> bills = new ArrayList<>();

        java.lang.String selectQuery = "SELECT * FROM " + BILL_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.billID = cursor.getString(cursor.getColumnIndex(BL_COLUMN_ID));
                bill.date = cursor.getLong(cursor.getColumnIndex(BL_COLUMN_DATE));

                bills.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return bills;
    }

    public boolean checkBill(String billID) {

        String[] columns = {
                BL_COLUMN_ID
        };
        db = dbHelper.getReadableDatabase();

        String selection = BL_COLUMN_ID + " = ?";

        String[] selectionArgs = {billID};

        Cursor cursor = db.query(BILL_TABLE,
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
