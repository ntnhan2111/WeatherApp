package thick2.appthoitiet_63134898;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // Ánh xạ LineChart
        lineChart = findViewById(R.id.lineChart);

        // Gọi dữ liệu biểu đồ
        loadChartData("Hanoi");
    }

    private void loadChartData(String city) {

        String url =
                "https://api.weatherapi.com/v1/forecast.json?key="
                        + ApiConfig.WEATHER_API_KEY
                        + "&q="
                        + city
                        + "&days=7"
                        + "&lang=vi";

        Log.d("CHART_URL", url);

        RequestQueue queue =
                Volley.newRequestQueue(this);

        JsonObjectRequest request =
                new JsonObjectRequest(

                        Request.Method.GET,
                        url,
                        null,

                        response -> {

                            try {

                                // Lấy forecast
                                JSONObject forecast =
                                        response.getJSONObject("forecast");

                                JSONArray forecastday =
                                        forecast.getJSONArray("forecastday");

                                // Danh sách điểm nhiệt độ
                                ArrayList<Entry> maxEntries =
                                        new ArrayList<>();

                                ArrayList<Entry> minEntries =
                                        new ArrayList<>();

                                // Duyệt 7 ngày
                                for (int i = 0; i < forecastday.length(); i++) {

                                    JSONObject dayObject =
                                            forecastday.getJSONObject(i);

                                    JSONObject day =
                                            dayObject.getJSONObject("day");

                                    float maxTemp =
                                            (float) day.getDouble("maxtemp_c");

                                    float minTemp =
                                            (float) day.getDouble("mintemp_c");

                                    // Thêm điểm vào chart
                                    maxEntries.add(
                                            new Entry(i, maxTemp)
                                    );

                                    minEntries.add(
                                            new Entry(i, minTemp)
                                    );
                                }

                                // Dataset nhiệt độ cao nhất
                                LineDataSet maxDataSet =
                                        new LineDataSet(
                                                maxEntries,
                                                "Nhiệt độ cao nhất"
                                        );

                                // Dataset nhiệt độ thấp nhất
                                LineDataSet minDataSet =
                                        new LineDataSet(
                                                minEntries,
                                                "Nhiệt độ thấp nhất"
                                        );

                                // Gộp dữ liệu
                                LineData lineData =
                                        new LineData(
                                                maxDataSet,
                                                minDataSet
                                        );

                                // Set dữ liệu cho chart
                                lineChart.setData(lineData);

                                // Refresh chart
                                lineChart.notifyDataSetChanged();

                                // Mô tả chart
                                Description description =
                                        new Description();

                                description.setText(
                                        "Biểu đồ nhiệt độ 7 ngày"
                                );

                                lineChart.setDescription(description);

                                // Vẽ lại chart
                                lineChart.invalidate();

                            }
                            catch (Exception e) {

                                e.printStackTrace();

                                Log.e(
                                        "CHART_ERROR",
                                        e.toString()
                                );
                            }

                        },

                        error -> {

                            error.printStackTrace();

                            Log.e(
                                    "SERVER_ERROR",
                                    error.toString()
                            );
                        }

                );

        queue.add(request);
    }
}