package vn.poly.hailt.bookmanager.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.BillDAO;
import vn.poly.hailt.bookmanager.model.Bill;

public class AddBillActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgDatePicker;
    private Button btnNext;
    private EditText edtBillID;
    private TextView tvDate;
    private Button btnReset;
    private TextInputLayout tlNotifyBillID;
    private TextView tvNotifyDate;
    private View viewNotifyDate;
    private BillDAO billDAO;
    private long timeMS = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        initViews();

        billDAO = new BillDAO(this);

        initActions();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        imgDatePicker = findViewById(R.id.imgDatePicker);
        btnNext = findViewById(R.id.btnNext);
        edtBillID = findViewById(R.id.edtBillID);
        tvDate = findViewById(R.id.tvDate);
        btnReset = findViewById(R.id.btnReset);
        tlNotifyBillID = findViewById(R.id.tlNotifyBillID);
        tvNotifyDate = findViewById(R.id.tvNotifyDate);
        viewNotifyDate = findViewById(R.id.viewNotifyDate);
    }

    private void initActions() {
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBill();

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtBillID.setText(null);
                tvDate.setText(null);
            }
        });
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                timeMS = calendar.getTimeInMillis();

                tvDate.setText(new Date(timeMS).toString());
                tvNotifyDate.setText(null);
                viewNotifyDate.setBackgroundColor(Color.BLACK);

            }
        }, year, month, day);
        datePicker.show();
    }

    private void addBill() {
        String billID = edtBillID.getText().toString().trim();

        if (validateForm(billID)) {
            if (!billDAO.checkBill(billID)) {
                Bill bill = new Bill(billID, timeMS);
                billDAO.insertBill(bill);
                tlNotifyBillID.setError(null);
                tlNotifyBillID.setErrorEnabled(false);
                Intent intent = new Intent(AddBillActivity.this, AddBillDetailActivity.class);
                intent.putExtra("billID", billID);
                startActivity(intent);
                finish();
            } else {
                tlNotifyBillID.setErrorEnabled(true);
                tlNotifyBillID.setError(getString(R.string.notify_bill_id_exists));
                edtBillID.requestFocus();
            }
        }
    }

    private boolean validateForm(String billID) {
        if (billID.matches("")) {
            edtBillID.setError(getString(R.string.notify_empty));
            return false;
        }
        if (billID.length() < 3 || billID.length() > 10) {
            edtBillID.setError(getString(R.string.notify_length_bill_id));
            return false;
        }

        if (timeMS == -1) {
            tvNotifyDate.setText(getString(R.string.notify_date_empty));
            viewNotifyDate.setBackgroundColor(Color.RED);
            return false;
        }

        return true;
    }

}
