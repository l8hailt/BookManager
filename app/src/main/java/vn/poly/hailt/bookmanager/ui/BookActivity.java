package vn.poly.hailt.bookmanager.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.BookDetailActivity;
import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.RecyclerItemClickListener;
import vn.poly.hailt.bookmanager.adapter.BookAdapter;
import vn.poly.hailt.bookmanager.dao.BookDAO;
import vn.poly.hailt.bookmanager.model.Book;


public class BookActivity extends AppCompatActivity implements Constant {

    private Toolbar toolbar;
    private EditText edtBookID;
    private FloatingActionButton fabAddBooks;
    private BookDAO bookDAO;
    private List<Book> listBooks;
    private RecyclerView lvListBook;
    private BookAdapter bookAdapter;
    private LinearLayoutManager manager;
    private BroadcastReceiver brBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        initViews();
        initActions();

        bookDAO = new BookDAO(this);

        setUpRecyclerView();

        hideFABWhenScroll();

        setUpBroadcastReceiver();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(ACTION_BOOK);
        registerReceiver(brBook, intentFilter);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(brBook);
        super.onDestroy();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        edtBookID = findViewById(R.id.edtBookID);
        fabAddBooks = findViewById(R.id.fabAddBooks);
        lvListBook = findViewById(R.id.lvBooks);
    }

    private void initActions() {
        fabAddBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookActivity.this, AddBookActivity.class));
            }
        });
        edtBookID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void hideFABWhenScroll() {
        lvListBook.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) fabAddBooks.hide();
                else fabAddBooks.show();
            }
        });
    }

    private void setUpRecyclerView() {
        listBooks = bookDAO.getAllBook();
        bookAdapter = new BookAdapter(this, listBooks);
        manager = new LinearLayoutManager(this);
        lvListBook.setLayoutManager(manager);
        lvListBook.setAdapter(bookAdapter);

        lvListBook.addOnItemTouchListener(
                new RecyclerItemClickListener(this, lvListBook,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
//                                Book book = bookAdapter.getItem(position);
                                showActionsDialog(position);
//                                showConfirmDeleteBook(book);
                            }
                        }));

    }

    private void setUpBroadcastReceiver() {
        brBook = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Book bookAdded = intent.getParcelableExtra("bookAdded");
                if (bookAdded != null) {
                    listBooks.add(bookAdded);
                    bookAdapter.notifyDataSetChanged();
                    Toast.makeText(BookActivity.this, R.string.toast_added_successfully, Toast.LENGTH_SHORT).show();
                }
                int position = intent.getIntExtra("position", -1);
                Book bookUpdated = intent.getParcelableExtra("bookUpdated");
                Log.e("TAGG", "" + position);
                if (bookUpdated != null && position != -1) {
                    listBooks.set(position, bookUpdated);
                    bookAdapter.notifyDataSetChanged();
                    Toast.makeText(BookActivity.this, R.string.toast_updated_successfully, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void showActionsDialog(final int position) {
        CharSequence actions[] = new CharSequence[]{"Sửa", "Xóa"};
        String bookName = listBooks.get(position).book_name;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(bookName);
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(BookActivity.this, BookDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    intent.putExtra("book", bundle);
                    startActivity(intent);
                } else {
                    showConfirmDeleteBook(position);
                }
            }
        });
        builder.show();
    }

    private void showConfirmDeleteBook(final int position) {
        String bookName = listBooks.get(position).book_name;
//        String bookName = book.book_name;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action_delete) + " " + bookName);
        builder.setMessage(getString(R.string.message_confirm_delete_book));

        builder.setNegativeButton(getString(R.string.action_no), null);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bookDAO.deleteBook(listBooks.get(position));
//                bookDAO.deleteBook(book);
                listBooks.remove(position);
//                listBooks.remove(book);
                Toast.makeText(BookActivity.this, R.string.toast_deleted_successfully, Toast.LENGTH_SHORT).show();
                bookAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private void filter(String text) {
        ArrayList<Book> filteredList = new ArrayList<>();

        for (Book item : listBooks) {
            if (item.book_id.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        bookAdapter.filterList(filteredList);
    }

}
