package com.example.pawsalon.model

data class Admins(
    val id: Int,
    val username: String,
    val permissions: List<String>
)
