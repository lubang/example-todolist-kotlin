package com.github.lubang.example.todolist.port.adapter.persistent

import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*
import kotlin.test.*

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
        }
    }

    @Test
    fun `should save and findById a TodoItem in H2 Database`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()
        val todoItem = TodoItem(
            UUID.randomUUID(),
            "1",
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        // Act
        repository.save(todoItem)
        val actual = repository.findById(todoItem.id)

        // Assert
        assertEquals(todoItem, actual)
    }

    @Test
    fun `should save and findById a TodoItem with dependencies`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()

        val todoItem1 = TodoItem(
            UUID.randomUUID(),
            "D1",
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )
        repository.save(todoItem1)

        val todoItem2 = TodoItem(
            UUID.randomUUID(),
            "D2",
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )
        repository.save(todoItem2)

        val todoItem = TodoItem(
            UUID.randomUUID(),
            "D3",
            "Wake up 8:00 to climb a rock",
            setOf(todoItem1.id, todoItem2.id),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        // Act
        repository.save(todoItem)
        val actual = repository.findById(todoItem.id)

        // Assert
        assertEquals(todoItem, actual)
    }

    @Test
    fun `should delete dependencies when a todo item is deleted`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()

        val todoItem1 = TodoItem(
            UUID.randomUUID(),
            "D11",
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )
        repository.save(todoItem1)

        val todoItem2 = TodoItem(
            UUID.randomUUID(),
            "D12",
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )
        repository.save(todoItem2)

        val todoItem = TodoItem(
            UUID.randomUUID(),
            "D13",
            "Wake up 8:00 to climb a rock",
            setOf(todoItem1.id, todoItem2.id),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )
        repository.save(todoItem)

        // Act
        repository.delete(todoItem2.id)
        val actual = repository.findById(todoItem.id)

        // Assert
        assertNotEquals(todoItem, actual)
        assertEquals(setOf(todoItem1.id), actual.dependentIds)
    }

    @Test
    fun `should be found result with a offset and a count`() {
        // Arrange
        val repository: TodoItemRepository = ExposedTodoItemRepository()
        for (i in 0..15) {
            val todoItem = TodoItem(
                UUID.randomUUID(),
                "P$i",
                "Wake up 8:00 to climb a rock",
                setOf(),
                false,
                DateTime("2019-10-19T00:00:00+09:00"),
                DateTime("2019-10-19T00:00:00+09:00")
            )
            repository.save(todoItem)
        }

        // Act
        val actual = repository.find(10, 2)

        // Assert
        assertEquals(2, actual.size)
        assertEquals("P10", actual[0].key)
        assertEquals("P11", actual[1].key)
    }
}