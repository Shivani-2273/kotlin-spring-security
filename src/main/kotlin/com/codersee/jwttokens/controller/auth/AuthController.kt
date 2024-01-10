package com.codersee.jwttokens.controller.auth

import com.codersee.jwttokens.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authenticationService: AuthenticationService) {

    @PostMapping
    fun authenticate(@RequestBody authRequest: AuthenticationRequest): AuthenticationResponse =
        authenticationService.authentication(authRequest)


    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): TokenResponse
    = authenticationService.refreshAccessToken(refreshTokenRequest.token)
        ?.mapToTokenResponse()
        ?: throw ResponseStatusException(HttpStatus.FORBIDDEN,"Invalid refresh token")


    private fun String.mapToTokenResponse(): TokenResponse =
        TokenResponse(
            token = this
        )
}
