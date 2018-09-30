package vn.poly.hailt.bookmanager;

public interface Constant {

    boolean isDEBUG = true;

    //REQUEST_CODE
    int REQUEST_CODE_ADD_USER = 111;
    int REQUETS_CODE_UPDATE_USER = 123;

    // BROADCAST RECEIVER
    String ACTION_CATEGORY = "action_category";
    String ACTION_BOOK = "action_book";

    // SharedPreferences
    String PREF_NAME = "BookManagerPref";
    String IS_REMEMBER = "isRemember";
    String KEY_USERNAME = "username";
    String KEY_PASSWORD = "password";


    // Database

    //USER
    String DATABASE_NAME = "BookManager";
    int DATABASE_VERSION = 1;

    String USER_TABLE = "users";

    String COLUMN_USER_NAME = "username";
    String COLUMN_PASSWORD = "password";
    String COLUMN_NAME = "name";
    String COLUMN_PHONE_NUMBER = "phone_number";

    String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + "("
                    + COLUMN_USER_NAME + " VARCHAR PRIMARY KEY, "
                    + COLUMN_PASSWORD + " VARCHAR, "
                    + COLUMN_NAME + " VARCHAR, "
                    + COLUMN_PHONE_NUMBER + " VARCHAR"
                    + ")";

    //CATEGORY
    String CATEGORY_TABLE = "categories";

    String CT_COLUMN_CATEGORY_ID = "category_id";
    String CT_COLUMN_CATEGORY_NAME = "category_name";

    String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + CATEGORY_TABLE + "("
                    + CT_COLUMN_CATEGORY_ID + " CHAR(10) PRIMARY KEY, "
                    + CT_COLUMN_CATEGORY_NAME + " NVARCHAR(50) NOT NULL"
                    + ")";

    //BOOK
    String BOOK_TABLE = "books";

    String B_COLUMN_BOOK_ID = "book_id";
    String B_COLUMN_CATEGORY_ID = "category_id";
    String B_COLUMN_BOOK_NAME = "book_name";
    String B_COLUMN_AUTHOR = "author";
    String B_COLUMN_PUBLISHER = "publisher";
    String B_COLUMN_PRICE = "price";
    String B_COLUMN_QUANTITY = "quantity";

    String CREATE_BOOK_TABLE =
            "CREATE TABLE " + BOOK_TABLE + "("
                    + B_COLUMN_BOOK_ID + " NCHAR(10) PRIMARY KEY, "
                    + B_COLUMN_CATEGORY_ID + " NCHAR(10), "
                    + B_COLUMN_BOOK_NAME + " NVARCHAR(50) NOT NULL, "
                    + B_COLUMN_AUTHOR + " NVARCHAR(50), "
                    + B_COLUMN_PUBLISHER + " NVARCHAR(50), "
                    + B_COLUMN_PRICE + " DOUBLE NOT NULL, "
                    + B_COLUMN_QUANTITY + " INT NOT NULL"
                    + ")";
}
