package com.example.cservice;

data class LoginFormState (
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false,
    val sdescription:Int?=null
)
