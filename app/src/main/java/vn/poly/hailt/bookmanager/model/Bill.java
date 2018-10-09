package vn.poly.hailt.bookmanager.model;

public class Bill {
    public String billID;
    public long date;

    public Bill(String billID, long date) {
        this.billID = billID;
        this.date = date;
    }

    public Bill() {

    }
}
