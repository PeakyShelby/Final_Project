package com.exam.finalproject;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NextHalaman extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SensorEventListener, LocationListener {
    private SensorManager sensorManager;
    TextView tv_sensor;

    Spinner spinner;

    LocationListener locationListener;

    Sensor accelerometer;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_halaman);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.option_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        tv_sensor = findViewById(R.id.tv_sensor);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        // Membuat listener untuk spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();

                if (selectedOption.equals("cek posisi")) {
                    checkLocationPermission();
                } else if (selectedOption.equals("Jelajahi")){
                    String url = "https://ump.ac.id";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else if (selectedOption.equals("Hubungi")) {
                    sendEmail();
                } else if (selectedOption.equals("Baca data")){
                    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    if (accelerometer != null) {
                        sensorManager.registerListener(NextHalaman.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {
                        sensorManager.unregisterListener(NextHalaman.this);
                        tv_sensor.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Tidak ada opsi yang dipilih
            }

            private void sendEmail() {
                String[] TO = {"stufi1983@gmail.com"};
                String subject = "Pesan dari aplikasi Android";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Pilih aplikasi email"));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(NextHalaman.this, "Tidak ada aplikasi email yang terpasang.", Toast.LENGTH_SHORT).show();
                }
            }



        });



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Menggunakan data lokasi yang diperoleh
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String locationString = "Latitude: " + latitude + ", Longitude: " + longitude;

                // Tampilkan lokasi saat ini
                Toast.makeText(NextHalaman.this, locationString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }



    private void checkLocationPermission() {
        // Memeriksa izin lokasi
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Jika izin tidak diberikan, minta izin
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }


        // Mendapatkan lokasi saat ini
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Memeriksa izin yang diberikan
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, dapatkan lokasi
                checkLocationPermission();
            } else {
                // Izin ditolak
                Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // Tampilkan data accelerometer pada TextView
        String data = "X: " + x + "\nY: " + y + "\nZ: " + z;
        tv_sensor.setText(data);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}