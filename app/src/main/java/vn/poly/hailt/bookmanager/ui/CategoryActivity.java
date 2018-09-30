package vn.poly.hailt.bookmanager.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import vn.poly.hailt.bookmanager.adapter.CategoryAdapter;
import vn.poly.hailt.bookmanager.dao.CategoryDAO;
import vn.poly.hailt.bookmanager.model.Category;

public class CategoryActivity extends AppCompatActivity implements Constant {

    private Toolbar toolbar;
    private FloatingActionButton fabAddCategories;
    private RecyclerView lvListCategory;
    private List<Category> listCategories;
    private CategoryAdapter categoryAdapter;
    private CategoryDAO categoryDAO;
    private BroadcastReceiver brCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initViews();
        initActions();

        categoryDAO = new CategoryDAO(this);
        listCategories = categoryDAO.getAllCategory();

        categoryAdapter = new CategoryAdapter(this, listCategories);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lvListCategory.setLayoutManager(manager);
        lvListCategory.setAdapter(categoryAdapter);

        lvListCategory.addOnItemTouchListener(
                new RecyclerItemClickListener(this, lvListCategory,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                showActionsDialog(position);
                            }
                        }));

        brCategory = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Category categoryAdded = intent.getParcelableExtra("categoryAdded");
                if (categoryAdded != null) {
                    listCategories.add(categoryAdded);
                    categoryAdapter.notifyDataSetChanged();
                    Toast.makeText(CategoryActivity.this, R.string.toast_added_successfully, Toast.LENGTH_SHORT).show();
                }
                int position = intent.getIntExtra("position", -1);
                Category categoryUpdated = intent.getParcelableExtra("categoryUpdated");
                if (categoryUpdated != null && position != -1) {
                    listCategories.set(position, categoryUpdated);
                    categoryAdapter.notifyDataSetChanged();
                    Toast.makeText(CategoryActivity.this, R.string.toast_updated_successfully, Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        fabAddCategories = findViewById(R.id.fabAddCategories);
        lvListCategory = findViewById(R.id.lvCategories);
    }

    private void initActions() {
        fabAddCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, AddCategoryActivity.class));
            }
        });
    }


    private void showActionsDialog(final int position) {
        CharSequence actions[] = new CharSequence[]{"Sửa", "Xóa"};
        String categoryName = listCategories.get(position).getCategory_name();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(categoryName);
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(CategoryActivity.this, CategoryDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    intent.putExtra("category", bundle);
                    startActivity(intent);
                } else {
                    showConfirmDeleteCategory(position);
                }
            }
        });
        builder.show();
    }

    private void showConfirmDeleteCategory(final int position) {
        String categoryName = listCategories.get(position).getCategory_name();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action_delete) + " " + categoryName);
        builder.setMessage(getString(R.string.message_confirm_delete_category));

        builder.setNegativeButton(getString(R.string.action_no), null);
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryDAO.deleteCategory(listCategories.get(position));
                listCategories.remove(position);
                Toast.makeText(CategoryActivity.this, R.string.toast_deleted_successfully, Toast.LENGTH_SHORT).show();
                categoryAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ACTION_CATEGORY);
        registerReceiver(brCategory, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "onDestroy");
        unregisterReceiver(brCategory);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
