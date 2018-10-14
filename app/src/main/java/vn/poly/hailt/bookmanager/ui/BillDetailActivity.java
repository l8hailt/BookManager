package vn.poly.hailt.bookmanager.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.adapter.BillDetailAdapter;
import vn.poly.hailt.bookmanager.dao.BillDetailDAO;
import vn.poly.hailt.bookmanager.model.BillDetail;

public class BillDetailActivity extends AppCompatActivity {

    private RecyclerView lvBillDetail;
    private TextView tvTotal;
    //    private FloatingActionButton fabAddBillDetail;
    private List<BillDetail> listBillDetails;
    private BillDetailDAO billDetailDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        initViews();

        billDetailDAO = new BillDetailDAO(this);

        setUpRecyclerView();

//        initActions();
        setTotal();

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        lvBillDetail = findViewById(R.id.lvBillDetail);

        tvTotal = findViewById(R.id.tvTotal);
//        fabAddBillDetail = findViewById(R.id.fabAddBillDetail);
    }

//    private void initActions() {
////        fabAddBillDetail.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(BillDetailActivity.this, AddBillDetailActivity.class);
////                startActivity(intent);
////            }
////        });
//    }

    private void setUpRecyclerView() {
        listBillDetails = billDetailDAO.getAllBillDetail(getIntent().getStringExtra("position"));
        BillDetailAdapter billDetailAdapter = new BillDetailAdapter(this, listBillDetails);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lvBillDetail.setLayoutManager(manager);
        lvBillDetail.setAdapter(billDetailAdapter);

//        lvBillDetail.addOnItemTouchListener(
//                new RecyclerItemClickListener(this, lvBillDetail,
//                        new RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//
//                            }
//
//                            @Override
//                            public void onItemLongClick(View view, int position) {
//                                showConfirmDeleteBillDetail(position);
//                            }
//                        }));

    }

//    private void showConfirmDeleteBillDetail(final int position) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getString(R.string.action_delete));
//        builder.setMessage(getString(R.string.message_confirm_delete_bill_detail));
//
//        builder.setNegativeButton(getString(R.string.action_no), null);
//        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                billDetailDAO.deleteBillDetail(String.valueOf(listBillDetails.get(position).billDetailID));
//                listBillDetails.remove(position);
//
//                billDetailAdapter.notifyDataSetChanged();
//                setTotal();
//            }
//        });
//        builder.show();
//    }

    private void setTotal() {
        double total = 0;
        for (BillDetail billDetail : listBillDetails) {
            total = total + (billDetail.book.price * billDetail.quantity);
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        String currencyTotal = fmt.format(total);
        tvTotal.setText((getString(R.string.label_total) + ": " + currencyTotal));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
