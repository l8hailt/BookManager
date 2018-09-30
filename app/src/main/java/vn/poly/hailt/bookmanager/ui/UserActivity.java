package vn.poly.hailt.bookmanager.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import vn.poly.hailt.bookmanager.Constant;
import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.RecyclerItemClickListener;
import vn.poly.hailt.bookmanager.adapter.UserApdater;
import vn.poly.hailt.bookmanager.dao.UserDAO;
import vn.poly.hailt.bookmanager.model.User;

public class UserActivity extends AppCompatActivity implements Constant {

    private FloatingActionButton fabAddUsers;
    private RecyclerView lvListUser;
    private List<User> listUsers;
    private UserApdater userApdater;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initViews();
        initActions();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        userDAO = new UserDAO(this);
        // Xóa user mình login
        listUsers = userDAO.getAllUser();
//        removeUserLogin();
        userApdater = new UserApdater(this, listUsers);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        lvListUser.setLayoutManager(manager);
        lvListUser.setAdapter(userApdater);

        lvListUser.addOnItemTouchListener(
                new RecyclerItemClickListener(this, lvListUser,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                showActionsDialog(position);
                            }
                        }));

    }

//    private void removeUserLogin() {
//        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        String username = pref.getString(KEY_USERNAME, null);
//        User user = userDAO.getUser(username);
//        boolean sta = listUsers.remove(user);
//        Log.e("TAGG", "" + sta);
//    }

    private void showActionsDialog(final int position) {
        CharSequence actions[] = new CharSequence[]{"Sửa", "Xóa"};
        String fullname = listUsers.get(position).getName();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(fullname);
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(UserActivity.this, UserDetailActivity.class);
                    intent.putExtra("position", position);
                    startActivityForResult(intent, REQUETS_CODE_UPDATE_USER);
                } else {
                    showConfirmDeleteUser(position);
                }
            }
        });
        builder.show();
    }

    private void showConfirmDeleteUser(final int position) {
        String fullname = listUsers.get(position).getName();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action_delete) + " " + fullname);
        builder.setMessage(getString(R.string.message_confirm_delete_user));

        builder.setNegativeButton(getString(R.string.action_no), null);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userDAO.deleteUser(listUsers.get(position));
                listUsers.remove(position);
                Toast.makeText(UserActivity.this, R.string.toast_deleted_successfully, Toast.LENGTH_SHORT).show();
                userApdater.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        fabAddUsers = findViewById(R.id.fabAddUser);
        lvListUser = findViewById(R.id.lvListUser);
    }

    private void initActions() {
        fabAddUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(UserActivity.this, AddUserActivity.class),
                        REQUEST_CODE_ADD_USER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_USER) {
            if (resultCode == RESULT_OK) {
                User user = data.getParcelableExtra("user");
                listUsers.add(user);
                userApdater.notifyDataSetChanged();
                Toast.makeText(UserActivity.this, R.string.toast_added_successfully, Toast.LENGTH_SHORT).show();
            } else {
                Log.e("onActivityResult", "Not data");
            }
        }
        if (requestCode == REQUETS_CODE_UPDATE_USER) {
            if (resultCode == RESULT_OK) {
                User user = data.getParcelableExtra("user");
                int position = data.getIntExtra("position", -1);
                listUsers.set(position, user);
                userApdater.notifyDataSetChanged();
                Toast.makeText(UserActivity.this, R.string.toast_updated_successfully, Toast.LENGTH_SHORT).show();
            } else {
                Log.e("onActivityResult", "Not data");
            }
        }
    }
}
