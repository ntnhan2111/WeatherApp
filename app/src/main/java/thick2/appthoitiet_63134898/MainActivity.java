package thick2.appthoitiet_63134898;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etCity;
    private Button btnSearch, btnGoToMap, btnGoToRank, btnGoToChart, btnGoToAi;
    private TextView tvRecentCity;

    private final String SHARED_PREFS = "weather_prefs";
    private final String KEY_CITY = "last_searched_city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);
        tvRecentCity = findViewById(R.id.tvRecentCity);

        btnGoToMap = findViewById(R.id.btnMap);
        btnGoToRank = findViewById(R.id.btnRank);
        btnGoToChart = findViewById(R.id.btnChart);
        btnGoToAi = findViewById(R.id.btnPredictAI);

        loadLastCity();

        btnSearch.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (!city.isEmpty()) {
                saveLastCity(city);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("SEARCH_KEY", city);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập tên thành phố!", Toast.LENGTH_SHORT).show();
            }
        });

        if (btnGoToMap != null) btnGoToMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("MAP_CITY_KEY", etCity.getText().toString().trim());
            startActivity(intent);
        });

        if (btnGoToRank != null) btnGoToRank.setOnClickListener(v -> {
            Intent intent = new Intent(this, RankActivity.class);
            intent.putExtra("RANK_CITY_KEY", etCity.getText().toString().trim());
            startActivity(intent);
        });

        if (btnGoToChart != null) btnGoToChart.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChartActivity.class);
            intent.putExtra("CHART_CITY_KEY", etCity.getText().toString().trim());
            startActivity(intent);
        });

        if (btnGoToAi != null) btnGoToAi.setOnClickListener(v -> {
            Intent intent = new Intent(this, AiTravelActivity.class);
            intent.putExtra("CITY_KEY", etCity.getText().toString().trim());
            startActivity(intent);
        });
    }

    private void saveLastCity(String city) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CITY, city);
        editor.apply();
        tvRecentCity.setText("Thành phố vừa tìm: " + city);
    }

    private void loadLastCity() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String lastCity = sharedPreferences.getString(KEY_CITY, "Hanoi");
        etCity.setText(lastCity);
        tvRecentCity.setText("Thành phố vừa tìm: " + lastCity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLastCity();
    }
}
