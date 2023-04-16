package com.cristopherrj.appretogeosatelital.Views.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cristopherrj.appretogeosatelital.R
import com.cristopherrj.appretogeosatelital.Views.Map.MapActivity
import com.cristopherrj.appretogeosatelital.Views.Ubicacion.PermisoUbicacionActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this,PermisoUbicacionActivity::class.java))
        finish()
    }
}