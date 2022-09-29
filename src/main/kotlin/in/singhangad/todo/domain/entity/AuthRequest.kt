package `in`.singhangad.todo.domain.entity

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email

class AuthRequest(
    @Email
    @Length(min = 5, max = 50)
    val email: String,

    @Length(min = 5, max = 10)
    val password: String
)