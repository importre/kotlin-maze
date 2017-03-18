package com.importre.example.main.login

import com.importre.example.R

data class LoginModel(
    val email: String = "",
    val emailError: Int = R.string.error_invalid_email,
    val password: String = "",
    val passwordError: Int = R.string.error_invalid_password
)
