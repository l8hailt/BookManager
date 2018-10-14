package vn.poly.hailt.bookmanager.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.UserDAO;


public class LoginActivity extends AppCompatActivity implements Constant {

    private EditText edtUsername;
    private EditText edtPassword;
    private CheckBox cbRememberPassword;
    private Button btnLogin;
    private Button btnExit;
    private TextInputLayout tlNotifyUsername;
    private TextInputLayout tlNotifyPassword;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initActions();
        restorePref();
        userDAO = new UserDAO(this);

    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        cbRememberPassword = findViewById(R.id.cbRememberPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);
        tlNotifyUsername = findViewById(R.id.tlNotifyUsername);
        tlNotifyPassword = findViewById(R.id.tlNotifyPassword);
    }

    private void initActions() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                checkLogin(username, password);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void checkLogin(String username, String password) {

        if (username.equals("admin") && password.equals("admin")) {
            rememberUser(username, password, cbRememberPassword.isChecked());
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }

        if (userDAO.checkUser(username)) {
            tlNotifyUsername.setErrorEnabled(true);
            tlNotifyUsername.setError(getString(R.string.notify_username_not_exists));
            return;
        } else {
            tlNotifyUsername.setError(null);
            tlNotifyUsername.setErrorEnabled(false);
        }

        if (!userDAO.checkUserPassword(username, password)) {
            tlNotifyPassword.setErrorEnabled(true);
            tlNotifyPassword.setError(getString(R.string.notify_password_wrong));
        } else {
            tlNotifyPassword.setError(null);
            tlNotifyPassword.setErrorEnabled(false);
            rememberUser(username, password, cbRememberPassword.isChecked());
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }

    }

    private void rememberUser(String username, String password, boolean status) {
        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!status) {
            editor.clear();
            editor.putString(KEY_USERNAME, username);
        } else {
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.putBoolean(IS_REMEMBER, true);
        }
        editor.apply();
    }

    private void restorePref() {
        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isRemember = pref.getBoolean(IS_REMEMBER, false);
        if (isRemember) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//            Bundle bundle = new Bundle();
            intent.putExtra("username", "admin");
//            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

}

