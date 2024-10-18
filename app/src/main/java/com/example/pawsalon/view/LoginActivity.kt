package com.example.pawsalon.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawsalon.R
import com.example.pawsalon.databinding.ActivityLoginBinding
import com.example.pawsalon.network.LoginRequest
import com.example.pawsalon.network.LoginResponse
import com.example.pawsalon.RetrofitInstance
import com.example.pawsalon.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        mBinding.loginUsernameEt.onFocusChangeListener = this
        mBinding.loginPasswordEt.onFocusChangeListener = this

        mBinding.loginBigBtn.setOnClickListener {
            handleLogin()
        }

        // Navigate to SignUp activity
        mBinding.signUpButton.setOnClickListener {
            navigateToSignUp()
        }

        // Navigate to ForgotPassword activity
        mBinding.forgotPasswordButton.setOnClickListener {
            navigateToForgotPassword()
        }
    }

    private fun navigateToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun navigateToForgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun handleLogin() {
        if (validateUsername() && validatePassword()) {
            val username = mBinding.loginUsernameEt.text.toString()
            val password = mBinding.loginPasswordEt.text.toString()

            // Create LoginRequest object
            val loginRequest = LoginRequest(username, password)

            // Call the login API using Retrofit
            val apiService = RetrofitInstance.getClient().create(ApiService::class.java)
            apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        Toast.makeText(this@LoginActivity, "Login Successful: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
                        // Handle success, navigate or store token
                    } else {
                        // Log the raw response for debugging
                        Toast.makeText(this@LoginActivity, "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        Log.e("LoginError", "Response: ${response.errorBody()?.string()}") // Add this to log errors
                    }
                }


                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Log the error message
                    Log.e("LoginError", "onFailure: ${t.message}", t)
                    Toast.makeText(this@LoginActivity, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun validateUsername(): Boolean {
        val value = mBinding.loginUsernameEt.text.toString()
        return if (value.isEmpty()) {
            mBinding.loginUsernameTil.apply {
                isErrorEnabled = true
                error = "Username is required"
            }
            false
        } else {
            mBinding.loginUsernameTil.isErrorEnabled = false
            true
        }
    }

    private fun validatePassword(): Boolean {
        val value = mBinding.loginPasswordEt.text.toString()
        return if (value.isEmpty()) {
            mBinding.loginPasswordTil.apply {
                isErrorEnabled = true
                error = "Password is required"
            }
            false
        } else {
            mBinding.loginPasswordTil.isErrorEnabled = false
            true
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.login_UsernameEt -> if (!hasFocus) validateUsername()
                R.id.login_PasswordEt -> if (!hasFocus) validatePassword()
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.login_bigBtn -> handleLogin()
        }
    }
}