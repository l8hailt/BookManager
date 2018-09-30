package vn.poly.hailt.bookmanager.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.UserDAO;
import vn.poly.hailt.bookmanager.model.User;

/**
 * - đổi "com.example" -> khác
 * - tên package a-z.
 */

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constant {

    private ImageView imgUser;
    private ImageView imgCategory;
    private ImageView imgBook;
    private ImageView imgOrder;
    private ImageView imgBestSeller;
    private ImageView imgAnalytic;
    private TextView tvFullName;
    private TextView tvPhoneNumber;
    boolean doubleBackToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initActions();

        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        tvFullName = headerView.findViewById(R.id.tvFullName);
        tvPhoneNumber = headerView.findViewById(R.id.tvPhoneNumber);

        UserDAO userDAO = new UserDAO(this);

        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String username = pref.getString(KEY_USERNAME, null);
        if (username != null) {
            if (username.equals("admin")) {
                tvFullName.setText(getString(R.string.admin));
                tvPhoneNumber.setText(getString(R.string.admin));
            } else {
                User user = userDAO.getUser(username);
                String fullname = user.getName();
                String phoneNumber = user.getPhone_number();
                tvFullName.setText(fullname);
                tvPhoneNumber.setText(phoneNumber);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExit) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExit = true;
            Toast.makeText(this, getString(R.string.action_double_back_to_exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExit = false;
                }
            }, 2000);
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_password) {
            startActivity(new Intent(HomeActivity.this, ChangePasswordActivity.class));
        } else if (id == R.id.nav_logout) {
            showConfirmLogoutDialog();
        } else if (id == R.id.nav_exit) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initViews() {
        imgUser = findViewById(R.id.imgUser);
        imgCategory = findViewById(R.id.imgCategory);
        imgBook = findViewById(R.id.imgBook);
        imgOrder = findViewById(R.id.imgOrder);
        imgBestSeller = findViewById(R.id.imgBestSeller);
        imgAnalytic = findViewById(R.id.imgAnalytic);
    }

    public void initActions() {
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserActivity.class));
            }
        });
        imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CategoryActivity.class));
            }
        });
        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BookActivity.class));
            }
        });
        imgOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BillActivity.class));
            }
        });
        imgBestSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BestSellerActivity.class));
            }
        });
        imgAnalytic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AnalyticActivity.class));
            }
        });
    }

    private void showConfirmLogoutDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.action_logout));
        builder.setMessage(getString(R.string.message_confirm_logout));
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.action_no), null);
        builder.show();
    }

}
