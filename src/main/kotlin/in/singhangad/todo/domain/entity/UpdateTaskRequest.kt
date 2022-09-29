package `in`.singhangad.todo.domain.entity

class UpdateTaskRequest  (
    val taskTitle: String = "",
    val taskDescription: String? = null,
    val isPinned: Boolean ?= null,
    val endDate: Long = -1
)

fun UpdateTaskRequest.toRemoteTask(): RemoteTask {
    return RemoteTask(
        null,
        this.taskTitle,
        this.taskDescription,
        this.isPinned!!,
        null,
        this.endDate
    )
}