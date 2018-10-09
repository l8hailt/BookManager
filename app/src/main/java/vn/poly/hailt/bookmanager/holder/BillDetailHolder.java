package vn.poly.hailt.bookmanager.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import vn.poly.hailt.bookmanager.R;

public class BillDetailHolder extends RecyclerView.ViewHolder {
    public final TextView tvBookID;
    public final TextView tvPrice;
    public final TextView tvQuantity;
    public final TextView tvTotal;


    public BillDetailHolder(View convertView) {
        super(convertView);

        tvBookID = convertView.findViewById(R.id.tvBookID);
        tvPrice = convertView.findViewById(R.id.tvPrice);
        tvQuantity = convertView.findViewById(R.id.tvQuantity);
        tvTotal = convertView.findViewById(R.id.tvTotal);
    }
}
