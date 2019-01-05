package com.github.lubang.example.todolist.application

import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import javax.inject.Inject

class TodoItemService @Inject constructor(
    private val todoItemRepository: TodoItemRepository
) {
    fun createTodoItem(message: String): TodoItem {
        val todoItem = TodoItem.create(message)
        val id = todoItemRepository.save(todoItem)
        return todoItemRepository.findById(id)
    }

    fun modify(id: Long, message: String): TodoItem {
        val todoItem = todoItemRepository.findById(id)
        todoItem.editMessage(message)
        todoItemRepository.update(todoItem)
        return todoItem
    }
}
