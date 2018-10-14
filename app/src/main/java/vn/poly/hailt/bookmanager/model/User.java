package vn.poly.hailt.bookmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String username;
    private String password;
    private String name;
    private String phone_number;

    public User() {

    }

//    public User(String name, String phone_number) {
//        this.name = name;
//        this.phone_number = phone_number;
//    }

    public User(String user_name, String password, String name, String phone_number) {
        this.username = user_name;
        this.password = password;
        this.name = name;
        this.phone_number = phone_number;
    }

    private User(Parcel in) {
        username = in.readString();
        password = in.readString();
        name = in.readString();
        phone_number = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUser_name() {
        return username;
    }

    public void setUser_name(String user_name) {
        this.username = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(phone_number);
    }
}
