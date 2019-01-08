package com.github.lubang.example.todolist.application

import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import javax.inject.Inject

class TodoItemService @Inject constructor(
    private val todoItemRepository: TodoItemRepository
) {
    fun createTodoItem(
        message: String,
        dependentIds: Set<Long>
    ): TodoItem {
        val todoItem = TodoItem.create(message)
        todoItem.updateDependentIds(dependentIds)
        val id = todoItemRepository.save(todoItem)
        return todoItemRepository.findById(id)
    }

    fun editMessage(
        id: Long,
        message: String,
        dependentIds: Set<Long>
    ): TodoItem {
        val todoItem = todoItemRepository.findById(id)
        todoItem.editMessage(message)
        todoItem.updateDependentIds(dependentIds)
        todoItemRepository.update(todoItem)
        return todoItem
    }

    fun complete(id: Long): TodoItem {
        val todoItem = todoItemRepository.findById(id)
        todoItem.dependentIds.forEach {
            if (!todoItemRepository.getCompleted(it)) {
                throw InvalidDependentCompletionException("TodoItem requires to complete that all dependencies are completed")
            }
        }
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
