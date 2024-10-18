package com.example.pawsalon.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawsalon.R
import com.example.pawsalon.network.ForgotPasswordRequest
import com.example.pawsalon.network.ForgotPasswordResponse
import com.example.pawsalon.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var forgot_pass_emailTil: TextInputLayout
    private lateinit var forgot_pass_emailEt: TextInputEditText
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize views
        forgot_pass_emailTil = findViewById(R.id.forgot_pass_emailTil)
        forgot_pass_emailEt = findViewById(R.id.forgot_pass_emailEt)
        continueButton = findViewById(R.id.continue_bigBtn)

        // Add TextWatcher to clear error message when user starts typing
        forgot_pass_emailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (forgot_pass_emailTil.error != null) {
                    forgot_pass_emailTil.error = null
                }
            }
        })

        // Continue button action
        continueButton.setOnClickListener {
            val input = forgot_pass_emailEt.text.toString().trim()

            if (isInputValid(input)) {
                proceedWithPasswordReset(input)
            } else {
                forgot_pass_emailTil.error = "Invalid username or email."
            }
        }
    }

    // Function to validate input (email or username)
    private fun isInputValid(input: String): Boolean {
        return input.isNotEmpty() // Additional validation logic can be added here
    }

    // Function to proceed with the password reset
    private fun proceedWithPasswordReset(input: String) {
        val request = ForgotPasswordRequest(input) // Adjust request constructor as needed

        val apiService = RetrofitInstance.getClient().create(ApiService::class.java)
        // Call the API to send the forgot password request
        apiService.forgotPassword(request).enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ForgotPasswordActivity, "Reset link sent!", Toast.LENGTH_SHORT).show()
                    // Navigate to another activity, e.g., back to login
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                Toast.makeText(this@ForgotPasswordActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}