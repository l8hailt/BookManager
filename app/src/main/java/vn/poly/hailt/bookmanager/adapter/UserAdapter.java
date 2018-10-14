package vn.poly.hailt.bookmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.holder.UserHolder;
import vn.poly.hailt.bookmanager.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    private final Context context;
    private final List<User> listUsers;

    public UserAdapter(Context context, List<User> listUsers) {
        this.context = context;
        this.listUsers = listUsers;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {

        holder.tvTitle.setText(listUsers.get(position).getName());
        holder.tvSubtitle.setText(listUsers.get(position).getPhone_number());
        holder.imgIcon.setImageResource(R.drawable.ic_user);

    }

    @Override
    public int getItemCount() {

        if (listUsers == null) {
            return 0;
        }
        return listUsers.size();

    }
}
