package vn.poly.hailt.bookmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.holder.CategoryHolder;
import vn.poly.hailt.bookmanager.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    private Context context;
    private List<Category> listCategories;

    public CategoryAdapter(Context context, List<Category> listCategories) {
        this.context = context;
        this.listCategories = listCategories;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {

        holder.tvTitle.setText(listCategories.get(position).getCategory_id());
        holder.tvSubtitle.setText(listCategories.get(position).getCategory_name());
        holder.imgIcon.setImageResource(R.drawable.ic_category);

    }

    @Override
    public int getItemCount() {
        if (listCategories == null) return 0;

        return listCategories.size();
    }
}
