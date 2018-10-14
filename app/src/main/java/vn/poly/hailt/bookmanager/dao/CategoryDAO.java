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
import vn.poly.hailt.bookmanager.model.Category;

public class CategoryDAO implements Constant {
    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertCategory(Category category) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CT_COLUMN_CATEGORY_ID, category.getCategory_id());
        values.put(CT_COLUMN_CATEGORY_NAME, category.getCategory_name());

        long id = db.insert(CATEGORY_TABLE, null, values);

        if (isDEBUG) Log.e("insertCategory", "insertCategory ID: " + id);

        db.close();
    }

    public void deleteCategory(Category category) {
        db = dbHelper.getWritableDatabase();

        long id = db.delete(CATEGORY_TABLE,
                CT_COLUMN_CATEGORY_ID + " = ?",
                new String[]{category.getCategory_id()});

        if (isDEBUG) Log.e("deleteCategory", "deleteCategory ID: " + id);

        db.close();
    }

    public void updateCategory(Category category) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CT_COLUMN_CATEGORY_NAME, category.getCategory_name());

        long id = db.update(CATEGORY_TABLE, values,
                CT_COLUMN_CATEGORY_ID + " = ?",
                new String[]{category.getCategory_id()});

        if (isDEBUG) Log.e("updateCategory", "updateCategory ID: " + id);

        db.close();
    }

//    public Category getCategory(String category_id) {
//        Category category = null;
//        db = dbHelper.getReadableDatabase();
//
//        Cursor cursor = db.query(CATEGORY_TABLE,
//                new String[]{CT_COLUMN_CATEGORY_ID, CT_COLUMN_CATEGORY_NAME},
//                CT_COLUMN_CATEGORY_ID + " = ?",
//                new String[]{category_id}, null, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//
//            category = new Category(cursor.getString(cursor.getColumnIndex(CT_COLUMN_CATEGORY_ID)),
//                    cursor.getString(cursor.getColumnIndex(CT_COLUMN_CATEGORY_NAME)));
//
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//        db.close();
//
//        return category;
//
//    }

    public List<Category> getAllCategory() {
        db = dbHelper.getReadableDatabase();
        List<Category> categories = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CATEGORY_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCategory_id(cursor.getString(cursor.getColumnIndex(CT_COLUMN_CATEGORY_ID)));
                category.setCategory_name(cursor.getString(cursor.getColumnIndex(CT_COLUMN_CATEGORY_NAME)));

                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return categories;
    }

    public boolean checkCategory(String categoryID) {

        String[] columns = {
                CT_COLUMN_CATEGORY_ID
        };
        db = dbHelper.getReadableDatabase();

        String selection = CT_COLUMN_CATEGORY_ID + " = ?";

        String[] selectionArgs = {categoryID};

        Cursor cursor = db.query(CATEGORY_TABLE,
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
