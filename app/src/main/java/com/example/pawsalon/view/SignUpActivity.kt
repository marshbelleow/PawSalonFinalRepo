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
import com.example.pawsalon.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var mBinding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        // Assign onFocusChangeListener and onClickListener
        mBinding.signupUsernameEt.onFocusChangeListener = this
        mBinding.signupPasswordEt.onFocusChangeListener = this
        mBinding.signupCPasswordEt.onFocusChangeListener = this
        mBinding.firstnameSetupEt.onFocusChangeListener = this
        mBinding.lastnameSetupEt.onFocusChangeListener = this
        mBinding.signupPhoneNumEt.onFocusChangeListener = this

        mBinding.signInButton.setOnClickListener {
            navigateToLogin()
        }

        mBinding.signupBigBtn.setOnClickListener {
            if (validateAllFields()) {
                // Proceed with signup if all fields are valid
                Toast.makeText(this, "All fields are valid", Toast.LENGTH_SHORT).show()
            } else {
                // If any field is incomplete, show a message that all fields are required
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun validateAllFields(): Boolean {
        val isFirstNameValid = validateFirstName()
        val isLastNameValid = validateLastName()
        val isPhoneNumberValid = validatePhoneNumber()
        val isUsernameValid = validateUsername()
        val isPasswordValid = validatePassword() && validatePasswordAndConfirmPassword()

        // Check if any of the validations failed, return false if any field is invalid
        return isFirstNameValid && isLastNameValid && isPhoneNumberValid && isUsernameValid && isPasswordValid
    }

    private fun validateFirstName(): Boolean {
        var errorMessage: String? = null
        val firstName: String = mBinding.firstnameSetupEt.text.toString().trim()

        if (firstName.isEmpty()) {
            errorMessage = "First Name is required"
        }

        if (errorMessage != null) {
            mBinding.firstnameSetupTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.firstnameSetupTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    private fun validateLastName(): Boolean {
        var errorMessage: String? = null
        val lastName: String = mBinding.lastnameSetupEt.text.toString().trim()

        if (lastName.isEmpty()) {
            errorMessage = "Last Name is required"
        }

        if (errorMessage != null) {
            mBinding.lastnameSetupTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.lastnameSetupTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    private fun validatePhoneNumber(): Boolean {
        var errorMessage: String? = null
        val phoneNumber: String = mBinding.signupPhoneNumEt.text.toString().trim()

        if (phoneNumber.isEmpty()) {
            errorMessage = "Phone Number is required"
        } else if (!phoneNumber.matches(Regex("^\\+?[0-9]{10,15}\$"))) {
            errorMessage = "Enter a valid phone number (10-15 digits)"
        }

        if (errorMessage != null) {
            mBinding.signupPhoneNumTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            mBinding.signupPhoneNumTil.isErrorEnabled = false
        }

        return errorMessage == null
    }

    private fun validateUsername(): Boolean {
        var errorMessage: String? = null
        val username: String = mBinding.signupUsernameEt.text.toString().trim()

        if (username.isEmpty()) {
            errorMessage = "Username is required"
        } else if (username.length < 6) {
            errorMessage = "Username must be at least 6 characters long"
        } else if (!username.matches(Regex("^[a-zA-Z0-9_.]+$"))) {
            errorMessage = "Username can only contain letters, numbers, underscores, and periods"
        } else if (username.startsWith("_") || username.startsWith(".") || username.endsWith("_") || username.endsWith(".")) {
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
            mBinding.signupCPasswordTil.apply {
                setStartIconDrawable(R.drawable.check_circle_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GRAY))
            }
        }

        return errorMessage == null
    }

    private fun isUniqueUsername(username: String): Boolean {
        // Dummy check for example purposes
        val takenUsernames = listOf("user1", "admin", "test_user")
        return !takenUsernames.contains(username)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        when (view?.id) {
            R.id.signup_UsernameEt -> if (!hasFocus) validateUsername()
            R.id.signup_PasswordEt -> if (!hasFocus) validatePassword()
            R.id.signupCPasswordEt -> if (!hasFocus) validateConfirmPassword()
            R.id.firstname_setupEt -> if (!hasFocus) validateFirstName()
            R.id.lastname_setupEt -> if (!hasFocus) validateLastName()
            R.id.signup_PhoneNumEt -> if (!hasFocus) validatePhoneNumber()
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }

    override fun onClick(view: View?) {
        // Additional logic if needed for onClick events
    }
}
