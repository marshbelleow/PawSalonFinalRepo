package com.example.pawsalon.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawsalon.R

class OtpPasswordActivity : AppCompatActivity() {

    private lateinit var otpDigit1: EditText
    private lateinit var otpDigit2: EditText
    private lateinit var otpDigit3: EditText
    private lateinit var otpDigit4: EditText
    private lateinit var continueButton: Button
    private lateinit var resendCodeButton: Button
    private lateinit var bgImage: ImageView

    // Simulated OTP value for testing
    private val simulatedOTP = "1234"
    private var isOtpSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_password)

        // Initialize views
        otpDigit1 = findViewById(R.id.otpDigit1)
        otpDigit2 = findViewById(R.id.otpDigit2)
        otpDigit3 = findViewById(R.id.otpDigit3)
        otpDigit4 = findViewById(R.id.otpDigit4)
        continueButton = findViewById(R.id.reset_password_bigBtn)
        resendCodeButton = findViewById(R.id.send_codeagain_button)
        bgImage = findViewById(R.id.bg_image)

        // Set up OTP input fields with TextWatchers
        setupOtpInputFields()

        // Continue button action to verify OTP
        continueButton.setOnClickListener {
            val enteredOTP = getEnteredOTP()
            if (isOTPValid(enteredOTP)) {
                // Proceed to password reset screen
                proceedToPasswordReset()
            } else {
                // Show an error if OTP is invalid
                Toast.makeText(this, "Invalid OTP. Please check and try again.", Toast.LENGTH_SHORT).show()
                resetOtpFields() // Reset the OTP fields for re-entry
            }
        }

        // Resend OTP action
        resendCodeButton.setOnClickListener {
            if (isOtpSent) {
                // Simulate resend action
                resendOTP()
            } else {
                Toast.makeText(this, "OTP has not been sent yet.", Toast.LENGTH_SHORT).show()
            }
        }

        // Simulate sending the OTP on activity start
        sendOTP()
    }

    // Function to set up OTP input fields for automatic focus management
    private fun setupOtpInputFields() {
        otpDigit1.addTextChangedListener(OtpTextWatcher(otpDigit1, otpDigit2))
        otpDigit2.addTextChangedListener(OtpTextWatcher(otpDigit2, otpDigit3))
        otpDigit3.addTextChangedListener(OtpTextWatcher(otpDigit3, otpDigit4))
        otpDigit4.addTextChangedListener(OtpTextWatcher(otpDigit4, null))
    }

    // Function to get entered OTP from the 4 input fields
    private fun getEnteredOTP(): String {
        val otp1 = otpDigit1.text.toString()
        val otp2 = otpDigit2.text.toString()
        val otp3 = otpDigit3.text.toString()
        val otp4 = otpDigit4.text.toString()

        return otp1 + otp2 + otp3 + otp4
    }

    // Function to check if the entered OTP is valid
    private fun isOTPValid(enteredOTP: String): Boolean {
        return enteredOTP == simulatedOTP
    }

    // Function to send OTP to the user's phone
    private fun sendOTP() {
        // Simulate sending the OTP (this would normally involve an API call)
        Toast.makeText(this, "An OTP has been sent to your phone.", Toast.LENGTH_SHORT).show()
        isOtpSent = true
    }

    // Function to simulate OTP resend
    private fun resendOTP() {
        resetOtpFields()
        Toast.makeText(this, "A new OTP has been sent to your phone.", Toast.LENGTH_SHORT).show()
    }

    // Function to reset OTP fields for re-entry
    private fun resetOtpFields() {
        otpDigit1.text.clear()
        otpDigit2.text.clear()
        otpDigit3.text.clear()
        otpDigit4.text.clear()
        otpDigit1.requestFocus() // Focus back to the first OTP field
    }

    // Proceed to the password reset activity
    private fun proceedToPasswordReset() {
        val intent = Intent(this, NewPasswordActivity::class.java)
        startActivity(intent)
    }

    // Custom TextWatcher for OTP input fields
    private inner class OtpTextWatcher(private val current: EditText, private val next: EditText?) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s?.length == 1 && next != null) {
                next.requestFocus()
            } else if (s?.isEmpty() == true) {
                current.clearFocus()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}
