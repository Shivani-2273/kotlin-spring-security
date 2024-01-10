package com.codersee.jwttokens.controller.auth

data class AuthenticationRequest(
    val email: String,
    val password: String
)
