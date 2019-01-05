package com.github.lubang.example.todolist.domain.models

interface TodoItemRepository {
    fun save(todoItem: TodoItem): Long
    fun findById(id: Long): TodoItem
    fun find(offset: Int, count: Int): List<TodoItem>
    fun delete(id: Long)
    fun update(todoItem: TodoItem)
}
