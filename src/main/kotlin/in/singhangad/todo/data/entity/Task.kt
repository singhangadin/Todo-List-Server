package `in`.singhangad.todo.data.entity

import `in`.singhangad.todo.domain.entity.RemoteTask
import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var taskId: Long? = null

    var taskTitle: String = ""
    var taskDescription: String? = null
    var isPinned: Boolean = false
    var createdAt: Long = Date().time
    var endDate: Long = Date().time

    constructor()

    constructor(
        taskId: Long?,
        taskTitle: String,
        taskDescription: String?,
        isPinned: Boolean,
        createdAt: Long,
        endDate: Long
    ) {
        this.taskId = taskId
        this.taskTitle = taskTitle
        this.taskDescription = taskDescription
        this.isPinned = isPinned
        this.createdAt = createdAt
        this.endDate = endDate
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is Task) {
            false
        } else {
            other.taskId == this.taskId &&
            other.taskTitle == this.taskTitle &&
            other.taskDescription == this.taskDescription &&
            other.isPinned == this.isPinned &&
            other.createdAt == this.createdAt &&
            other.endDate == this.endDate
        }
    }

    override fun hashCode(): Int {
        return taskId.hashCode()
    }
}

fun Task.toDomain(): RemoteTask {
    return RemoteTask(
        this.taskId,
        this.taskTitle,
        this.taskDescription,
        this.isPinned,
        this.createdAt,
        this.endDate
    )
}

fun RemoteTask.fromDomain(): Task {
    return Task(
        this.taskId,
        this.taskTitle!!,
        this.taskDescription,
        this.isPinned!!,
        this.createdAt!!,
        this.endDate!!
    )
}