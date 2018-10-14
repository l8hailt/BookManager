package vn.poly.hailt.bookmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.UserDAO;
import vn.poly.hailt.bookmanager.model.User;

public class AddUserActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtRePassword;
    private EditText edtPhoneNumber;
    private EditText edtFullName;
    private Button btnAdd;
    private Button btnReset;
    private UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initViews();

        userDAO = new UserDAO(this);

        initActions();

    }

    private void initActions() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtUsername.setText("");
                edtUsername.requestFocus();
                edtPassword.setText("");
                edtRePassword.setText("");
                edtPhoneNumber.setText("");
                edtFullName.setText("");
            }
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtFullName = findViewById(R.id.edtFullName);
        btnAdd = findViewById(R.id.btnAdd);
        btnReset = findViewById(R.id.btnReset);
    }

    private boolean validateForm(String username, String password, String rePassword, String fullname, String phoneNumber) {
        if (username.matches("")) {
            edtUsername.setError(getString(R.string.notify_empty));
            return false;
        }
        if (username.length() < 6) {
            edtPassword.setError(getString(R.string.notify_min_length_user_name));
            return false;
        }
        if (password.matches("")) {
            edtPassword.setError(getString(R.string.notify_empty));
            return false;
        }
        if (password.length() < 6) {
            edtPassword.setError(getString(R.string.notify_min_length_pass_word));
            return false;
        }
        if (rePassword.matches("")) {
            edtRePassword.setError(getString(R.string.notify_empty));
            return false;
        }
        if (fullname.matches("")) {
            edtFullName.setError(getString(R.string.notify_empty));
            return false;
        }
        if (phoneNumber.matches("")) {
            edtPhoneNumber.setError(getString(R.string.notify_empty));
            return false;
        }
        if (!rePassword.equals(password)) {
            edtRePassword.setError(getString(R.string.notify_re_password_dont_match));
            return false;
        }

        return true;
    }

    private void addUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String rePassword = edtRePassword.getText().toString().trim();
        String fullname = edtFullName.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();


        if (validateForm(username, password, rePassword, fullname, phoneNumber)) {
            if (userDAO.checkUser(username)) {
                User user = new User(username, password, fullname, phoneNumber);
                userDAO.insertUser(user);
                Intent intent = new Intent();
                intent.putExtra("user", user);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                TextInputLayout tlNotifyUsername = findViewById(R.id.tlNotifyUsername);
                tlNotifyUsername.setError(getString(R.string.notify_username_exists));
                edtUsername.requestFocus();
            }
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
}
