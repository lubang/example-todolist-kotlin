package com.github.lubang.example.todolist.domain.models

import com.github.lubang.example.todolist.port.adapter.persistent.ExposedConnector
import com.github.lubang.example.todolist.port.adapter.persistent.ExposedTodoItemRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TodoItemRepositoryTest {

    @BeforeTest
    fun setup() {
        val properties = Properties()
        properties["dataSourceClassName"] = "org.h2.jdbcx.JdbcDataSource"
        properties["dataSource.url"] = "jdbc:h2:mem:test"
        ExposedConnector.initialize(properties)
    }

    @AfterTest
    fun teardown() {
        transaction {
            exec("DROP TABLE ${ExposedTodoItemRepository.TodoItems.tableName}")
        }
    }

    @Test
    fun `should be saved with a message in the database`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()

        // Act
        val todoItem = repository.save("Read a book at 8:00pm")

        // Assert
        assertEquals(1L, todoItem.id)
        assertEquals("Read a book at 8:00pm", todoItem.message)
        assertTrue(todoItem.dependentIds.isEmpty())
    }

    @Test
    fun `should be saved with a message and a dependency`() {
        val repository: TodoItemRepository = ExposedTodoItemRepository()
        val todoItem = repository.save("This is a TodoItem message")
        val dependentItems = mutableSetOf(todoItem.id)

        val actual = repository.save("This is 2st TodoItem Item", dependentItems)

        assertEquals(dependentItems, actual.dependentIds)
    }
}