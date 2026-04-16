package com.example.appmapas;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private TextView txtLugarUbi, txtPais, txtCodigoPostal, txtDepartamentoEstado, txtCoordenadas, txtImportancia, txtTipo;
    private EditText inputBuscar;
    private ImageButton btnBuscar;

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

        btnBuscar = findViewById(R.id.btnBuscar);
        inputBuscar = findViewById(R.id.inputBuscar);
        mapView = findViewById(R.id.mapView);
        // Inicializar el mapa
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        // Zoom inicial
        mapView.getController().setZoom(5.0);

        // Prueba de conexión
        buscarLugar("San Salvador, El Salvador");

        btnBuscar.setOnClickListener(v->{
            String lugar = inputBuscar.getText().toString().trim();

            if (lugar.isEmpty()){
                inputBuscar.setError("Ingresa un lugar");
                return;
            }
            buscarLugar(lugar);
        });

    }

    private void buscarLugar(String query) {
        NominatimClient.getService()
                .searchPlace(query, "json", 1, 1)
                .enqueue(new Callback<List<PlaceResult>>() {

                    @Override
                    public void onResponse(Call<List<PlaceResult>> call, Response<List<PlaceResult>> response) {

                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            PlaceResult lugar = response.body().get(0);

                            runOnUiThread(() -> {
                                // Inicializar vistas
                                txtLugarUbi = findViewById(R.id.txtLugarUbi);
                                txtPais = findViewById(R.id.txtPais);
                                txtCodigoPostal = findViewById(R.id.txtCodigoPostal);
                                txtDepartamentoEstado = findViewById(R.id.txtDepartamento_Estado);
                                txtCoordenadas = findViewById(R.id.txtCoordenadas);
                                txtImportancia = findViewById(R.id.txtImportancia);
                                txtTipo = findViewById(R.id.txtTipo);

                                // Mostrar datos

                                txtLugarUbi.setText(lugar.getDisplayName());
                                txtCoordenadas.setText("Coordenadas: " + lugar.getLat() + ", " + lugar.getLon());
                                txtImportancia.setText("Importancia: " + lugar.getImportance());
                                txtTipo.setText("Tipo: " + lugar.getType());

                                if (lugar.getAddress() != null) {
                                    txtPais.setText("País: " + lugar.getAddress().getCountry());
                                    txtCodigoPostal.setText("Código Postal: " + lugar.getAddress().getPostcode());
                                    txtDepartamentoEstado.setText("Departamento/Estado: " + lugar.getAddress().getCity() + ", " + lugar.getAddress().getState());
                                } else {
                                    txtPais.setText("País: No disponible");
                                    txtCodigoPostal.setText("Código Postal: No disponible");
                                    txtDepartamentoEstado.setText("Departamento/Estado: No disponible");
                                }

                                mostrarEnMapa(lugar);
                            });
                        }else {
                            // La API respondio pero no encontró el lugar
                            runOnUiThread(() -> {
                                inputBuscar.setError("Lugar no encontrado");
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlaceResult>> call, Throwable t) {
                        // Error de red o conexion
                        runOnUiThread(() -> {
                            inputBuscar.setError("Sin conexión a internet");
                        });
                    }
                });
    }

    private void mostrarEnMapa(PlaceResult lugar) {
        GeoPoint punto = new GeoPoint(lugar.getLatAsDouble(), lugar.getLonAsDouble());

        // Limpiar marcadores anteriores
        mapView.getOverlays().clear();

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