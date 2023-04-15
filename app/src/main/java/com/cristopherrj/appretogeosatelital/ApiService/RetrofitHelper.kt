package com.cristopherrj.appretogeosatelital.ApiService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://geolite-js.dct4.geosatelital.red/api/app/auths/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}