package vn.poly.hailt.bookmanager.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.RecyclerItemClickListener;
import vn.poly.hailt.bookmanager.adapter.BillAdapter;
import vn.poly.hailt.bookmanager.dao.BillDAO;
import vn.poly.hailt.bookmanager.model.Bill;

public class BillActivity extends AppCompatActivity {

    private FloatingActionButton fabAddBill;
    private BillDAO billDAO;
    private BillAdapter billAdapter;
    private List<Bill> listBills;
    private RecyclerView lvBills;
    private SearchView searchView;

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
                                Bill bill = billAdapter.getBill(position);
                                int index = listBills.indexOf(bill);
                                showConfirmDeleteBill(index);
                            }
                        }));

        hideFABWhenScroll();

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_bill);

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
        String billID = listBills.get(position).billID;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action_delete) + " " + billID);
        builder.setMessage(getString(R.string.message_confirm_delete_bill));

        builder.setNegativeButton(getString(R.string.action_no), null);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                billDAO.deleteBill(listBills.get(position));
                listBills.remove(position);
                billAdapter.notifyDataSetChanged();
                Toast.makeText(BillActivity.this, R.string.toast_deleted_successfully, Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        billDAO = new BillDAO(this);
        listBills = billDAO.getAllBill();
        Collections.reverse(listBills);

        billAdapter = new BillAdapter(this, listBills);
        LinearLayoutManager manager = new LinearLayoutManager(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_view, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setQueryHint((getString(R.string.prompt_bill_id) + "..."));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        ArrayList<Bill> filteredList = new ArrayList<>();

        for (Bill item : listBills) {
            if (item.billID.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        billAdapter.filterList(filteredList);
    }

}
