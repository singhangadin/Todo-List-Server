package `in`.singhangad.todo.data.repository

import `in`.singhangad.todo.data.entity.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository: CrudRepository<User, Int> {

    fun findByEmail(email: String?): Optional<User?>?

    fun findUserByEmail(email: String?): User?
}