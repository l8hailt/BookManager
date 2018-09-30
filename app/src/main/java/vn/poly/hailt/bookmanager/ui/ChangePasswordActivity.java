package vn.poly.hailt.bookmanager.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.UserDAO;
import vn.poly.hailt.bookmanager.model.User;


public class ChangePasswordActivity extends AppCompatActivity implements Constant {

    private EditText edtCurrentPassword;
    private EditText edtNewPassword;
    private EditText edtRePassword;
    private Button btnSave;
    private Button btnReset;
    private TextInputLayout tlNotifyCurrentPassword;
    private TextInputLayout tlNotifyNewPassword;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        initViews();
        initActionns();

        userDAO = new UserDAO(this);


    }

    private void initActionns() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = edtCurrentPassword.getText().toString().trim();
                String newPassword = edtNewPassword.getText().toString().trim();
                String reNewPassword = edtRePassword.getText().toString().trim();

                changePasswordUser(currentPassword, newPassword, reNewPassword);

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCurrentPassword.setText("");
                edtNewPassword.setText("");
                edtRePassword.setText("");
            }
        });
    }

    private void initViews() {
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);
        tlNotifyCurrentPassword = findViewById(R.id.tlNotifyCurrentPassword);
        tlNotifyNewPassword = findViewById(R.id.tlNotifyNewPassword);
    }

    private void changePasswordUser(String currentPassword, String newPassword, String reNewPassword) {
        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String username = pref.getString(KEY_USERNAME, null);
        User u = userDAO.getUser(username);
        String password = u.getPassword();
        Log.e("TAGG", username);
        Log.e("TAGG", password);
        if (!currentPassword.equals(password)) {
            tlNotifyCurrentPassword.setErrorEnabled(true);
            tlNotifyCurrentPassword.setError(getString(R.string.notify_password_wrong));
        } else {
            tlNotifyCurrentPassword.setError(null);
            tlNotifyCurrentPassword.setErrorEnabled(false);
            if (validateForm(currentPassword, newPassword, reNewPassword)) {
                u.setPassword(newPassword);
                userDAO.changedPasswordUser(u);
                Toast.makeText(ChangePasswordActivity.this,
                        R.string.changed_password_successful, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean validateForm(String currentPassword, String newPassword, String reNewPassword) {
        if (newPassword.matches("")) {
            edtNewPassword.setError(getString(R.string.notify_empty));
            return false;
        }
        if (newPassword.length() < 6) {
            edtNewPassword.setError(getString(R.string.notify_min_length_pass_word));
            return false;
        }
        if (reNewPassword.matches("")) {
            edtRePassword.setError(getString(R.string.notify_empty));
            return false;
        }
        if (reNewPassword.length() < 6) {
            edtRePassword.setError(getString(R.string.notify_min_length_pass_word));
            return false;
        }
        if (!reNewPassword.matches(newPassword)) {
            edtRePassword.setError(getString(R.string.notify_re_password_dont_match));
            return false;
        }
        if (currentPassword.equals(newPassword)) {
            tlNotifyNewPassword.setErrorEnabled(true);
            tlNotifyNewPassword.setError(getString(R.string.notify_new_password_not_different));
            return false;
        } else {
            tlNotifyNewPassword.setError(null);
            tlNotifyNewPassword.setErrorEnabled(false);
        }


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}

