package com.cristopherrj.appretogeosatelital.Views.Map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.cristopherrj.appretogeosatelital.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap
    private lateinit var btnGps: FloatingActionButton

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createFragment()

        btnGps = findViewById(R.id.btnGpsGo)

        btnGps.setOnClickListener {
            irUbicacionGPS()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i("criscris", "Iniciando createonMapReady")
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        habilitarBotonUbicacion()

    }

    private fun createFragment() { //Crea el Fragment
        Log.i("criscris", "Iniciando createFragment")
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMyLocationButtonClick(): Boolean { //Cuando se le da clikc al boton GPS
        //es un onverride para cuando de click al boton de geolocalizacion
        Toast.makeText(
            this,
            "Yendo a Ubicacion",
            Toast.LENGTH_SHORT
        ).show()
        return false //devuelve false si quieres que se vaya a tu posicion, devuelve true si no quieres
    }

    override fun onMyLocationClick(p0: Location) { //Cuando le da click al punto Azul(Ubicacion)
        //es un onverride/ para cuando de click al a tu posicion
        Toast.makeText(
            this,
            "Estas en ${p0.latitude} , ${p0.longitude} ",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun habilitarBotonUbicacion() { //Habilita/muestra el boton GPS
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Error, Permisos deshabilitados", Toast.LENGTH_SHORT).show()
            return
        } else {
            map.isMyLocationEnabled = true
        }
    }

    private fun irUbicacionGPS() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Error, Permisos deshabilitados", Toast.LENGTH_SHORT).show()
            return
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // La ubicación puede ser nula si la ubicación no se conoce todavía
                    location?.let {
                        // Aquí puedes obtener la latitud y longitud de la ubicación
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val coordenada = LatLng(latitude, longitude)
                        val greenMarkerIcon =
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)

                        map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(coordenada, 15f),
                            2000,
                            null
                        )
                        map.addMarker(
                            MarkerOptions().position(coordenada).title("Ubicacion")
                                .icon(greenMarkerIcon)
                        )

                    }
                }
        }

    }

}