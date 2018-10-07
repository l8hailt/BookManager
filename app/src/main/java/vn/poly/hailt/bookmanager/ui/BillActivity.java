package vn.poly.hailt.bookmanager.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.RecyclerItemClickListener;
import vn.poly.hailt.bookmanager.adapter.BillAdapter;
import vn.poly.hailt.bookmanager.dao.BillDAO;
import vn.poly.hailt.bookmanager.model.Bill;

public class BillActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabAddBill;
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

//        billDAO = new BillDAO(this);
//        listBills = billDAO.getAllBill();
//
//        billAdapter = new BillAdapter(this, listBills);
//        manager = new LinearLayoutManager(this);
//        lvBills.setLayoutManager(manager);
//        lvBills.setAdapter(billAdapter);
//
        lvBills.addOnItemTouchListener(
                new RecyclerItemClickListener(this, lvBills,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(BillActivity.this, BillDetailActivity.class);
                                intent.putExtra("position", listBills.get(position).billID);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                showConfirmDeleteBill(position);
                            }
                        }));

        hideFABWhenScroll();

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        fabAddBill = findViewById(R.id.fabAddBill);
        lvBills = findViewById(R.id.lvBills);
    }

    private void initActions() {
        fabAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BillActivity.this, AddBillActivity.class));
            }
        });
    }

    private void showConfirmDeleteBill(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action_delete));
        builder.setMessage(getString(R.string.message_confirm_delete_bill_detail));

        builder.setNegativeButton(getString(R.string.action_no), null);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                billDAO.deleteBill(listBills.get(position));
                listBills.remove(position);
                billAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        billDAO = new BillDAO(this);
        listBills = billDAO.getAllBill();

        billAdapter = new BillAdapter(this, listBills);
        manager = new LinearLayoutManager(this);
        lvBills.setLayoutManager(manager);
        lvBills.setAdapter(billAdapter);

//        lvBills.addOnItemTouchListener(
//                new RecyclerItemClickListener(this, lvBills,
//                        new RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                Intent intent = new Intent(BillActivity.this, BillDetailActivity.class);
//                                intent.putExtra("position", listBills.get(position).billID);
//                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void onItemLongClick(View view, int position) {
//                                showConfirmDeleteBill(position);
//                            }
//                        }));
    }

    private void hideFABWhenScroll() {
        lvBills.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) fabAddBill.hide();
                else fabAddBill.show();
            }
        });
    }

}
