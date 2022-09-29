package `in`.singhangad.todo.domain.controller

import `in`.singhangad.todo.data.entity.Task
import `in`.singhangad.todo.data.entity.fromDomain
import `in`.singhangad.todo.data.entity.toDomain
import `in`.singhangad.todo.data.repository.TaskRepository
import `in`.singhangad.todo.domain.entity.CreateTaskRequest
import `in`.singhangad.todo.domain.entity.RemoteTask
import `in`.singhangad.todo.domain.entity.UpdateTaskRequest
import `in`.singhangad.todo.domain.entity.toRemoteTask
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/todo")
class MainController {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @PostMapping
    @ResponseBody
    fun saveTask(@RequestBody task: CreateTaskRequest): ResponseEntity<RemoteTask> {
        val savedTask = taskRepository.save(task.toRemoteTask().fromDomain())
        return ResponseEntity.ok(savedTask.toDomain())
    }

    @GetMapping
    @ResponseBody
    fun getAllTask(): ResponseEntity<List<RemoteTask>> {
        val tasks = taskRepository.findAll().toList()
        return ResponseEntity.ok(tasks.map { it.toDomain() })
    }

    @GetMapping("/{id}")
    @ResponseBody
    fun getTaskWithId(@PathVariable id: Long): ResponseEntity<RemoteTask> {
        val savedTask = taskRepository.findByIdOrNull(id)
        return if (savedTask != null) {
            ResponseEntity.ok(savedTask.toDomain())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody task: UpdateTaskRequest): ResponseEntity<RemoteTask> {
        val savedTask = taskRepository.findByIdOrNull(id)
        return if (task.taskTitle.isNullOrBlank() || task.isPinned == null || task.endDate == -1L || task.endDate == 0L) {
            ResponseEntity.badRequest().build()
        } else if (savedTask != null) {
            val newTask = Task(
                savedTask.taskId,
                task.taskTitle,
                task.taskDescription,
                task.isPinned,
                savedTask.createdAt,
                task.endDate
            )
            ResponseEntity.ok(taskRepository.save(newTask).toDomain())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Unit> {
        val savedTask = taskRepository.findByIdOrNull(id)
        return if (savedTask != null) {
            taskRepository.deleteById(id)
            ResponseEntity.ok(Unit)
        } else {
            ResponseEntity.notFound().build()
        }

    }
}