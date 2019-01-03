package com.github.lubang.example.todolist.domain.models

import org.joda.time.DateTime

interface TodoItemRepository {
    fun save(
        message: String,
        dependentIds: Set<Long> = setOf(),
        writtenAt: DateTime = DateTime(),
        modifiedAt: DateTime = DateTime()
    ): TodoItem
}
