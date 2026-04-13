package com.example.appmapas;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Prueba de conexión
        buscarLugar("Torre Eiffel");
    }

    private void buscarLugar(String query) {
        NominatimClient.getService()
                .searchPlace(query, "json", 1, 1)
                .enqueue(new Callback<List<PlaceResult>>() {

                    @Override
                    public void onResponse(Call<List<PlaceResult>> call, Response<List<PlaceResult>> response) {
                        Log.d("NOMINATIM", "Código respuesta: " + response.code());
                        Log.d("NOMINATIM", "Body nulo: " + (response.body() == null));

                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("NOMINATIM", "Tamaño lista: " + response.body().size());

                            if (!response.body().isEmpty()) {
                                PlaceResult lugar = response.body().get(0);
                                Log.d("NOMINATIM", "Nombre: " + lugar.getDisplayName());
                                Log.d("NOMINATIM", "Lat: " + lugar.getLat());
                                Log.d("NOMINATIM", "Lon: " + lugar.getLon());
                            } else {
                                Log.d("NOMINATIM", "Lista vacía");
                            }
                        } else {
                            Log.e("NOMINATIM", "Respuesta no exitosa: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlaceResult>> call, Throwable t) {
                        Log.e("NOMINATIM", "Error: " + t.getMessage());
                    }
                });
    }

}