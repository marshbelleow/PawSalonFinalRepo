package com.example.pawsalon.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.Button
import com.example.pawsalon.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var phoneNumberTil: TextInputLayout
    private lateinit var phoneNumberEt: TextInputEditText
    private lateinit var continueButton: Button

    // Define the country code prefix
    private val countryCode = "+63"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize views
        phoneNumberTil = findViewById(R.id.phoneNumberTil)
        phoneNumberEt = findViewById(R.id.phoneNumberEt)
        continueButton = findViewById(R.id.continue_bigBtn)

        // Set the default text to +63 and ensure the cursor is placed after the prefix
        phoneNumberEt.setText(countryCode)
        phoneNumberEt.setSelection(phoneNumberEt.text?.length ?: 0)

        // Add TextWatcher to prevent users from removing the +63 prefix
        phoneNumberEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Ensure the text always starts with +63
                if (!s.toString().startsWith(countryCode)) {
                    phoneNumberEt.setText(countryCode)
                    phoneNumberEt.setSelection(phoneNumberEt.text?.length ?: 0) // Move cursor to the end
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Clear the error message when the user starts typing
                if (phoneNumberTil.error != null) {
                    phoneNumberTil.error = null
                }
            }
        })

        // Continue button action
        continueButton.setOnClickListener {
            // Get the entered phone number (excluding the country code) and validate it
            val phoneNumber = phoneNumberEt.text.toString().trim().removePrefix(countryCode)

            if (isPhoneNumberValid(phoneNumber)) {
                // If valid, proceed to reset password
                proceedWithPasswordReset(phoneNumber)
            } else {
                // Show error message if phone number is invalid
                phoneNumberTil.error = "Invalid phone number."
            }
        }
    }

    // Function to validate phone number (10-digit without the +63)
    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        // Check if phone number is 10 digits long, starts with 9, and contains only digits
        return phoneNumber.length == 10 && phoneNumber.startsWith("9") && phoneNumber.all { it.isDigit() }
    }

    private fun proceedWithPasswordReset(phoneNumber: String) {
        // Show a toast message indicating success
        Toast.makeText(this, "Proceeding to reset password for $countryCode$phoneNumber", Toast.LENGTH_SHORT).show()

        // Proceed to ResetPasswordActivity, passing the phone number
        val intent = Intent(this, OtpPasswordActivity::class.java)
        intent.putExtra("phoneNumber", "$countryCode$phoneNumber")
        startActivity(intent)
    }
}
