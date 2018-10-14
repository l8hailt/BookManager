package vn.poly.hailt.bookmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.model.BookIDItem;

public class AutoCompleteBookIDAdapter extends ArrayAdapter<BookIDItem> {

    private final List<BookIDItem> listBookID;

    public AutoCompleteBookIDAdapter(@NonNull Context context, @NonNull List<BookIDItem> objects) {
        super(context, 0, objects);
        listBookID = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return bookIDFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_autocomplete, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        BookIDItem bookIDItem = getItem(position);

        if (bookIDItem != null) {
            textViewName.setText(bookIDItem.bookID);
        }

        return convertView;
    }

    private final Filter bookIDFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<BookIDItem> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(listBookID);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (BookIDItem item : listBookID) {
                    if (item.bookID.toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((BookIDItem) resultValue).bookID;
        }
    };
}
