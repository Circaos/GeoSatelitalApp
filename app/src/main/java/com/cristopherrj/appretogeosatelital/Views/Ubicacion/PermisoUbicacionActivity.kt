package com.cristopherrj.appretogeosatelital.Views.Ubicacion

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cristopherrj.appretogeosatelital.R
import com.cristopherrj.appretogeosatelital.Views.Login.LoginActivity

class PermisoUbicacionActivity : AppCompatActivity() {

    private val REQUEST_CODE_LOCATION = 0
    private val REQUEST_CODE_GPS = 1

    private lateinit var tvGPS: TextView
    private lateinit var tvPermisoUbicacion: TextView

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this@PermisoUbicacionActivity, "GPS habilitado", Toast.LENGTH_SHORT)
                    .show()
                tvGPS.text = "Habilitado"
            } else {
                Toast.makeText(this, "GPS no habilitado", Toast.LENGTH_SHORT).show()
                tvGPS.text = "No Habilitado"
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permiso_ubicacion)

        //Botones
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)
        val btnUbicacionPermiso = findViewById<Button>(R.id.btnUbicacionPermiso)
        val btnGPS = findViewById<Button>(R.id.btnGPS)
        tvGPS = findViewById<TextView>(R.id.tvGPS)
        tvPermisoUbicacion = findViewById<TextView>(R.id.tvPermisoUbicacion)

        //boton Ubicacion Permiso
        btnUbicacionPermiso.setOnClickListener {
            if (estaHabilitadoPermisoUbicacion()) {
                Toast.makeText(this, "Permiso de ubicación ya habilitado", Toast.LENGTH_SHORT)
                    .show()
            } else {
                solicitarPermisoUbicacion()
            }
        }

        //Boton GPS
        btnGPS.setOnClickListener {

            if (estaHabilitadoGPS()) {
                Toast.makeText(this, "GPS ya habilitado", Toast.LENGTH_SHORT).show()
            } else {
                solicitaActivacionGPS()
            }
        }

        //Boton next Login
        btnSiguiente.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    //Override verifica permiso Ubicacion
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación habilitado", Toast.LENGTH_SHORT).show()
                //btnNext.isEnabled = true
                tvPermisoUbicacion.text = "Habilitado"
            } else {
                Toast.makeText(this, "Permiso de ubicación no habilitado", Toast.LENGTH_SHORT)
                    .show()
                tvPermisoUbicacion.text = "No Habilitado"

            }
        }
    }

    //Verifica si esta el permiso de Ubicacion
    private fun estaHabilitadoPermisoUbicacion() = ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    //Solicita Permiso
    private fun solicitarPermisoUbicacion() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_LOCATION
        )
    }


    //Verifica si el GPs esta Activado
    private fun estaHabilitadoGPS(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    //Solicita Gps
    private fun solicitaActivacionGPS() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        responseLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        if (estaHabilitadoPermisoUbicacion()) {
            tvPermisoUbicacion.text = "Habilitado"
        } else {
            tvPermisoUbicacion.text = "No Habilitado"
        }

        if (estaHabilitadoGPS()) {
            tvGPS.text = "Habilitado"
        } else {
            tvGPS.text = "No Habilitado"
        }
    }

}