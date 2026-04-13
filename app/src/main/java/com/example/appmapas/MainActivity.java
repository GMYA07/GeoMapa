package com.example.appmapas;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;

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

        // Configuración obligatoria de osmdroid
        Configuration.getInstance().setUserAgentValue("AppMapas/1.0");

        setContentView(R.layout.activity_main);

        // Inicializar el mapa
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        // Zoom inicial
        mapView.getController().setZoom(5.0);

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

                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            PlaceResult lugar = response.body().get(0);

                            Log.d("NOMINATIM", "Nombre: " + lugar.getDisplayName());

                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlaceResult>> call, Throwable t) {
                        Log.e("NOMINATIM", "Error: " + t.getMessage());
                    }
                });
    }

    private void mostrarEnMapa(PlaceResult lugar) {
        GeoPoint punto = new GeoPoint(lugar.getLatAsDouble(), lugar.getLonAsDouble());

        // Centrar y hacer zoom al lugar
        mapView.getController().setZoom(15.0);
        mapView.getController().animateTo(punto);

        // Agregar marcador
        Marker marcador = new Marker(mapView);
        marcador.setPosition(punto);
        marcador.setTitle(lugar.getDisplayName());
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marcador);
        mapView.invalidate();
    }

    // Necesario para que el mapa funcione bien con el ciclo de vida
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


}