package `in`.singhangad.todo.domain

sealed interface ApiResponse {
    class Success<T>(val data: T): ApiResponse
    class Error(val message: String): ApiResponse
}