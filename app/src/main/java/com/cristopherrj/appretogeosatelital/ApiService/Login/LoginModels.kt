package com.cristopherrj.appretogeosatelital.ApiService.Login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email:String,
    @SerializedName("password") val password:String
)

data class LoginResponse(
    @SerializedName("success") val success:Boolean,
    @SerializedName("results") val results:ResultLogin
)

data class ResultLogin(
    @SerializedName("token") val token:String,
    @SerializedName("expires_at") val expires_at:String,
    @SerializedName("provisory") val provisory:Boolean,
)

data class ErrorLoginRequest(
    @SerializedName("success") val success:Boolean,
    @SerializedName("message") val message:String,
)
//data class LoginModels()