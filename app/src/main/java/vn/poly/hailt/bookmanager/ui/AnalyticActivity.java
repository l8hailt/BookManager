package vn.poly.hailt.bookmanager.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import vn.poly.hailt.bookmanager.R;
import vn.poly.hailt.bookmanager.dao.StatisticDAO;

public class AnalyticActivity extends AppCompatActivity {

    private TextView tvStatisticsByDay;
    private TextView tvStatisticsByMonth;
    private TextView tvStatisticsByYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytic);

        StatisticDAO statisticDAO = new StatisticDAO(this);

        initViews();

        double totalDay = statisticDAO.getStatisticByDay();
        double totalMonth = statisticDAO.getStatisticByMonth();
        double totalYear = statisticDAO.getStatisticByYear();

        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        String currencyTotalDay = fmt.format(totalDay);
        String currencyTotalMonth = fmt.format(totalMonth);
        String currencyTotalYear = fmt.format(totalYear);

        tvStatisticsByDay.setText((getString(R.string.label_today) + ": " + currencyTotalDay));
        tvStatisticsByMonth.setText((getString(R.string.label_this_month) + ": " + currencyTotalMonth));
        tvStatisticsByYear.setText((getString(R.string.label_this_year) + ": " + currencyTotalYear));

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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
