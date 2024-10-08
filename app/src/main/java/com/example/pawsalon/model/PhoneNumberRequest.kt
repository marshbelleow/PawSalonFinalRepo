package com.example.pawsalon.model

data class PhoneNumberRequest(val phone_number: String) {
    fun isValid(): Boolean {
        // Matches +63 or 09 followed by 9 digits
        val regex = Regex("^(\\+63|0)9\\d{9}$")
        return regex.matches(phone_number)
    }
}
