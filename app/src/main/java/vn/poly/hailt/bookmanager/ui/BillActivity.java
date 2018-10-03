package vn.poly.hailt.bookmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.adapter.BillAdapter;
import vn.poly.hailt.bookmanager.dao.BillDAO;
import vn.poly.hailt.bookmanager.model.Bill;

public class BillActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabAddOrders;
    private BillDAO billDAO;
    private BillAdapter billAdapter;
    private List<Bill> listBills;
    private RecyclerView lvBills;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        initViews();
        initActions();

        billDAO = new BillDAO(this);
        listBills = billDAO.getAllBill();

        billAdapter = new BillAdapter(this, listBills);
        manager = new LinearLayoutManager(this);
        lvBills.setLayoutManager(manager);
        lvBills.setAdapter(billAdapter);

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

        fabAddOrders = findViewById(R.id.fabAddOrders);
        lvBills = findViewById(R.id.lvBills);
    }

    private void initActions() {
        fabAddOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this, AddBillActivity.class));
            }
        });
    }
}
