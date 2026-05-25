package thick2.appthoitiet_63134898;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    private EditText etMapCity;

    private Button btnMapSearch;
    private Button btnMapBack;
    private Button btnMapHome;
    private Button btnMapCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Ánh xạ View
        etMapCity = findViewById(R.id.etMapCity);

        btnMapSearch = findViewById(R.id.btnMapSearch);
        btnMapBack = findViewById(R.id.btnMapBack);
        btnMapHome = findViewById(R.id.btnMapHome);
        btnMapCustom = findViewById(R.id.btnMapCustom);

        // Thành phố mặc định
        etMapCity.setText("Nha Trang");

        // Nút tìm
        btnMapSearch.setOnClickListener(v -> {

            String city =
                    etMapCity.getText().toString().trim();

            if (!city.isEmpty()) {

                openGoogleMap(city);

            } else {

                Toast.makeText(
                        this,
                        "Nhập tên thành phố",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // Nút back
        btnMapBack.setOnClickListener(v -> finish());

        // Nút home
        btnMapHome.setOnClickListener(v ->
                openGoogleMap("Nha Trang")
        );

        // Nút custom
        btnMapCustom.setOnClickListener(v ->
                openGoogleMap("Hanoi")
        );
    }

    // Mở Google Maps
    private void openGoogleMap(String city) {

        city = city.replace(" ", "+");

        Uri uri = Uri.parse(
                "https://www.google.com/maps/search/?api=1&query="
                        + city
        );

        Intent intent =
                new Intent(Intent.ACTION_VIEW, uri);

        startActivity(intent);
    }
}