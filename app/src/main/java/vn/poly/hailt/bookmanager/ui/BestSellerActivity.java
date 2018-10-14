package vn.poly.hailt.bookmanager.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.adapter.BookAdapter;
import vn.poly.hailt.bookmanager.dao.StatisticDAO;
import vn.poly.hailt.bookmanager.model.Book;

public class BestSellerActivity extends AppCompatActivity {

    private Spinner spnPickMonth;
    private RecyclerView lvBestSeller;
    private StatisticDAO statisticDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_seller);

        statisticDAO = new StatisticDAO(this);

        initViews();
        setMaxHeightPopupSpinner();
        setUpMonthSpinner();

        spnPickMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    switch (position) {
                        case 1:
                            setUpRecyclerView(String.valueOf(1));
                            break;
                        case 2:
                            setUpRecyclerView(String.valueOf(2));
                            break;
                        case 3:
                            setUpRecyclerView(String.valueOf(3));
                            break;
                        case 4:
                            setUpRecyclerView(String.valueOf(4));
                            break;
                        case 5:
                            setUpRecyclerView(String.valueOf(5));
                            break;
                        case 6:
                            setUpRecyclerView(String.valueOf(6));
                            break;
                        case 7:
                            setUpRecyclerView(String.valueOf(7));
                            break;
                        case 8:
                            setUpRecyclerView(String.valueOf(8));
                            break;
                        case 9:
                            setUpRecyclerView(String.valueOf(9));
                            break;
                        case 10:
                            setUpRecyclerView(String.valueOf(10));
                            break;
                        case 11:
                            setUpRecyclerView(String.valueOf(11));
                            break;
                        case 12:
                            setUpRecyclerView(String.valueOf(12));
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        spnPickMonth = findViewById(R.id.spnPickMonth);
        lvBestSeller = findViewById(R.id.lvBestSeller);
    }

    private void setMaxHeightPopupSpinner() {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spnPickMonth);

            // Set popupWindow height to 500px
            popupWindow.setHeight(600);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

    private void setUpMonthSpinner() {
        List<String> months = new ArrayList<>();
        months.add("Chọn tháng...");
        for (int i = 1; i < 13; i++) {
            months.add("Tháng " + i);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, months) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPickMonth.setAdapter(spinnerArrayAdapter);
    }

    private void setUpRecyclerView(String month) {
        List<Book> listBooks = statisticDAO.getBookTop10(month);
        BookAdapter bookAdapter = new BookAdapter(BestSellerActivity.this, listBooks);
        LinearLayoutManager manager = new LinearLayoutManager(BestSellerActivity.this);
        lvBestSeller.setLayoutManager(manager);
        lvBestSeller.setAdapter(bookAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
