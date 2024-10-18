package com.example.pawsalon.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Data model for login request
data class LoginRequest(
    val username: String,
    val password: String
)

// Data model for login response
data class LoginResponse(
    val token: String?,
    val message: String?
)

// Data model for signup request
data class SignUpRequest(
    val fullname: String,
    val username: String,
    val email: String,
    val password: String
)

// Data model for signup response
data class SignUpResponse(
    val success: Boolean,
    val message: String
)

// Data model for forgot password request
data class ForgotPasswordRequest(
    val phoneNumber: String
)

// Data model for forgot password response
data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String,
    val otpSent: Boolean
)

// Retrofit interface for API calls
interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun signup(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>
}
