package vn.poly.hailt.bookmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.List;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.BookDAO;
import vn.poly.hailt.bookmanager.dao.CategoryDAO;
import vn.poly.hailt.bookmanager.model.Book;
import vn.poly.hailt.bookmanager.model.Category;

public class AddBookActivity extends AppCompatActivity implements Constant {

    private Toolbar toolbar;
    private EditText edtBookID;
    private Spinner spnBookCategory;
    private ImageView imgAddCategory;
    private EditText edtBookName;
    private EditText edtAuthor;
    private EditText edtPublisher;
    private EditText edtPrice;
    private EditText edtQuantity;
    private Button btnAdd;
    private Button btnReset;
    private BookDAO bookDAO;
    private CategoryDAO categoryDAO;
    private List<Category> listCategories;
    private String categoryIDSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        initViews();
        initActions();

        bookDAO = new BookDAO(this);
        categoryDAO = new CategoryDAO(this);

        listCategories = categoryDAO.getAllCategory();

        setUpSpinner();

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        edtBookID = findViewById(R.id.edtBookID);
        spnBookCategory = findViewById(R.id.spnBookCategory);
        imgAddCategory = findViewById(R.id.imgAddCategory);
        edtBookName = findViewById(R.id.edtBookName);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtPublisher = findViewById(R.id.edtPublisher);
        edtPrice = findViewById(R.id.edtPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        btnAdd = findViewById(R.id.btnAdd);
        btnReset = findViewById(R.id.btnReset);
    }

    private void initActions() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtBookID.setText(null);
                edtBookID.requestFocus();
                edtBookName.setText(null);
                edtAuthor.setText(null);
                edtPublisher.setText(null);
                edtPrice.setText(null);
                edtQuantity.setText(null);
            }
        });
        imgAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBookActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addBook() {
        String bookID = edtBookID.getText().toString().trim();
        String bookName = edtBookName.getText().toString().trim();
        String author = edtAuthor.getText().toString().trim();
        String publisher = edtPublisher.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String quantity = edtQuantity.getText().toString().trim();

        if (validateForm(bookID, bookName, price, quantity)) {
            if (!bookDAO.checkBook(bookID)) {
                Book book = new Book(bookID, categoryIDSelected, bookName,
                        author, publisher, Double.parseDouble(price), Integer.parseInt(quantity));
                bookDAO.insertBook(book);

                Intent intent = new Intent(ACTION_BOOK);
                intent.putExtra("bookAdded", book);
                sendBroadcast(intent);
                finish();
            } else {
                TextInputLayout tlNotifyBookID = findViewById(R.id.tlNotifyBookID);
                tlNotifyBookID.setError(getString(R.string.notify_book_id_exists));
                edtBookID.requestFocus();
            }
        }
    }

    private void setUpSpinner() {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBookCategory.setAdapter(adapter);

        spnBookCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryIDSelected = listCategories.get(position).getCategory_id();
                Log.e("categorySelectedID", categoryIDSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validateForm(String bookID, String bookName, String price, String quantity) {
        if (bookID.matches("")) {
            edtBookID.setError(getString(R.string.notify_empty));
            return false;
        }
        if (bookID.length() < 3 || bookID.length() > 10) {
            edtBookID.setError(getString(R.string.notify_length_book_id));
            return false;
        }
        if (bookName.matches("")) {
            edtBookName.setError(getString(R.string.notify_empty));
            return false;
        }
        if (bookName.length() > 50) {
            edtBookName.setError(getString(R.string.notify_max_length_book_name));
            return false;
        }
        if (price.matches("")) {
            edtPrice.setError(getString(R.string.notify_empty));
            return false;
        }
        if (quantity.matches("")) {
            edtQuantity.setError(getString(R.string.notify_empty));
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

}
