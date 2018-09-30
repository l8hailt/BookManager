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

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.UserDAO;
import vn.poly.hailt.bookmanager.model.User;

public class UserDetailActivity extends AppCompatActivity {

    private EditText edtFullName;
    private EditText edtPhoneNumber;
    private Button btnEdit;
    private Button btnReset;
    private TextView tvUsername;
    private UserDAO userDAO;
    private List<User> listUsers;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initViews();
        initActions();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        userDAO = new UserDAO(this);
        listUsers = userDAO.getAllUser();

        position = getIntent().getIntExtra("position", -1);
        String username = listUsers.get(position).getUser_name();
        String fullname = listUsers.get(position).getName();
        String phoneNumber = listUsers.get(position).getPhone_number();
        tvUsername.setText(username);
        edtFullName.setText(fullname);
        edtPhoneNumber.setText(phoneNumber);

    }

    private void initViews() {
        edtFullName = findViewById(R.id.edtFullName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnEdit = findViewById(R.id.btnEdit);
        btnReset = findViewById(R.id.btnReset);
        tvUsername = findViewById(R.id.tvUsername);
    }

    private void initActions() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPhoneNumber.setText("");
                edtFullName.setText("");
            }
        });
    }

    private void updateUser() {
        String fullname = edtFullName.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();

        if (validateForm(fullname, phoneNumber)) {
            User u = listUsers.get(position);

            u.setName(fullname);
            u.setPhone_number(phoneNumber);

            userDAO.updateUser(u);
            Intent intent = new Intent();
            intent.putExtra("user", u);
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private boolean validateForm(String fullname, String phoneNumber) {
        if (fullname.matches("")) {
            edtFullName.setError(getString(R.string.notify_empty));
            return false;
        }
        if (phoneNumber.matches("")) {
            edtPhoneNumber.setError(getString(R.string.notify_empty));
            return false;
        }

        return true;
    }
}
