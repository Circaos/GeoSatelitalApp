package com.cristopherrj.appretogeosatelital.Views.Ubicacion

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cristopherrj.appretogeosatelital.R
import com.cristopherrj.appretogeosatelital.Views.Login.LoginActivity

class PermisoUbicacionActivity : AppCompatActivity() {

    private val REQUEST_CODE_LOCATION = 0

    private lateinit var btnSiguiente: Button
    private lateinit var ivGPS: ImageView
    private lateinit var ivPermisoUbicacion: ImageView


    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this@PermisoUbicacionActivity, "GPS habilitado", Toast.LENGTH_SHORT)
                    .show()
                ivGPS.setImageResource(R.drawable.icon_ok)

            } else {
                Toast.makeText(this, "GPS no habilitado", Toast.LENGTH_SHORT).show()
                ivGPS.setImageResource(R.drawable.icon_alerta)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permiso_ubicacion)

        //Botones
        btnSiguiente = findViewById<Button>(R.id.btnSiguiente)
        val btnUbicacionPermiso = findViewById<Button>(R.id.btnUbicacionPermiso)
        val btnGPS = findViewById<Button>(R.id.btnGPS)
        ivGPS = findViewById(R.id.ivEncenderGPS)
        ivPermisoUbicacion = findViewById(R.id.ivPermisoUbicacion)


        //boton Ubicacion Permiso
        btnUbicacionPermiso.setOnClickListener {
            verificaPermisoUbicacion()
            if (estaHabilitadoPermisoUbicacion()) {
                Toast.makeText(this, "Permiso de ubicación ya habilitado", Toast.LENGTH_SHORT)
                    .show()
            } else {
                solicitarPermisoUbicacion()
            }
        }

        //Boton GPS
        btnGPS.setOnClickListener {
            verificaGPS()
            if (estaHabilitadoGPS()) {
                Toast.makeText(this, "GPS ya habilitado", Toast.LENGTH_SHORT).show()
            } else {
                solicitaActivacionGPS()
            }
        }

        //Boton next Login
        btnSiguiente.setOnClickListener {

            if (estaHabilitadoGPS() && estaHabilitadoPermisoUbicacion()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Verifica Configuracion", Toast.LENGTH_SHORT).show()
                verificaGPS()
                verificaPermisoUbicacion()
                btnSiguiente.isEnabled = false
            }


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
                ivPermisoUbicacion.setImageResource(R.drawable.icon_ok)

            } else {
                Toast.makeText(this, "Permiso de ubicación no habilitado", Toast.LENGTH_SHORT)
                    .show()
                ivPermisoUbicacion.setImageResource(R.drawable.icon_alerta)
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

    //Cuando se regresa al Activity, Verifica los permisos
    override fun onResume() {
        super.onResume()

        verificaPermisoUbicacion()
        verificaGPS()

        btnSiguiente.isEnabled = estaHabilitadoGPS() && estaHabilitadoPermisoUbicacion()

    }

    private fun verificaPermisoUbicacion() {
        if (estaHabilitadoPermisoUbicacion()) {
            ivPermisoUbicacion.setImageResource(R.drawable.icon_ok)
        } else {
            ivPermisoUbicacion.setImageResource(R.drawable.icon_alerta)
        }
    }

    private fun verificaGPS() {
        if (estaHabilitadoGPS()) {
            ivGPS.setImageResource(R.drawable.icon_ok)
        } else {
            ivGPS.setImageResource(R.drawable.icon_alerta)
        }
    }


}