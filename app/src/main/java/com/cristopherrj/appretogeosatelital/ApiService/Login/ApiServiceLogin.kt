package com.cristopherrj.appretogeosatelital.ApiService.Login

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiServiceLogin {
//    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body request:LoginRequest) : Response<LoginResponse>

}