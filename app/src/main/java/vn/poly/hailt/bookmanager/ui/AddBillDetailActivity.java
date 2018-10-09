package vn.poly.hailt.bookmanager.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.RecyclerItemClickListener;
import vn.poly.hailt.bookmanager.adapter.AutoCompleteBookIDAdapter;
import vn.poly.hailt.bookmanager.adapter.BillDetailAdapter;
import vn.poly.hailt.bookmanager.dao.BillDAO;
import vn.poly.hailt.bookmanager.dao.BillDetailDAO;
import vn.poly.hailt.bookmanager.dao.BookDAO;
import vn.poly.hailt.bookmanager.model.BillDetail;
import vn.poly.hailt.bookmanager.model.BookIDItem;

public class AddBillDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvBillID;
    private TextInputLayout tlNotifyBookID;
    private AutoCompleteTextView edtBookID;
    private EditText edtQuantity;
    private Button btnAdd;
    private Button btnPay;
    private TextView tvTotal;
    private RecyclerView lvBillDetail;
    private BillDetailDAO billDetailDAO;
    private BookDAO bookDAO;
    private BillDAO billDAO;
    private List<BookIDItem> listBookID;
    private List<BillDetail> listBillDetails;
    private LinearLayoutManager manager;
    private BillDetailAdapter billDetailAdapter;
    private AutoCompleteBookIDAdapter bookIDAdapter;
    private String billID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill_detail);

        initViews();

        billDetailDAO = new BillDetailDAO(this);
        bookDAO = new BookDAO(this);
        billDAO = new BillDAO(this);

        listBookID = bookDAO.getAllBookID();
        bookIDAdapter = new AutoCompleteBookIDAdapter(this, listBookID);
        edtBookID.setAdapter(bookIDAdapter);

        initActions();

        setUpRecyclerView();
        billID = getIntent().getStringExtra("billID");
        tvBillID.setText(billID);

        billDetailDAO.getDateFromMS();

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        tvBillID = findViewById(R.id.tvBillID);
        edtBookID = findViewById(R.id.edtBookID);
        tlNotifyBookID = findViewById(R.id.tlNotifyBookID);
        edtQuantity = findViewById(R.id.edtQuantity);
        btnAdd = findViewById(R.id.btnAdd);
        btnPay = findViewById(R.id.btnPay);
        tvTotal = findViewById(R.id.tvTotal);
        lvBillDetail = findViewById(R.id.lvBillDetail);
    }

    private void initActions() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBillDetail();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payBillDetail();
            }
        });
    }

    private void addBillDetail() {
        String bookID = edtBookID.getText().toString().trim();
        String quantity = edtQuantity.getText().toString().trim();

        if (validateForm(bookID, quantity)) {
            if (bookDAO.checkBook(bookID)) {

                BillDetail billDetail = new BillDetail(billDAO.getBill(billID), bookDAO.getBook(bookID), Integer.parseInt(quantity));
                listBillDetails.add(billDetail);
                billDetailAdapter.notifyDataSetChanged();

                tlNotifyBookID.setError(null);
                tlNotifyBookID.setErrorEnabled(false);
            } else {
                tlNotifyBookID.setErrorEnabled(true);
                tlNotifyBookID.setError(getString(R.string.notify_book_id_not_exists));
                edtBookID.requestFocus();
            }
        }
    }

    private void payBillDetail() {
        double total = 0;

        for (BillDetail billDetail : listBillDetails) {
            billDetailDAO.insertBillDetail(billDetail);
            total = total + (billDetail.book.price * billDetail.quantity);
        }
        tvTotal.setText((getString(R.string.label_total) + ": " + total + " " + getString(R.string.currency)));
        edtBookID.setText(null);
        edtBookID.requestFocus();
        edtQuantity.setText(null);
        listBillDetails.clear();
        billDetailAdapter.notifyDataSetChanged();
    }

    private void setUpRecyclerView() {
        listBillDetails = new ArrayList<>();
        billDetailAdapter = new BillDetailAdapter(this, listBillDetails);
        manager = new LinearLayoutManager(this);
        lvBillDetail.setLayoutManager(manager);
        lvBillDetail.setAdapter(billDetailAdapter);

        lvBillDetail.addOnItemTouchListener(
                new RecyclerItemClickListener(this, lvBillDetail,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                showConfirmDeleteBillDetail(position);
                            }
                        }));

    }

    private void showConfirmDeleteBillDetail(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action_delete));
        builder.setMessage(getString(R.string.message_confirm_delete_bill_detail));

        builder.setNegativeButton(getString(R.string.action_no), null);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listBillDetails.remove(position);
                billDetailAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private boolean validateForm(String bookID, String quantity) {
        if (bookID.matches("")) {
            edtBookID.setError(getString(R.string.notify_empty));
            return false;
        }
        if (quantity.matches("")) {
            edtQuantity.setError(getString(R.string.notify_empty));
            return false;
        }


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
