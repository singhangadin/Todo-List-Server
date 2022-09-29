package `in`.singhangad.todo.data.repository

import `in`.singhangad.todo.data.entity.Task
import org.springframework.data.repository.CrudRepository

interface TaskRepository: CrudRepository<Task, Long>