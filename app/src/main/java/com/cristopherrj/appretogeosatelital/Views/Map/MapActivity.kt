package com.cristopherrj.appretogeosatelital.Views.Map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.cristopherrj.appretogeosatelital.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        createFragment()
    }

    override fun onMapReady(googleMap: GoogleMap) {
//        TODO("Not yet implemented")
        Log.i("criscris", "Iniciando createonMapReady")
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        habilitarBotonUbicacion()
    }

    private fun createFragment(){ //Crea el Fragment
        Log.i("criscris", "Iniciando createFragment")
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMyLocationButtonClick(): Boolean { //Cuando se le da clikc al boton GPS
        //es un onverride/ para cuando de click al boton de geolocalizacion
        Toast.makeText(
            this,
            "Yendo a Ubicacion",
            Toast.LENGTH_SHORT
        ).show()
        return false //devulve false si quieres que se vaya a tu posicion, devuelve true si no quieres
    }

    override fun onMyLocationClick(p0: Location) { //Cuando le da click al punto Azul
        //es un onverride/ para cuando de click al a tu posicion
        Toast.makeText(
            this,
            "Estas en ${p0.latitude} , ${p0.longitude} ",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun habilitarBotonUbicacion(){ //Habilita/muestra el boton GPS
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           Toast.makeText(this,"Error, Permisos deshabilitados",Toast.LENGTH_SHORT).show()
            return
        }else{
            map.isMyLocationEnabled = true
        }
    }


}