package com.codersee.jwttokens.service

import com.codersee.jwttokens.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(jwtProperties: JwtProperties) {

    //instance of secret ket which is used to generate tokens and sign the token
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    //to generate access tokens and refresh token
    fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()


    //this is to extract email from userDetails
    fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject

    //to check token is expired or not
    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    //to verify token is valid, not expired and userDetails is matches email from the token
    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)
        return userDetails.username == email && !isExpired(token)
    }


    //this function verifies token with secret key that is token is signedWith
    // same key and extract payload
    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser
            .parseSignedClaims(token)
            .payload
    }
}
