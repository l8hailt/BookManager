package vn.poly.hailt.bookmanager.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import vn.poly.hailt.bookmanager.R;

public class UserHolder extends RecyclerView.ViewHolder {

    public final TextView tvTitle;
    public final TextView tvSubtitle;
    public final ImageView imgIcon;


    public UserHolder(View convertView) {
        super(convertView);

        tvTitle = convertView.findViewById(R.id.tvTitle);
        tvSubtitle = convertView.findViewById(R.id.tvSubTitle);
        imgIcon = convertView.findViewById(R.id.imgIcon);
    }
}
