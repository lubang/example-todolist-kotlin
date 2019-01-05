package com.github.lubang.example.todolist.port.adapter.persistent

import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ExposedTodoItemRepositoryTest {
    @BeforeTest
    fun setup() {
        val config = HikariConfig()
        config.dataSourceClassName = "org.h2.jdbcx.JdbcDataSource"
        config.dataSourceProperties["url"] = "jdbc:h2:mem:test"
        config.validate()

        Database.connect(HikariDataSource(config))
    }

    @AfterTest
    fun teardown() {
        transaction {
            ExposedTodoItemRepository.TodoItems.deleteAll()
            ExposedTodoItemRepository.Dependencies.deleteAll()
        }
    }

    @Test
    fun `save a todo item with an unique Id`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()
        val todoItem = TodoItem.create("Wake up 8:00 to climb a rock")

        // Act
        val id = repository.save(todoItem)
        val actual = repository.findById(id)

        // Assert
        assertEquals(todoItem.message, actual.message)
    }

    @Test
    fun `find by ID should be with dependent IDs`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()

        val todoItem1 = TodoItem.create("Wake up 8:00 to climb a rock 1")
        val todoItem2 = TodoItem.create("Wake up 8:00 to climb a rock 2")
        val todoItem1Id = repository.save(todoItem1)
        val todoItem2Id = repository.save(todoItem2)

        val todoItem = TodoItem.create("Wake up 8:00 to climb a rock 3")
        todoItem.addDependentId(todoItem1Id)
        todoItem.addDependentId(todoItem2Id)
        val todoItemId = repository.save(todoItem)

        // Act
        val actual = repository.findById(todoItemId)

        // Assert
        assertEquals("Wake up 8:00 to climb a rock 3", actual.message)
        assertEquals(setOf(todoItem1Id, todoItem2Id), actual.dependentIds)
    }

    @Test
    fun `delete a todo item should remove a dependency in others`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()

        val todoItem1 = TodoItem.create("Wake up 8:00 to climb a rock 1")
        val todoItem2 = TodoItem.create("Wake up 8:00 to climb a rock 2")
        val todoItem1Id = repository.save(todoItem1)
        val todoItem2Id = repository.save(todoItem2)

        val todoItem = TodoItem.create("Wake up 8:00 to climb a rock 3")
        todoItem.addDependentId(todoItem1Id)
        todoItem.addDependentId(todoItem2Id)
        val todoItemId = repository.save(todoItem)

        // Act
        repository.delete(todoItem1Id)
        val actual = repository.findById(todoItemId)

        // Assert
        assertEquals("Wake up 8:00 to climb a rock 3", actual.message)
        assertEquals(setOf(todoItem2Id), actual.dependentIds)
    }

    @Test
    fun `find a result with an offset and a count`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()
        for (i in 0L..15L) {
            val todoItem = TodoItem.create("Wake up 8:00 to climb a rock $i")
            repository.save(todoItem)
        }

        // Act
        val actual = repository.find(10, 2)

        // Assert
        assertEquals(2, actual.size)
        assertEquals(11, actual[0].id)
        assertEquals(12, actual[1].id)
    }

    @Test
    fun `update a todo item with an added dependency`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()

        val todoItem1 = TodoItem.create("Wake up 8:00 to climb a rock 1")
        val todoItem2 = TodoItem.create("Wake up 8:00 to climb a rock 2")
        val todoItem1Id = repository.save(todoItem1)
        val todoItem2Id = repository.save(todoItem2)
        val todoItem3 = TodoItem.create("Wake up 8:00 to climb a rock 3")
        todoItem3.addDependentId(todoItem1Id)
        val todoItem3Id = repository.save(todoItem3)

        // Act
        val actual = repository.findById(todoItem3Id)
        actual.addDependentId(todoItem2Id)
        repository.update(actual)

        // Assert
        assertEquals("Wake up 8:00 to climb a rock 3", actual.message)
        assertEquals(setOf(todoItem1Id, todoItem2Id), actual.dependentIds)
    }

    @Test
    fun `update a todo item with an removed dependency`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()

        val todoItem1 = TodoItem.create("Wake up 8:00 to climb a rock 1")
        val todoItem2 = TodoItem.create("Wake up 8:00 to climb a rock 2")
        val todoItem1Id = repository.save(todoItem1)
        val todoItem2Id = repository.save(todoItem2)
        val todoItem3 = TodoItem.create("Wake up 8:00 to climb a rock 3")
        todoItem3.addDependentId(todoItem1Id)
        todoItem3.addDependentId(todoItem2Id)
        val todoItem3Id = repository.save(todoItem3)

        // Act
        val actual = repository.findById(todoItem3Id)
        actual.removeDependentId(todoItem2Id)
        repository.update(actual)

        // Assert
        assertEquals("Wake up 8:00 to climb a rock 3", actual.message)
        assertEquals(setOf(todoItem1Id), actual.dependentIds)
    }
}