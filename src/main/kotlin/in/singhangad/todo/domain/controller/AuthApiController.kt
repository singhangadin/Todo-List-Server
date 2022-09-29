package `in`.singhangad.todo.domain.controller

import `in`.singhangad.todo.data.entity.User
import `in`.singhangad.todo.data.repository.UserRepository
import `in`.singhangad.todo.domain.ApiResponse
import `in`.singhangad.todo.domain.entity.AuthRequest
import `in`.singhangad.todo.domain.entity.AuthResponse
import `in`.singhangad.todo.util.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
class AuthApi {
    @Autowired
    lateinit var authManager: AuthenticationManager

    @Autowired
    lateinit var jwtUtil: JwtTokenUtil

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @PostMapping("/todo/auth/login")
    @ResponseBody
    fun login(@RequestBody @Valid request: AuthRequest): ResponseEntity<ApiResponse> {
        return try {
            val authentication: Authentication = authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.email, request.password
                )
            )
            val user: User = authentication.principal as User
            val accessToken = jwtUtil.generateAccessToken(user)
            val response = AuthResponse(user.email, accessToken)
            ResponseEntity.ok().body(ApiResponse.Success(response))
        } catch (ex: BadCredentialsException) {
            ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @PostMapping("/todo/auth/register")
    @ResponseBody
    fun registerUser(@RequestBody request: @Valid AuthRequest): ResponseEntity<*>? {
        if (userRepo.findUserByEmail(request.email) != null) {
            return ResponseEntity<Any?>(
                ApiResponse.Error("Email Address already in use!"),
                HttpStatus.BAD_REQUEST
            )
        }

        val user = User(request.email, passwordEncoder.encode(request.password))
        userRepo.save(user)
        return ResponseEntity.ok<ApiResponse>(ApiResponse.Success("User registered successfully"))
    }
}