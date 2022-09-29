package `in`.singhangad.todo.domain

import `in`.singhangad.todo.data.entity.User
import `in`.singhangad.todo.util.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtTokenFilter : OncePerRequestFilter() {
    @Autowired
    private val jwtUtil: JwtTokenUtil? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response)
            return
        }
        val token = getAccessToken(request)
        if (!jwtUtil!!.validateAccessToken(token)) {
            filterChain.doFilter(request, response)
            return
        }
        setAuthenticationContext(token, request)
        filterChain.doFilter(request, response)
    }


    private fun hasAuthorizationBearer(request: HttpServletRequest): Boolean {
        val header = request.getHeader("Authorization")
        return !(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer"))
    }

    private fun getAccessToken(request: HttpServletRequest): String {
        val header = request.getHeader("Authorization")
        return header.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
    }

    private fun setAuthenticationContext(token: String, request: HttpServletRequest) {
        val userDetails = getUserDetails(token)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, null)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun getUserDetails(token: String): UserDetails {
        val userDetails = User()
        val jwtSubject = jwtUtil!!.getSubject(token)!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        userDetails.id = (jwtSubject[0].toInt())
        userDetails.email = (jwtSubject[1])
        return userDetails
    }
}