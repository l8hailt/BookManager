package vn.poly.hailt.bookmanager.ui;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import java.util.List;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.BookDAO;
import vn.poly.hailt.bookmanager.dao.CategoryDAO;
import vn.poly.hailt.bookmanager.model.Book;
import vn.poly.hailt.bookmanager.model.Category;

public class BookDetailActivity extends AppCompatActivity implements Constant {

    private TextView tvCategoryID;
    private Spinner spnBookCategory;
    private ImageView imgAddCategory;
    private EditText edtBookName;
    private EditText edtAuthor;
    private EditText edtPublisher;
    private EditText edtPrice;
    private EditText edtQuantity;
    private Button btnEdit;
    private Button btnReset;
    private BookDAO bookDAO;
    private CategoryDAO categoryDAO;
    private List<Book> listBooks;
    private List<Category> listCategories;
    private ArrayAdapter<Category> categoryAdapter;
    private int position;
    private String categoryIDSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initViews();
        initActions();

        bookDAO = new BookDAO(this);
        categoryDAO = new CategoryDAO(this);
        listBooks = bookDAO.getAllBook();

        setUpSpinner();

        setData();

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        tvCategoryID = findViewById(R.id.tvCategoryID);
        spnBookCategory = findViewById(R.id.spnBookCategory);
        imgAddCategory = findViewById(R.id.imgAddCategory);
        edtBookName = findViewById(R.id.edtBookName);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtPublisher = findViewById(R.id.edtPublisher);
        edtPrice = findViewById(R.id.edtPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        btnEdit = findViewById(R.id.btnEdit);
        btnReset = findViewById(R.id.btnReset);
    }

    private void initActions() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtBookName.setText(null);
                edtBookName.requestFocus();
                edtAuthor.setText(null);
                edtPublisher.setText(null);
                edtPrice.setText(null);
                edtQuantity.setText(null);
            }
        });
        imgAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookDetailActivity.this, AddCategoryActivity.class));
            }
        });
    }

    private void updateBook() {
        String bookName = edtBookName.getText().toString().trim();
        String author = edtAuthor.getText().toString().trim();
        String publisher = edtPublisher.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String quantity = edtQuantity.getText().toString().trim();


        if (validateForm(bookName, price, quantity)) {
            Book book = listBooks.get(position);
            book.category_id = categoryIDSelected;
            book.book_name = bookName;
            book.author = author;
            book.publisher = publisher;
            book.price = Double.parseDouble(price);
            book.quantity = Integer.parseInt(quantity);
            bookDAO.updateBook(book);

            Intent intent = new Intent(ACTION_BOOK);
            intent.putExtra("position", position);
            intent.putExtra("bookUpdated", book);
            sendBroadcast(intent);
            finish();
        }

    }

    private int checkPositionCategory(String categoryID) {
        for (int i = 0; i < listCategories.size(); i++) {
            if (categoryID.equals(listCategories.get(i).getCategory_id())) {
                Log.e("I", i + "");
                return i;
            }
        }
        return 0;
    }

    private void setUpSpinner() {
        listCategories = categoryDAO.getAllCategory();
        categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBookCategory.setAdapter(categoryAdapter);

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

    private void setData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("book");
        if (bundle != null) {
            position = bundle.getInt("position");
            String bookID = listBooks.get(position).book_id;
            String bookName = listBooks.get(position).book_name;
            String author = listBooks.get(position).author;
            String publisher = listBooks.get(position).publisher;
            String price = String.valueOf(listBooks.get(position).price);
            String quantity = String.valueOf(listBooks.get(position).quantity);
            tvCategoryID.setText(bookID);
            edtBookName.setText(bookName);
            edtAuthor.setText(author);
            edtPublisher.setText(publisher);
            edtPrice.setText(price);
            edtQuantity.setText(quantity);
        }
    }

    private boolean validateForm(String bookName, String price, String quantity) {
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
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listCategories = categoryDAO.getAllCategory();
        categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBookCategory.setAdapter(categoryAdapter);

        String categoryID = listBooks.get(position).category_id;
        spnBookCategory.setSelection(checkPositionCategory(categoryID));
    }
}
