package `in`.singhangad.todo

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "Todo APIs", version = "0.0.1", description = "Todo APIs v0.0.1"))
class TodoApplication

fun main(args: Array<String>) {
	runApplication<TodoApplication>(*args)
}
