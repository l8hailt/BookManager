package vn.poly.hailt.bookmanager.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import vn.poly.hailt.bookmanager.R;

public class BillHolder extends RecyclerView.ViewHolder {

    public final ImageView imgIcon;
    public final TextView tvTitle;
    public final TextView tvSubTitle;

    public BillHolder(View convertView) {
        super(convertView);
        imgIcon = convertView.findViewById(R.id.imgIcon);
        tvTitle = convertView.findViewById(R.id.tvTitle);
        tvSubTitle = convertView.findViewById(R.id.tvSubTitle);
    }
}
