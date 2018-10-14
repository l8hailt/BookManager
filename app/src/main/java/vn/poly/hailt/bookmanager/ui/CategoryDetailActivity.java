package vn.poly.hailt.bookmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.CategoryDAO;
import vn.poly.hailt.bookmanager.model.Category;

public class CategoryDetailActivity extends AppCompatActivity implements Constant {

    private TextView tvCategoryID;
    private EditText edtCategoryName;
    private Button btnEdit;
    private Button btnReset;
    private CategoryDAO categoryDAO;
    private List<Category> listCategories;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        initViews();
        initActions();

        categoryDAO = new CategoryDAO(this);
        listCategories = categoryDAO.getAllCategory();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("category");
        if (bundle != null) {
            position = bundle.getInt("position");
            String categoryID = listCategories.get(position).getCategory_id();
            String categoryName = listCategories.get(position).getCategory_name();
            tvCategoryID.setText(categoryID);
            edtCategoryName.setText(categoryName);
        }

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        tvCategoryID = findViewById(R.id.tvCategoryID);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnEdit = findViewById(R.id.btnEdit);
        btnReset = findViewById(R.id.btnReset);
    }

    private void initActions() {

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCategory();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCategoryName.setText(null);
            }
        });
    }

    private void updateCategory() {
        String categoryName = edtCategoryName.getText().toString().trim();

        if (validateForm(categoryName)) {
            Category category = listCategories.get(position);
            category.setCategory_name(categoryName);
            categoryDAO.updateCategory(category);

            Intent intent = new Intent(ACTION_CATEGORY);
            intent.putExtra("position", position);
            intent.putExtra("categoryUpdated", category);
            sendBroadcast(intent);
            finish();
        }

    }

    private boolean validateForm(String categoryName) {
        if (categoryName.matches("")) {
            edtCategoryName.setError(getString(R.string.notify_empty));
            return false;
        }
        if (categoryName.length() > 50) {
            edtCategoryName.setError(getString(R.string.notify_max_length_category_name));
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
