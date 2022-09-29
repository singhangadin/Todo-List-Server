package `in`.singhangad.todo.domain.entity

class RemoteTask (
    val taskId: Long ?= null,
    val taskTitle: String,
    val taskDescription: String? = null,
    val isPinned: Boolean = false,
    val createdAt: Long? = null,
    val endDate: Long
)