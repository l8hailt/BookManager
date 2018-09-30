package vn.poly.hailt.bookmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public String book_id;
    public String category_id;
    public String book_name;
    public String author;
    public String publisher;
    public double price;
    public int quantity;

    public Book(String book_id, String category_id, String book_name, String author, String publisher, double price, int quantity) {
        this.book_id = book_id;
        this.category_id = category_id;
        this.book_name = book_name;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
    }

    public Book() {

    }

    protected Book(Parcel in) {
        book_id = in.readString();
        category_id = in.readString();
        book_name = in.readString();
        author = in.readString();
        publisher = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(book_id);
        dest.writeString(category_id);
        dest.writeString(book_name);
        dest.writeString(author);
        dest.writeString(publisher);
        dest.writeDouble(price);
        dest.writeInt(quantity);
    }
}
