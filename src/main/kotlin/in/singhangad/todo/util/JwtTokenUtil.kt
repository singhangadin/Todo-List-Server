package `in`.singhangad.todo.util

import `in`.singhangad.todo.data.entity.User
import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenUtil {
    @Value("\${app.jwt.secret}")
    private val SECRET_KEY: String? = null

    fun generateAccessToken(user: User): String {
        return Jwts.builder()
            .setSubject(java.lang.String.format("%s,%s", user.id, user.email))
            .setIssuer("CodeJava")
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact()
    }

    companion object {
        private const val EXPIRE_DURATION = (
            24 * 60 * 60 * 1000 // 24 hour
        ).toLong()

        var LOGGER: Logger = LoggerFactory.getLogger(JwtTokenUtil::class.java)
    }

    fun validateAccessToken(token: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
            return true
        } catch (ex: ExpiredJwtException) {
            LOGGER.error("JWT expired", ex.message)
        } catch (ex: IllegalArgumentException) {
            LOGGER.error("Token is null, empty or only whitespace", ex.message)
        } catch (ex: MalformedJwtException) {
            LOGGER.error("JWT is invalid", ex)
        } catch (ex: UnsupportedJwtException) {
            LOGGER.error("JWT is not supported", ex)
        } catch (ex: SignatureException) {
            LOGGER.error("Signature validation failed")
        }
        return false
    }

    fun getSubject(token: String): String? {
        return parseClaims(token).subject
    }

    private fun parseClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .body
    }
}