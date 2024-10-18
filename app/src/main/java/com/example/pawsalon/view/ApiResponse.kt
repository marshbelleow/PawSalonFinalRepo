package com.example.pawsalon.view

import com.example.pawsalon.network.ForgotPasswordRequest
import com.example.pawsalon.network.ForgotPasswordResponse
import com.example.pawsalon.network.LoginRequest
import com.example.pawsalon.network.LoginResponse
import com.example.pawsalon.network.SignUpRequest
import com.example.pawsalon.network.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("clients/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("clients/register")
    fun signup(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("clients/forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>
}