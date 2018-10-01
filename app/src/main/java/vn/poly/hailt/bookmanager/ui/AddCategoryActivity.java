package vn.poly.hailt.bookmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.CategoryDAO;
import vn.poly.hailt.bookmanager.model.Category;

public class AddCategoryActivity extends AppCompatActivity implements Constant{

    private EditText edtCategoryID;
    private EditText edtCategoryName;
    private Button btnAdd;
    private Button btnReset;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        initViews();

        categoryDAO = new CategoryDAO(this);

        initActions();

    }


    private void initViews() {
        edtCategoryID = findViewById(R.id.edtCategoryID);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnAdd = findViewById(R.id.btnAdd);
        btnReset = findViewById(R.id.btnReset);
    }

    private void initActions() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCategoryID.setText(null);
                edtCategoryID.requestFocus();
                edtCategoryName.setText(null);
            }
        });
    }

    private boolean validateForm(String categoryID, String categoryName) {
        if (categoryID.matches("")) {
            edtCategoryID.setError(getString(R.string.notify_empty));
            return false;
        }
        if (categoryID.length() < 3 || categoryID.length() > 10) {
            edtCategoryID.setError(getString(R.string.notify_length_category_id));
            return false;
        }
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

    private void addCategory() {
        String categoryID = edtCategoryID.getText().toString().trim();
        String categoryName = edtCategoryName.getText().toString().trim();

        if (validateForm(categoryID, categoryName)) {
            if (!categoryDAO.checkCategory(categoryID)) {
                Category category = new Category(categoryID, categoryName);
                categoryDAO.insertCategory(category);

                Intent intent = new Intent(ACTION_CATEGORY);
                intent.putExtra("categoryAdded", category);
//                Intent intentAddBookAct = new Intent(ACTION_BOOK);
//                intent.putExtra("categoryAdded", category);
                sendBroadcast(intent);
//                sendBroadcast(intentAddBookAct);
                finish();
            } else {
                TextInputLayout tlNotifyCategoryID = findViewById(R.id.tlNotifyCategoryID);
                tlNotifyCategoryID.setError(getString(R.string.notify_category_id_exists));
                edtCategoryID.requestFocus();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
