package `in`.singhangad.todo.domain.entity

class CreateTaskRequest (
    val taskTitle: String = "",
    val taskDescription: String? = null,
    val isPinned: Boolean = false,
    val endDate: Long
)

fun CreateTaskRequest.toRemoteTask(): RemoteTask {
    return RemoteTask(
        null,
        this.taskTitle,
        this.taskDescription,
        this.isPinned,
        System.currentTimeMillis(),
        this.endDate
    )
}