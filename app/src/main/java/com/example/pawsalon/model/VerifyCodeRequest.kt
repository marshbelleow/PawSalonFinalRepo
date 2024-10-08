package com.example.pawsalon.model

data class VerifyCodeRequest(val phone_number: String, val verification_code: String) {
    fun isValid(): Boolean {
        // Assuming the verification code must be 6 digits long
        return verification_code.length == 6 && verification_code.all { it.isDigit() }
    }
}
