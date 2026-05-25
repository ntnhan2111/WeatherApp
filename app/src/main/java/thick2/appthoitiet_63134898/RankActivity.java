package thick2.appthoitiet_63134898;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class RankActivity extends AppCompatActivity {

    private TextView tvMaxRecord, tvMinRecord, tvStableRecord, tvRankList;
    // ⚠️ Hãy thay bằng API Key chạy được của bạn
    private final String API_KEY = "66fb59801562aa019ac84703345ced5d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        tvMaxRecord = findViewById(R.id.tvMaxRecord);
        tvMinRecord = findViewById(R.id.tvMinRecord);
        tvStableRecord = findViewById(R.id.tvStableRecord);
        tvRankList = findViewById(R.id.tvRankList);

        String city = getIntent().getStringExtra("RANK_CITY_KEY");
        if (city == null || city.isEmpty()) city = "Hanoi";

        getRankWeatherData(city);
    }

    private void getRankWeatherData(String city) {
        RequestQueue queue = Volley.newRequestQueue(this);
        // SỬA LỖI: Đường dẫn API chính xác của OpenWeather Forecast
        String forecastUrl =
                "https://api.openweathermap.org/data/2.5/forecast?q="
                        + city
                        + "&appid="
                        + API_KEY
                        + "&units=metric&lang=vi";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, forecastUrl, null,
                response -> {
                    try {
                        JSONArray list = response.getJSONArray("list");

                        ArrayList<WeatherTimeline> timelineList = new ArrayList<>();
                        double maxTemp = -100.0;
                        double minTemp = 100.0;

                        // Đối tượng lưu mốc thời gian có độ lệch nhiệt độ thấp nhất (ổn định nhất)
                        WeatherTimeline stableTimeline = null;
                        double minDelta = 100.0;

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            long dt = item.getLong("dt") * 1000; // Đổi sang mili giây

                            JSONObject main = item.getJSONObject("main");
                            double temp = main.getDouble("temp");
                            double tempMin = main.getDouble("temp_min");
                            double tempMax = main.getDouble("temp_max");

                            // Định dạng ngày giờ hiển thị
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
                            String timeStr = sdf.format(new Date(dt));

                            WeatherTimeline timeline = new WeatherTimeline(timeStr, temp);
                            timelineList.add(timeline);

                            // Tìm nhiệt độ cao nhất / thấp nhất toàn lịch trình
                            if (temp > maxTemp) maxTemp = temp;
                            if (temp < minTemp) minTemp = temp;

                            // Tìm mốc ổn định nhất (chênh lệch temp_max và temp_min nhỏ nhất)
                            double delta = Math.abs(tempMax - tempMin);
                            if (delta < minDelta) {
                                minDelta = delta;
                                stableTimeline = timeline;
                            }
                        }

                        // 1. Hiển thị thông tin kỷ lục lên Thống Kê
                        tvMaxRecord.setText("Nhiệt độ cao nhất: " + maxTemp + "°C");
                        tvMinRecord.setText("Nhiệt độ thấp nhất: " + minTemp + "°C");
                        if (stableTimeline != null) {
                            tvStableRecord.setText("Nhiệt độ ổn định nhất: " + stableTimeline.temp + "°C (" + stableTimeline.time + ")");
                        }

                        // 2. Sắp xếp danh sách nhiệt độ từ cao đến thấp để lập bảng xếp hạng
                        Collections.sort(timelineList, (o1, o2) -> Double.compare(o2.temp, o1.temp));

                        // 3. Hiển thị danh sách chi tiết lên giao diện
                        StringBuilder rankBuilder = new StringBuilder();
                        int rank = 1;
                        for (WeatherTimeline t : timelineList) {
                            rankBuilder.append(rank).append(". ")
                                    .append(t.time).append("  ➡  ")
                                    .append(t.temp).append("°C\n");
                            rank = rank + 1;
                        }
                        tvRankList.setText(rankBuilder.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        tvRankList.setText("Lỗi xử lý dữ liệu JSON từ hệ thống!");
                    }
                },
                error -> {
                    error.printStackTrace();
                    tvRankList.setText("Lỗi kết nối máy chủ OpenWeather, vui lòng kiểm tra mạng hoặc API Key!");
                }
        );

        queue.add(request);
    }

    // Class phụ hỗ trợ lưu cấu trúc dữ liệu thời tiết thuận tiện cho việc sắp xếp
    private static class WeatherTimeline {
        String time;
        double temp;

        WeatherTimeline(String time, double temp) {
            this.time = time;
            this.temp = temp;
        }
    }
}
