package vn.poly.hailt.bookmanager.model;

public class BillDetail {
    public int billDetailID;
    public Bill bill;
    public Book book;
    public int quantity;

//    public BillDetail(int billDetailID, Bill bill, Book book, int quantity) {
//        this.billDetailID = billDetailID;
//        this.bill = bill;
//        this.book = book;
//        this.quantity = quantity;
//    }

    public BillDetail(Bill bill, Book bookID, int quantity) {
        this.bill = bill;
        this.book = bookID;
        this.quantity = quantity;
    }

    public BillDetail() {

    }
}
