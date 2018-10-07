package vn.poly.hailt.bookmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.holder.BookHolder;
import vn.poly.hailt.bookmanager.model.Book;

public class BookAdapter extends RecyclerView.Adapter<BookHolder> {

    private Context context;
    private List<Book> listBooks;

    public BookAdapter(Context context, List<Book> listBooks) {
        this.context = context;
        this.listBooks = listBooks;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book book = listBooks.get(position);
        holder.tvTitle.setText((context.getString(R.string.prompt_book_id) + ": " + book.book_id));
        holder.tvSubTitle1.setText(book.book_name);
        holder.tvSubTitle2.setText((context.getString(R.string.prompt_quantity) + ": " + book.quantity));
        holder.tvPrice.setText((book.price + " " + context.getString(R.string.currency)));
        holder.imgIcon.setImageResource(R.drawable.ic_book);
    }

    public Book getItem(int position) {
        return listBooks.get(position);
    }

    @Override
    public int getItemCount() {
        if (listBooks == null) return 0;
        return listBooks.size();
    }

    public void filterList(ArrayList<Book> filteredList) {
        listBooks = filteredList;
        notifyDataSetChanged();
    }

}
