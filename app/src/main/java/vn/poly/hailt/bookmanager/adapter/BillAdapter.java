package vn.poly.hailt.bookmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.holder.BillHolder;
import vn.poly.hailt.bookmanager.model.Bill;

public class BillAdapter extends RecyclerView.Adapter<BillHolder> {

    private Context context;
    private List<Bill> listBills;

    public BillAdapter(Context context, List<Bill> listBills) {
        this.context = context;
        this.listBills = listBills;
    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        return new BillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillHolder holder, int position) {
        Bill bill = listBills.get(position);
        holder.tvTitle.setText((context.getString(R.string.prompt_bill_id) + ": " + bill.billID));
        holder.tvSubTitle.setText(new Date(bill.date).toString());
        holder.imgIcon.setImageResource(R.drawable.ic_bill);
    }

    @Override
    public int getItemCount() {
        if (listBills == null) return 0;
        return listBills.size();
    }

//    public void filterList(ArrayList<Book> filteredList) {
//        listBills = filteredList;
//        notifyDataSetChanged();
//    }

}
