package vn.poly.hailt.bookmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.holder.BillDetailHolder;
import vn.poly.hailt.bookmanager.model.BillDetail;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailHolder> {

    private final Context context;
    private final List<BillDetail> listBillDetails;

    public BillDetailAdapter(Context context, List<BillDetail> listBillDetails) {
        this.context = context;
        this.listBillDetails = listBillDetails;
    }

    @NonNull
    @Override
    public BillDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_detail, parent, false);
        return new BillDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillDetailHolder holder, int position) {
        BillDetail billDetail = listBillDetails.get(position);
        double price = billDetail.book.price;
        int quantity = billDetail.quantity;
        double total = price * quantity;

        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        String currencyPrice = fmt.format(price);
        String currencyTotal = fmt.format(total);

        holder.tvBookID.setText((context.getString(R.string.prompt_book_id) + ": " + billDetail.book.book_id));
        holder.tvPrice.setText((context.getString(R.string.prompt_price) + ": " + currencyPrice));
        holder.tvQuantity.setText((context.getString(R.string.prompt_quantity) + ": " + quantity));
        holder.tvTotal.setText((context.getString(R.string.label_total_money) + " " + currencyTotal));
    }

    @Override
    public int getItemCount() {
        if (listBillDetails == null) return 0;
        return listBillDetails.size();
    }
}
