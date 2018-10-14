package vn.poly.hailt.bookmanager.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private TextView tvBillID;
    private TextInputLayout tlNotifyBookID;
    private AutoCompleteTextView edtBookID;
    private TextInputLayout tlNotifyQuantity;
    private EditText edtQuantity;
    private Button btnAdd;
    private Button btnPay;
    private TextView tvTotal;
    private RecyclerView lvBillDetail;
    private BillDetailDAO billDetailDAO;
    private BookDAO bookDAO;
    private BillDAO billDAO;
    private List<BillDetail> listBillDetails;
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

        List<BookIDItem> listBookID = bookDAO.getAllBookID();
        bookIDAdapter = new AutoCompleteBookIDAdapter(this, listBookID);
        edtBookID.setAdapter(bookIDAdapter);

        initActions();

        setUpRecyclerView();
        billID = getIntent().getStringExtra("billID");
        tvBillID.setText(billID);

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        tvBillID = findViewById(R.id.tvBillID);
        edtBookID = findViewById(R.id.edtBookID);
        tlNotifyBookID = findViewById(R.id.tlNotifyBookID);
        tlNotifyQuantity = findViewById(R.id.tlNotifyQuantity);
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

                edtBookID.setText(null);
                edtQuantity.setText(null);

                removeErrorTlBookID();
                removeErrorTlQuantity();
                edtBookID.requestFocus();
            } else {
                tlNotifyBookID.setErrorEnabled(true);
                tlNotifyBookID.setError(getString(R.string.notify_book_id_not_exists));
                edtBookID.requestFocus();
            }
        }
    }

    private void payBillDetail() {
        double total = 0;
        int booksInStock;
        int quantity;
        boolean isOK = false;
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < listBillDetails.size(); i++) {
            BillDetail billDetail = listBillDetails.get(i);
            booksInStock = bookDAO.getBook(billDetail.book.book_id).quantity;
            quantity = billDetail.quantity;

            if (quantity < booksInStock) {
                isOK = true;
                billDetailDAO.insertBillDetail(billDetail);
                total += billDetail.book.price * billDetail.quantity;
                bookDAO.updateQuantityBook(billDetail.book, quantity);

                Locale locale = new Locale("vi", "VN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                String currencyTotal = fmt.format(total);

                tvTotal.setText((getString(R.string.label_total) + ": " + currencyTotal));

                edtBookID.setText(null);
                edtBookID.requestFocus();
                edtQuantity.setText(null);
                listBillDetails.remove(billDetail);
                billDetailAdapter.notifyDataSetChanged();
                removeErrorTlBookID();
                removeErrorTlQuantity();
            } else {
                message.append(getString(R.string.prompt_book_id)).append(" ").append(billDetail.book.book_id)
                        .append("\n").append(getString(R.string.books_in_stock)).append(": ").append(booksInStock).append("\n");
                isOK = false;

            }
//            if (quantity > booksInStock) {
//                message.append(getString(R.string.prompt_book_id)).append(" ").append(billDetail.book.book_id)
//                        .append("\n").append(getString(R.string.books_in_stock)).append(": ").append(booksInStock).append("\n");
//                isOK = false;
//            } else {
//                billDetailDAO.insertBillDetail(billDetail);
//                total += billDetail.book.price * billDetail.quantity;
//                bookDAO.updateQuantityBook(billDetail.book, quantity);
//
//                Locale locale = new Locale("vi", "VN");
//                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//                String currencyTotal = fmt.format(total);
//
//                tvTotal.setText((getString(R.string.label_total) + ": " + currencyTotal));
//
//                edtBookID.setText(null);
//                edtBookID.requestFocus();
//                edtQuantity.setText(null);
//                listBillDetails.remove(billDetail);
//                billDetailAdapter.notifyDataSetChanged();
//                removeErrorTlBookID();
//                removeErrorTlQuantity();
//            }
        }

        if (!isOK) {
            showAlertDialogBooksInStock(message.toString());
        }

    }

    private void setUpRecyclerView() {
        listBillDetails = new ArrayList<>();
        billDetailAdapter = new BillDetailAdapter(this, listBillDetails);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lvBillDetail.setLayoutManager(manager);
        lvBillDetail.setAdapter(billDetailAdapter);

        lvBillDetail.addOnItemTouchListener(
                new RecyclerItemClickListener(this, lvBillDetail,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                showUpdateBillDetailDialog(position);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                showConfirmDeleteBillDetail(position);
                            }
                        }));

    }

    private void showUpdateBillDetailDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.action_edit);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_update_bill_detail, null);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.action_edit, null);
        builder.setNegativeButton(R.string.action_cancel, null);

        final AutoCompleteTextView dialogEdtBookID = dialogView.findViewById(R.id.edtBookID);
        dialogEdtBookID.setAdapter(bookIDAdapter);
        final EditText dialogEdtQuantity = dialogView.findViewById(R.id.edtQuantity);

        final TextInputLayout dialogTLNotifyBookID = dialogView.findViewById(R.id.tlNotifyBookID);

        final BillDetail billDetail = listBillDetails.get(position);

        dialogEdtBookID.setText(billDetail.book.book_id);
        dialogEdtBookID.setSelection(billDetail.book.book_id.length());
        dialogEdtQuantity.setText(String.valueOf(billDetail.quantity));


        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button btnPositive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String bookID = dialogEdtBookID.getText().toString().trim();
                        String quantity = dialogEdtQuantity.getText().toString().trim();

                        if (validateFormDialog(bookID, quantity, dialogEdtBookID, dialogEdtQuantity)) {
                            if (bookDAO.checkBook(bookID)) {

                                billDetail.book.book_id = bookID;
                                billDetail.quantity = Integer.parseInt(quantity);
                                listBillDetails.set(position, billDetail);
                                billDetailAdapter.notifyItemChanged(position);

                                dialog.dismiss();
                                hideSoftKeyboard();
                            } else {
                                dialogTLNotifyBookID.setErrorEnabled(true);
                                dialogTLNotifyBookID.setError(getString(R.string.notify_book_id_not_exists));
                            }
                        }

                    }
                });
            }
        });
        dialog.show();
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

    private void showAlertDialogBooksInStock(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.books_in_stock_not_enough);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.action_close, null);
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

    private boolean validateFormDialog(String bookID, String quantity, AutoCompleteTextView dialogEdtBookID, EditText dialogEdtQuantity) {
        if (bookID.matches("")) {
            dialogEdtBookID.setError(getString(R.string.notify_empty));
            return false;
        }
        if (quantity.matches("")) {
            dialogEdtQuantity.setError(getString(R.string.notify_empty));
            return false;
        }


        return true;
    }

    private void removeErrorTlBookID() {
        tlNotifyBookID.setError(null);
        tlNotifyBookID.setErrorEnabled(false);
    }

    private void removeErrorTlQuantity() {
        tlNotifyQuantity.setError(null);
        tlNotifyQuantity.setErrorEnabled(false);
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
