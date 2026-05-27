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

        etMapCity = findViewById(R.id.etMapCity);

        btnMapSearch = findViewById(R.id.btnMapSearch);
        btnMapBack = findViewById(R.id.btnMapBack);
        btnMapHome = findViewById(R.id.btnMapHome);
        btnMapCustom = findViewById(R.id.btnMapCustom);

        etMapCity.setText("Nha Trang");

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

        btnMapBack.setOnClickListener(v -> finish());

        btnMapHome.setOnClickListener(v ->
                openGoogleMap("Nha Trang")
        );

        btnMapCustom.setOnClickListener(v ->
                openGoogleMap("Hanoi")
        );
    }

    private void openGoogleMap(String city) {

        Uri uri = Uri.parse(
                "geo:0,0?q=" + Uri.encode(city)
        );

        Intent intent =
                new Intent(Intent.ACTION_VIEW, uri);

        intent.setPackage(
                "com.google.android.apps.maps"
        );

        try {

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Chưa cài Google Maps",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}