package com.example.pawsalon.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawsalon.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var phoneNumberEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        // Initialize views
        backButton = findViewById(R.id.back_button)
        phoneNumberEditText = findViewById(R.id.phoneNumber)
        submitButton = findViewById(R.id.continue_button)

        // Back button action
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Submit button action
        submitButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString()
            if (validatePhoneNumber(phoneNumber)) {
                Toast.makeText(this, "Phone number is valid!", Toast.LENGTH_SHORT).show()
                // TODO: Handle the next step, such as sending a verification code
            } else {
                Toast.makeText(this, "Please enter a valid Philippine phone number.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Phone number validation (Philippine format)
    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        // Philippines mobile numbers start with 09 or +63 followed by 10 digits
        val regex = Regex("^(\\+63|0)9\\d{9}$")
        return regex.matches(phoneNumber)
    }
}
