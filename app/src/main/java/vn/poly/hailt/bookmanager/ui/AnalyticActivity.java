package vn.poly.hailt.bookmanager.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.StatisticDAO;

public class AnalyticActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvStatisticsByDay;
    private TextView tvStatisticsByMonth;
    private TextView tvStatisticsByYear;
    private StatisticDAO statisticDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytic);

        statisticDAO = new StatisticDAO(this);

        initViews();

        tvStatisticsByDay.setText(String.valueOf(statisticDAO.getStatisticByDay()));
        tvStatisticsByMonth.setText(String.valueOf(statisticDAO.getStatisticByMonth()));
        tvStatisticsByYear.setText(String.valueOf(statisticDAO.getStatisticByYear()));

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        tvStatisticsByDay = findViewById(R.id.tvStatisticsByDay);
        tvStatisticsByMonth = findViewById(R.id.tvStatisticsByMonth);
        tvStatisticsByYear = findViewById(R.id.tvStatisticsByYear);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
