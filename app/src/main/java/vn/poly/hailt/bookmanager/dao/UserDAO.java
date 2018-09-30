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
import vn.poly.hailt.bookmanager.model.User;

public class UserDAO implements Constant {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertUser(User user) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUser_name());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_PHONE_NUMBER, user.getPhone_number());

        long id = db.insert(USER_TABLE, null, values);

        if (isDEBUG) Log.e("insertUser", "insertUser ID: " + id);

        db.close();
    }

    public void deleteUser(User user) {
        db = dbHelper.getWritableDatabase();

        db.delete(USER_TABLE,
                COLUMN_USER_NAME + " = ?",
                new String[]{user.getUser_name()});

        db.close();
    }

    public void updateUser(User user) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_PHONE_NUMBER, user.getPhone_number());

        long id = db.update(USER_TABLE, values,
                COLUMN_USER_NAME + " = ?",
                new String[]{user.getUser_name()});

        if (isDEBUG) Log.e("updateUser", "updateUser ID: " + id);
        db.close();
    }

    public void changedPasswordUser(User user) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, user.getPassword());

        long id = db.update(USER_TABLE, values,
                COLUMN_USER_NAME + " = ?",
                new String[]{user.getUser_name()});

        if (isDEBUG) Log.e("changedPasswordUser", "changedPasswordUser ID: " + id);
        db.close();
    }

    public User getUser(String username) {
        User user = null;
        db = dbHelper.getReadableDatabase();

        // (ten bang, arr ten cot, ten cot khoa chinh, gia tri khoa chinh, cac tham so con lai la null)

        Cursor cursor = db.query(USER_TABLE,
                new String[]{COLUMN_USER_NAME, COLUMN_PASSWORD, COLUMN_NAME, COLUMN_PHONE_NUMBER},
                COLUMN_USER_NAME + " = ? ",
                new String[]{username}, null, null, null, null);


        if (cursor != null && cursor.moveToFirst()) {

            // khoi tao user voi cac gia tri lay duoc
            user = new User(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));

        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return user;

    }

    public List<User> getAllUser() {
        db = dbHelper.getReadableDatabase();
        List<User> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + USER_TABLE;

//        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUser_name(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setPhone_number(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));

                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // return notes list
        return users;
    }

    public boolean checkUser(String username) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_NAME
        };
        db = dbHelper.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {username};

        // query user table with condition
        Cursor cursor = db.query(USER_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }


    public boolean checkUser(String username, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_NAME
        };
        db = dbHelper.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {username, password};

        // query user table with conditions
        Cursor cursor = db.query(USER_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        return cursorCount > 0;

    }


}
