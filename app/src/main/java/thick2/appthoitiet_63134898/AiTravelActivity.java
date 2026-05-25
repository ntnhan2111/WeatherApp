package thick2.appthoitiet_63134898;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AiTravelActivity extends AppCompatActivity {

    private EditText etAiCity;
    private Button btnAnalyze;
    private TextView tvAiResult;

    // DÁN API KEY THẬT
    private final String GEMINI_API_KEY =
            "AIzaSyDUBe3almfA9zzbBeZTzWzmFERk7FoioZQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_travel);

        etAiCity = findViewById(R.id.etAiCity);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        tvAiResult = findViewById(R.id.tvAiResult);

        btnAnalyze.setOnClickListener(v -> {

            String city =
                    etAiCity.getText().toString().trim();

            if (city.isEmpty()) {

                city = "Hà Nội";
            }

            tvAiResult.setText(
                    "Gemini AI đang phân tích..."
            );

            askGemini(city);
        });
    }

    private void askGemini(String city) {

        OkHttpClient client =
                new OkHttpClient();

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/"
                        + "gemini-1.5-flash-latest:generateContent?key="
                        + GEMINI_API_KEY;

        try {

            JSONObject textPart =
                    new JSONObject();

            textPart.put(
                    "text",
                    "Hãy tư vấn du lịch cho "
                            + city
                            + ", bao gồm thời tiết, quần áo phù hợp và địa điểm nên đi."
            );

            JSONArray parts =
                    new JSONArray();

            parts.put(textPart);

            JSONObject content =
                    new JSONObject();

            content.put(
                    "parts",
                    parts
            );

            JSONArray contents =
                    new JSONArray();

            contents.put(content);

            JSONObject requestBodyJson =
                    new JSONObject();

            requestBodyJson.put(
                    "contents",
                    contents
            );

            MediaType JSON =
                    MediaType.parse(
                            "application/json; charset=utf-8"
                    );

            RequestBody body =
                    RequestBody.create(
                            requestBodyJson.toString(),
                            JSON
                    );

            Request request =
                    new Request.Builder()
                            .url(url)
                            .addHeader(
                                    "Content-Type",
                                    "application/json"
                            )
                            .post(body)
                            .build();

            client.newCall(request)
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(
                                Call call,
                                IOException e
                        ) {

                            runOnUiThread(() ->
                                    tvAiResult.setText(
                                            "Không thể kết nối AI!\n\n"
                                                    + e.toString()
                                    )
                            );

                            Log.e(
                                    "AI_ERROR",
                                    e.toString()
                            );
                        }

                        @Override
                        public void onResponse(
                                Call call,
                                Response response
                        ) throws IOException {

                            String responseText =
                                    response.body().string();

                            Log.d(
                                    "AI_RESPONSE",
                                    responseText
                            );

                            if (!response.isSuccessful()) {

                                runOnUiThread(() ->
                                        tvAiResult.setText(
                                                "ERROR CODE: "
                                                        + response.code()
                                                        + "\n\n"
                                                        + responseText
                                        )
                                );

                                return;
                            }

                            try {

                                JSONObject jsonObject =
                                        new JSONObject(responseText);

                                JSONArray candidates =
                                        jsonObject.getJSONArray(
                                                "candidates"
                                        );

                                JSONObject first =
                                        candidates.getJSONObject(0);

                                JSONObject content =
                                        first.getJSONObject(
                                                "content"
                                        );

                                JSONArray parts =
                                        content.getJSONArray(
                                                "parts"
                                        );

                                String result =
                                        parts.getJSONObject(0)
                                                .getString("text");

                                runOnUiThread(() ->
                                        tvAiResult.setText(result)
                                );

                            }
                            catch (Exception e) {

                                e.printStackTrace();

                                runOnUiThread(() ->
                                        tvAiResult.setText(
                                                "Lỗi xử lý JSON!\n\n"
                                                        + e.toString()
                                        )
                                );
                            }
                        }
                    });

        }
        catch (Exception e) {

            e.printStackTrace();

            tvAiResult.setText(
                    "Lỗi tạo request!"
            );
        }
    }
}