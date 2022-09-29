package `in`.singhangad.todo.domain.entity

class AuthResponse {
    lateinit var email: String
    lateinit var accessToken: String

    constructor()
    constructor(email: String, accessToken: String) {
        this.email = email
        this.accessToken = accessToken
    }
}