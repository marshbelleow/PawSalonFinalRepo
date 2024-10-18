package com.example.pawsalon.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawsalon.R
import com.example.pawsalon.RetrofitInstance
import com.example.pawsalon.databinding.ActivitySignupBinding
import com.example.pawsalon.network.SignUpRequest
import com.example.pawsalon.network.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// This class handles the user sign-up process, including validation of input fields
class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    // View binding to access layout components directly
    private lateinit var mBinding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        mBinding = ActivitySignupBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        // Assign listeners to input fields to handle focus changes and button clicks
        mBinding.signupUsernameEt.onFocusChangeListener = this
        mBinding.signupPasswordEt.onFocusChangeListener = this
        mBinding.signupCPasswordEt.onFocusChangeListener = this
        mBinding.fullnameSetupEt.onFocusChangeListener = this
        mBinding.signupEmailEt.onFocusChangeListener = this

        // Set up click listener for the "Sign In" button to navigate to the login screen
        mBinding.signInButton.setOnClickListener {
            navigateToLogin()
        }

        // Set up click listener for the "Sign Up" button to validate fields and sign up
        mBinding.signupBigBtn.setOnClickListener {
            if (validateAllFields()) {

                val fullName = mBinding.fullnameSetupEt.text.toString().trim()
                val username = mBinding.signupUsernameEt.text.toString().trim()
                val email = mBinding.signupEmailEt.text.toString().trim()
                val password = mBinding.signupPasswordEt.text.toString().trim()
                val registerRequest = SignUpRequest(fullName, username, email, password)
                val apiService = RetrofitInstance.getClient().create(ApiService::class.java)

                // If all fields are valid, show a message and proceed with sign-up
                apiService.signup(registerRequest).enqueue(object: Callback<SignUpResponse>{
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                            // Navigate to login
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@SignUpActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Toast.makeText(this@SignUpActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Navigate to the login activity when the user clicks "Sign In"
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    // This method validates all input fields (first name, last name, email, username, password)
    private fun validateAllFields(): Boolean {
        val isFullNameValid = validateFullName()
        val isEmailValid = validateEmail()
        val isUsernameValid = validateUsername()
        val isPasswordValid = validatePassword() && validatePasswordAndConfirmPassword() && validateConfirmPassword()

        // If any of the fields are invalid, return false. Otherwise, return true
        return isFullNameValid && isEmailValid && isUsernameValid && isPasswordValid && validateConfirmPassword()
    }

    // Validate the first name field
    private fun validateFullName(): Boolean {
        var errorMessage: String? = null
        val firstName: String = mBinding.fullnameSetupEt.text.toString().trim()

        if (firstName.isEmpty()) {
            errorMessage = "First Name is required"
        }

        if (errorMessage != null) {
            // If there is an error, show it in the TextInputLayout
            mBinding.fullnameSetupTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.fullnameSetupTil.isErrorEnabled = false
        }

        return errorMessage == null
    }




    // Validate the email field (checks for empty and valid email format)
    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val email: String = mBinding.signupEmailEt.text.toString().trim()

        if (email.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Enter a valid email address"
        }

        if (errorMessage != null) {
            mBinding.signupEmailTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupEmailTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Validate the username field (length, format, and uniqueness)
    private fun validateUsername(): Boolean {
        var errorMessage: String? = null
        val username: String = mBinding.signupUsernameEt.text.toString().trim()

        if (username.isEmpty()) {
            errorMessage = "Username is required"
        } else if (username.length < 6) {
            errorMessage = "Username must be at least 6 characters long"
        } else if (!username.matches(Regex("^[a-zA-Z0-9_.]+$"))) {
            errorMessage = "Username can only contain letters, numbers, underscores, and periods"
        } else if (username.startsWith("_") || username.startsWith(".") || username.endsWith("_") || username.endsWith(
                "."
            )
        ) {
            errorMessage = "Username cannot start or end with special characters"
        } else if (!isUniqueUsername(username)) {
            errorMessage = "Username is already taken"
        }

        if (errorMessage != null) {
            mBinding.signupUsernameTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupUsernameTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Validate the password field (checks for empty and minimum length)
    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val password = mBinding.signupPasswordEt.text.toString().trim()

        if (password.isEmpty()) {
            errorMessage = "Password is required"
        } else if (password.length < 8) {
            errorMessage = "Password must be at least 8 characters long"
        }

        if (errorMessage != null) {
            mBinding.signupPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupPasswordTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Validate the confirm password field (checks for empty field)
    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val confirmPassword = mBinding.signupCPasswordEt.text.toString().trim()

        if (confirmPassword.isEmpty()) {
            errorMessage = "Confirm Password is required"
        }

        if (errorMessage != null) {
            mBinding.signupCPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupCPasswordTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    // Check if the password and confirm password fields match
    private fun validatePasswordAndConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val password = mBinding.signupPasswordEt.text.toString().trim()
        val confirmPassword = mBinding.signupCPasswordEt.text.toString().trim()

        if (password != confirmPassword) {
            errorMessage = "Passwords do not match"
        }

        if (errorMessage != null) {
            mBinding.signupCPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            // Show a check mark if passwords match
            mBinding.signupCPasswordTil.apply {
                setStartIconDrawable(R.drawable.check_circle_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GRAY))
            }
        }

        return errorMessage == null
    }

    // Dummy method to check if the username is unique
    private fun isUniqueUsername(username: String): Boolean {
        val takenUsernames = listOf("user1", "admin", "test_user") // Simulate taken usernames
        return !takenUsernames.contains(username)
    }

    // Handle field validations when the focus changes
    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        when (view?.id) {
            R.id.signup_UsernameEt -> if (!hasFocus) validateUsername()
            R.id.signup_PasswordEt -> if (!hasFocus) validatePassword()
            R.id.signup_cPasswordEt -> if (!hasFocus) validateConfirmPassword()
            R.id.fullname_setupEt -> if (!hasFocus) validateFullName()
            R.id.signup_emailEt -> if (!hasFocus) validateEmail()
        }
    }

    // Placeholder for key press events, not used here
    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }

    // Handle additional onClick logic if needed
    override fun onClick(view: View?) {
        // Additional logic can be placed here for onClick events
    }
}