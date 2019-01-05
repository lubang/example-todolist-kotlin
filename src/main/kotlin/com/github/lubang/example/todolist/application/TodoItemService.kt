package com.github.lubang.example.todolist.application

import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject

class TodoItemService @Inject constructor(
    private val todoItemRepository: TodoItemRepository
) {
    fun create(key: String, message: String): TodoItem {
        val todoItem = TodoItem(
            UUID.randomUUID(),
            key,
            message,
            setOf(),
            false,
            DateTime(),
            DateTime()
        )
        todoItemRepository.save(todoItem)
        return todoItem
    }
}
