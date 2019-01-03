package com.github.lubang.example.todolist.domain.models

import com.github.lubang.example.todolist.cores.Entity
import org.joda.time.DateTime

class TodoItem(
    override val id: Long,
    message: String,
    dependentIds: Set<Long>,
    val writtenAt: DateTime,
    modifiedAt: DateTime
) : Entity {

    var message: String = message
        private set

    var dependentIds: Set<Long> = dependentIds
        private set

    var modifiedAt: DateTime = modifiedAt
        private set

    var isDone: Boolean = false
        private set

    fun modify(
        message: String? = null,
        dependentIds: Set<Long>? = null
    ) {
        if (this.isDone) {
            throw IllegalStateException("TodoItem could not modified cause TodoItem was done")
        }
        if (message == null && dependentIds == null) {
            throw IllegalArgumentException("TodoItem should be required a non-null parameter")
        }

        if (message != null) this.message = message
        if (dependentIds != null) this.dependentIds = dependentIds
        this.modifiedAt = DateTime()
    }

    fun done() {
        this.isDone = true
    }
}
