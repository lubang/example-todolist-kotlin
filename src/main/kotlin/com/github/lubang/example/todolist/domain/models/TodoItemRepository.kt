package com.github.lubang.example.todolist.domain.models

import java.util.*

interface TodoItemRepository {
    fun save(todoItem: TodoItem)
    fun findById(id: UUID): TodoItem
    fun find(offset: Int, count: Int): List<TodoItem>
    fun delete(id: UUID)
}
