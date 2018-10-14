package vn.poly.hailt.bookmanager.ui;

import android.app.SearchManager;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.RecyclerItemClickListener;
import vn.poly.hailt.bookmanager.adapter.BookAdapter;
import vn.poly.hailt.bookmanager.dao.BookDAO;
import vn.poly.hailt.bookmanager.model.Book;


public class BookActivity extends AppCompatActivity implements Constant {

    private FloatingActionButton fabAddBooks;
    private BookDAO bookDAO;
    private List<Book> listBooks;
    private RecyclerView lvListBook;
    private BookAdapter bookAdapter;
    private BroadcastReceiver brBook;
    private SearchView searchView;

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
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_book);

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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lvListBook.setLayoutManager(manager);
        lvListBook.setAdapter(bookAdapter);

        lvListBook.addOnItemTouchListener(
                new RecyclerItemClickListener(this, lvListBook,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Book book = bookAdapter.getItem(position);
                                int index = listBooks.indexOf(book);
                                showActUpdateBook(index);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                Book book = bookAdapter.getItem(position);
                                int index = listBooks.indexOf(book);
                                showConfirmDeleteBook(index);
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
                if (bookUpdated != null && position != -1) {
                    listBooks.set(position, bookUpdated);
                    bookAdapter.notifyDataSetChanged();
                    Toast.makeText(BookActivity.this, R.string.toast_updated_successfully, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void showActUpdateBook(int position) {
        Intent intent = new Intent(BookActivity.this, BookDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        intent.putExtra("book", bundle);
        startActivity(intent);
    }

    private void showConfirmDeleteBook(final int position) {
        String bookName = listBooks.get(position).book_name;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action_delete) + " " + bookName);
        builder.setMessage(getString(R.string.message_confirm_delete_book));

        builder.setNegativeButton(getString(R.string.action_no), null);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bookDAO.deleteBook(listBooks.get(position));
                listBooks.remove(position);
                bookAdapter.notifyDataSetChanged();
                Toast.makeText(BookActivity.this, R.string.toast_deleted_successfully, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_view, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setQueryHint((getString(R.string.prompt_book_id) + "..."));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

}
