package vn.poly.hailt.bookmanager.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.adapter.BookAdapter;
import vn.poly.hailt.bookmanager.dao.BookDAO;
import vn.poly.hailt.bookmanager.model.Book;

public class BookOfCategoryActivity extends AppCompatActivity {

    private RecyclerView lvBooks;
    private BookDAO bookDAO;
    private List<Book> listBooks;
    private BookAdapter bookAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_of_category);

        initViews();
        bookDAO = new BookDAO(this);
        setUpRecyclerView();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getIntent().getStringExtra("categoryName"));
        lvBooks = findViewById(R.id.lvBooks);
    }

    private void setUpRecyclerView() {
        String categoryID = getIntent().getStringExtra("categoryID");
        listBooks = bookDAO.getAllBookOfCategory(categoryID);
        bookAdapter = new BookAdapter(this, listBooks);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lvBooks.setLayoutManager(manager);
        lvBooks.setAdapter(bookAdapter);
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

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
