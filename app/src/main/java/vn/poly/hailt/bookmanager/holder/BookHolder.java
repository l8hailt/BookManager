package vn.poly.hailt.bookmanager.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import vn.poly.hailt.bookmanager.R;

public class BookHolder extends RecyclerView.ViewHolder {

    public final ImageView imgIcon;
    public final TextView tvTitle;
    public final TextView tvSubTitle1;
    public final TextView tvSubTitle2;
    public final TextView tvPrice;


    public BookHolder(View convertView) {
        super(convertView);
        imgIcon = convertView.findViewById(R.id.imgIcon);
        tvTitle = convertView.findViewById(R.id.tvTitle);
        tvSubTitle1 = convertView.findViewById(R.id.tvSubTitle1);
        tvSubTitle2 = convertView.findViewById(R.id.tvSubTitle2);
        tvPrice = convertView.findViewById(R.id.tvPrice);
    }
}
