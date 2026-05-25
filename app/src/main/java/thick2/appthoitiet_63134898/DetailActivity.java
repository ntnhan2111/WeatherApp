package thick2.appthoitiet_63134898;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import thick2.appthoitiet_63134898.ApiConfig;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DetailActivity extends AppCompatActivity {

    private TextView tvSearchedCity;
    private TextView tvCityNameResult;
    private TextView tvTempResult;
    private TextView tvDetailsResult;
    private TextView tvForecastListResult;
    private TextView tvBackBtn;

    private FloatingActionButton fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ánh xạ View
        tvSearchedCity = findViewById(R.id.tvSearchedCity);
        tvCityNameResult = findViewById(R.id.tvCityNameResult);
        tvTempResult = findViewById(R.id.tvTempResult);
        tvDetailsResult = findViewById(R.id.tvDetailsResult);
        tvForecastListResult = findViewById(R.id.tvForecastListResult);
        tvBackBtn = findViewById(R.id.tvBackBtn);
        fabMenu = findViewById(R.id.fabMenu);

        // Nút quay lại
        tvBackBtn.setOnClickListener(v -> finish());

        // Lấy tên thành phố
        String city =
                getIntent().getStringExtra("SEARCH_KEY");

        if (city == null || city.isEmpty()) {
            city = "Hanoi";
        }

        tvSearchedCity.setText(city);

        // Load dữ liệu thời tiết
        loadWeatherData(city);

        // Floating Button
        String finalCity = city;
        fabMenu.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            DetailActivity.this,
                            RankActivity.class
                    );

            intent.putExtra(
                    "RANK_CITY_KEY",
                    finalCity
            );

            startActivity(intent);

        });
    }

    private void loadWeatherData(String city) {

        RequestQueue queue =
                Volley.newRequestQueue(this);

        String url = "";

        try {

            url =
                    "https://api.weatherapi.com/v1/current.json?key="
                            + ApiConfig.WEATHER_API_KEY
                            + "&q="
                            + java.net.URLEncoder.encode(city, "UTF-8")
                            + "&lang=vi";

        } catch (Exception e) {

            e.printStackTrace();
        }

        // Kiểm tra URL
        Log.d("API_URL", url);

        JsonObjectRequest request =
                new JsonObjectRequest(

                        Request.Method.GET,
                        url,
                        null,

                        response -> {

                            try {

                                // LOCATION
                                JSONObject location =
                                        response.getJSONObject("location");

                                String cityName =
                                        location.getString("name");

                                String country =
                                        location.getString("country");

                                // CURRENT
                                JSONObject current =
                                        response.getJSONObject("current");

                                double temp =
                                        current.getDouble("temp_c");

                                int humidity =
                                        current.getInt("humidity");

                                double wind =
                                        current.getDouble("wind_kph");

                                // CONDITION
                                JSONObject condition =
                                        current.getJSONObject("condition");

                                String desc =
                                        condition.getString("text");

                                // HIỂN THỊ
                                tvCityNameResult.setText(
                                        cityName + ", " + country
                                );

                                tvTempResult.setText(
                                        Math.round(temp) + "°C"
                                );

                                tvDetailsResult.setText(
                                        "Trạng thái: " + desc
                                                + "\nĐộ ẩm: " + humidity + "%"
                                                + "\nTốc độ gió: " + wind + " km/h"
                                );

                                tvForecastListResult.setText(
                                        "Tải dữ liệu thời tiết thành công."
                                );

                            } catch (JSONException e) {

                                e.printStackTrace();

                                tvCityNameResult.setText(
                                        "Lỗi đọc dữ liệu!"
                                );

                                tvTempResult.setText("--°C");

                                tvDetailsResult.setText("-");
                            }

                        },

                        error -> {

                            error.printStackTrace();

                            if (error.networkResponse != null) {

                                Log.e(
                                        "API_ERROR_CODE",
                                        String.valueOf(
                                                error.networkResponse.statusCode
                                        )
                                );
                            }

                            Log.e(
                                    "API_ERROR",
                                    error.toString()
                            );

                            tvCityNameResult.setText(
                                    "Không tải được dữ liệu!"
                            );

                            tvTempResult.setText("--°C");

                            tvDetailsResult.setText("-");

                            tvForecastListResult.setText(
                                    "Lỗi kết nối API."
                            );
                        }

                );

        queue.add(request);
    }
}