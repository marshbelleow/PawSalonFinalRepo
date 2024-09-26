package com.example.pawsalon.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.pawsalon.R
import com.example.pawsalon.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var mBinding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.signupUsernameEt.onFocusChangeListener = this
        mBinding.signupPasswordEt.onFocusChangeListener = this
        mBinding.signupCPasswordET.onFocusChangeListener = this

        mBinding.signInButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun validateUsername(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.signupUsernameEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Username is required"
        }
        if (errorMessage != null) {
            mBinding.signupUsernameTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value = mBinding.signupPasswordEt.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must be 6 characters long"
        }
        if (errorMessage != null) {
            mBinding.signupPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val value = mBinding.signupCPasswordET.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Confirm Password is required"
        } else if (value.length < 6) {
            errorMessage = "Confirm Password must be 6 characters long"
        }
        if (errorMessage != null) {
            mBinding.signupCPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val password = mBinding.signupPasswordEt.text.toString()
        val confirmPassword = mBinding.signupCPasswordET.text.toString()
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match"
        }
        if (errorMessage != null) {
            mBinding.signupCPasswordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.signup_UsernameEt -> {
                    if (hasFocus){
                        if (mBinding.signupUsernameTil.isErrorEnabled){
                            mBinding.signupUsernameTil.isErrorEnabled = false
                        }
                    }else{
                        validateUsername()
                    }
                }
                R.id.signup_PasswordEt -> {
                    if (hasFocus){
                        if(mBinding.signupPasswordTil.isErrorEnabled){
                            mBinding.signupPasswordTil.isErrorEnabled = false
                        }
                    }else{
                        if (validatePassword() && mBinding.signupCPasswordET.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordAndConfirmPassword()){
                            if(mBinding.signupCPasswordTil.isErrorEnabled){
                                mBinding.signupCPasswordTil.isErrorEnabled = false
                            }
                            mBinding.signupCPasswordTil.apply {
                                setStartIconDrawable(R.drawable.check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GRAY))
                            }
                        }
                    }
                }
                R.id.signup_cPasswordET -> {
                    if (hasFocus){
                        if (mBinding.signupCPasswordTil.isErrorEnabled){
                            mBinding.signupCPasswordTil.isErrorEnabled = false
                        }
                    }else{
                        if (validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()){
                            if (mBinding.signupPasswordTil.isErrorEnabled){
                                mBinding.signupPasswordTil.isErrorEnabled = false
                            }
                            mBinding.signupCPasswordTil.apply {
                                setStartIconDrawable(R.drawable.check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }

    override fun onClick(view: View?) {
        // Implement click handling here
    }
}
