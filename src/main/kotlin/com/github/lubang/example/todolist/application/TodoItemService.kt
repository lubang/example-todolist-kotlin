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

    fun editMessage(id: Long, message: String): TodoItem {
        val todoItem = todoItemRepository.findById(id)
        todoItem.editMessage(message)
        todoItemRepository.update(todoItem)
        return todoItem
    }

    fun complete(id: Long): TodoItem {
        val todoItem = todoItemRepository.findById(id)
        todoItem.complete()
        todoItemRepository.update(todoItem)
        return todoItem
    }

    fun incomplete(id: Long): TodoItem {
        val todoItem = todoItemRepository.findById(id)
        todoItem.incomplete()
        todoItemRepository.update(todoItem)
        return todoItem
    }

    fun delete(id: Long) {
        todoItemRepository.delete(id)
    }

    fun addDependency(id: Long, dependencyId: Long): TodoItem {
        val todoItem = todoItemRepository.findById(id)
        todoItem.addDependentId(dependencyId)
        todoItemRepository.update(todoItem)
        return todoItem
    }

    fun removeDependency(id: Long, dependencyId: Long): TodoItem {
        val todoItem = todoItemRepository.findById(id)
        todoItem.removeDependentId(dependencyId)
        todoItemRepository.update(todoItem)
        return todoItem
    }

    fun findByPage(offset: Int, count: Int): Pair<List<TodoItem>, Int> {
        val totalCount = todoItemRepository.countAll()
        val results = todoItemRepository.find(offset, count)
        return Pair(results, totalCount)
    }
}
