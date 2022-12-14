package `in`.singhangad.todo.domain.controller

import `in`.singhangad.todo.data.entity.Task
import `in`.singhangad.todo.data.entity.fromDomain
import `in`.singhangad.todo.data.entity.toDomain
import `in`.singhangad.todo.data.repository.TaskRepository
import `in`.singhangad.todo.domain.ApiResponse
import `in`.singhangad.todo.domain.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/task")
class MainController {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @PostMapping
    @ResponseBody
    fun saveTask(@RequestBody task: CreateTaskRequest): ResponseEntity<ApiResponse> {
        val savedTask = taskRepository.save(task.toRemoteTask().fromDomain())
        return ResponseEntity.ok(ApiResponse.Success(savedTask.toDomain()))
    }

    @GetMapping("/all")
    @ResponseBody
    fun getAllTask(): ResponseEntity<ApiResponse> {
        val tasks = taskRepository.findAll().toList()
        return ResponseEntity.ok(ApiResponse.Success(tasks.map { it.toDomain() }))
    }

    @GetMapping("/{id}")
    @ResponseBody
    fun getTaskWithId(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        val savedTask = taskRepository.findByIdOrNull(id)
        return if (savedTask != null) {
            ResponseEntity.ok(ApiResponse.Success(savedTask.toDomain()))
        } else {
            ResponseEntity(ApiResponse.Error("Data not found"), HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody task: UpdateTaskRequest): ResponseEntity<ApiResponse> {
        val savedTask = taskRepository.findByIdOrNull(id)
        return if (task.taskTitle.isBlank()) {
            ResponseEntity(ApiResponse.Error("Title must not be null or empty"), HttpStatus.BAD_REQUEST)
        } else if (task.isPinned == null) {
            ResponseEntity(ApiResponse.Error("Property isPinned is missing"), HttpStatus.BAD_REQUEST)
        } else if (task.endDate == -1L || task.endDate == 0L) {
            ResponseEntity(ApiResponse.Error("The end date must be a valid date"), HttpStatus.BAD_REQUEST)
        } else if (task.endDate < System.currentTimeMillis()) {
            ResponseEntity(ApiResponse.Error("The end date must be greater than today"), HttpStatus.BAD_REQUEST)
        } else if (savedTask != null) {
            val newTask = Task(
                savedTask.taskId,
                task.taskTitle,
                task.taskDescription,
                task.isPinned,
                savedTask.createdAt,
                task.endDate
            )
            ResponseEntity.ok(ApiResponse.Success(taskRepository.save(newTask).toDomain()))
        } else {
            ResponseEntity(ApiResponse.Error("Data not found"), HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/pin/{id}")
    fun pinTask(@PathVariable id: Long, @RequestBody request: PinTaskRequest): ResponseEntity<ApiResponse> {
        val savedTask = taskRepository.findByIdOrNull(id)
        return if (savedTask == null) {
            ResponseEntity(ApiResponse.Error("Data not found"), HttpStatus.NOT_FOUND)
        } else {
            val newTask = Task(
                savedTask.taskId,
                savedTask.taskTitle,
                savedTask.taskDescription,
                request.isPinned,
                savedTask.createdAt,
                savedTask.endDate
            )
            ResponseEntity.ok(ApiResponse.Success(taskRepository.save(newTask).toDomain()))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        val savedTask = taskRepository.findByIdOrNull(id)
        return if (savedTask != null) {
            taskRepository.deleteById(id)
            ResponseEntity.ok(ApiResponse.Success("Task deleted"))
        } else {
            ResponseEntity(ApiResponse.Error("Data not found"), HttpStatus.NOT_FOUND)
        }
    }
}