package com.codersee.jwttokens.config

import com.codersee.jwttokens.service.CustomUserDetailsService
import com.codersee.jwttokens.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenService: TokenService,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        //first check if request contains authorization Header
        //if not, we do not proceed with this function and simply pass
        //request to down the filter chain
        val authHeader: String? = request.getHeader("Authorization")

        if(authHeader.doesNotContainBearer()) {
            filterChain.doFilter(request, response)
            return
        }

        //we extract the JWT token and valid authorization header consist of Bearer
        val jwtToken = authHeader!!.extractTokenValue()
        val email = tokenService.extractEmail(jwtToken)

        //email and there is no present authentication
        if(email != null && SecurityContextHolder.getContext().authentication == null) {
          //find user by username and if token is valid then we update context
            val foundUser = userDetailsService.loadUserByUsername(email)
            if(tokenService.isValid(jwtToken, foundUser)) {
                updateContext(foundUser, request)
            }

            filterChain.doFilter(request, response)
        }

    }

    private fun updateContext(foundUser: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(foundUser, null, foundUser.authorities)
        authToken.details=WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authToken
    }


    private fun String?.doesNotContainBearer(): Boolean =
        this == null || !this.startsWith("Bearer ")

    private fun String.extractTokenValue(): String =
        this.substringAfter("Bearer ")


}
