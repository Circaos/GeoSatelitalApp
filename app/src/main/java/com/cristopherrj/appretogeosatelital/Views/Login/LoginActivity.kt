package com.cristopherrj.appretogeosatelital.Views.Login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cristopherrj.appretogeosatelital.ApiService.Login.ApiServiceLogin
import com.cristopherrj.appretogeosatelital.ApiService.Login.ErrorLoginRequest
import com.cristopherrj.appretogeosatelital.ApiService.Login.LoginRequest
import com.cristopherrj.appretogeosatelital.ApiService.Login.LoginResponse
import com.cristopherrj.appretogeosatelital.ApiService.RetrofitHelper
import com.cristopherrj.appretogeosatelital.R
import com.cristopherrj.appretogeosatelital.Views.Map.MapActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketException

class LoginActivity : AppCompatActivity() {

    private val retrofit = RetrofitHelper().getRetrofit()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Inicializo Elementos UI
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etUsuarioLogin = findViewById<EditText>(R.id.etUsuarioLogin)
        val etPasswordLogin = findViewById<EditText>(R.id.etPasswordLogin)
        //Inicializo retrofit


        btnLogin.setOnClickListener {
            val usuario: String = etUsuarioLogin.text.toString()
            val password: String = etPasswordLogin.text.toString()
            val requestLogin = LoginRequest(usuario, password)

            CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
                loginSolicitud(requestLogin)
            }
        }

    }

    suspend fun loginSolicitud(request: LoginRequest) {
        ocultarTeclado()
        val myResponse: Response<LoginResponse> =
            retrofit.create(ApiServiceLogin::class.java).login(request)
        Log.i("criscris", "${myResponse.code()}")
        if (myResponse.isSuccessful) {
            val respuesta = myResponse.body()
            if (respuesta != null) {
                Log.i("criscris", "${respuesta.success}")
                runOnUiThread { irMap() }
            } else {
                Log.i("criscris", "nulo")
            }


        } else {
            val errorRespuesta = Gson().fromJson(
                myResponse.errorBody()?.charStream(),
                ErrorLoginRequest::class.java
            )
            if (errorRespuesta != null) {
                Log.i("criscris", errorRespuesta.message)
                runOnUiThread {
                    Toast.makeText(this, errorRespuesta.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.i("criscris", "nulo")
            }
        }
    }

    //Por si ocurre un Error la app no se para
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("criscris", "Error en la Corrutina")
        Log.i("criscris", throwable.toString())
        when (throwable) {
            is SocketException -> {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "ERROR / verifique coneccion al Internet",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else -> {}
        }
    }

    //Ir al Map
    private fun irMap() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Ocultar teclado
    private fun ocultarTeclado(){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = getCurrentFocus()
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}