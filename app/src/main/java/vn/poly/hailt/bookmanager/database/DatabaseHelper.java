package vn.poly.hailt.bookmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import vn.poly.hailt.bookmanager.Constant;

public class DatabaseHelper extends SQLiteOpenHelper implements Constant {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_BILL_TABLE);
        if (isDEBUG) Log.e("CREATE_USER_TABLE", CREATE_USER_TABLE);
        if (isDEBUG) Log.e("CREATE_CATEGORY_TABLE", CREATE_CATEGORY_TABLE);
        if (isDEBUG) Log.e("CREATE_BOOK_TABLE", CREATE_BOOK_TABLE);
        if (isDEBUG) Log.e("CREATE_BILL_TABLE", CREATE_BILL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_BILL_TABLE);

        onCreate(db);
    }
}
