package com.github.lubang.example.todolist.domain.models

import org.joda.time.DateTime

class TodoItem(
    message: String,
    dependentItems: Set<TodoItem> = setOf(),
    val writtenAt: DateTime = DateTime(),
    modifiedAt: DateTime = DateTime()
) {

    var message: String = message
        private set

    var dependentItems: Set<TodoItem> = dependentItems
        private set

    var modifiedAt: DateTime = modifiedAt
        private set

    var isDone: Boolean = false
        private set

    fun modify(
        message: String? = null,
        dependentItems: Set<TodoItem>? = null
    ) {
        if (this.isDone) {
            throw IllegalStateException("TodoItem could not modified cause TodoItem was done")
        }
        if (message == null && dependentItems == null) {
            throw IllegalArgumentException("TodoItem should be required a non-null parameter")
        }

        if (message != null) this.message = message
        if (dependentItems != null) this.dependentItems = dependentItems
        this.modifiedAt = DateTime()
    }

    fun done() {
        this.isDone = true
    }

}
