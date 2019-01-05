package com.github.lubang.example.todolist.domain.models

import org.joda.time.DateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

internal class TodoItemTest {
    @Test
    fun `create with a message`() {
        // Arrange
        val message = "Wake up 8:00 to climb a rock"

        // Act
        val actual = TodoItem.create(message)

        // Assert
        assertEquals("Wake up 8:00 to climb a rock", actual.message)
    }

    @Test
    fun `create with all arguments for a persisted data`() {
        // Arrange

        // Act
        val actual = TodoItem.create(
            1L,
            "Wake up 8:00 to climb a rock",
            setOf(),
            true,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:01:00+09:00"),
            DateTime("2019-10-19T00:02:00+09:00")
        )

        // Assert
        assertEquals(1L, actual.id)
        assertEquals("Wake up 8:00 to climb a rock", actual.message)
        assertEquals(setOf(), actual.dependentIds)
        assertEquals(true, actual.completed)
        assertEquals(DateTime("2019-10-19T00:00:00+09:00"), actual.writtenAt)
        assertEquals(DateTime("2019-10-19T00:01:00+09:00"), actual.modifiedAt)
        assertEquals(DateTime("2019-10-19T00:02:00+09:00"), actual.completedAt)
    }

    @Test
    fun `equals and hashcode`() {
        // Arrange

        // Act
        val todoItem1 = TodoItem.create(
            1L,
            "Wake up 8:00 to climb a rock",
            setOf(),
            true,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:01:00+09:00"),
            DateTime("2019-10-19T00:02:00+09:00")
        )
        val todoItem2 = TodoItem.create(
            1L,
            "Wake up 8:00 to climb a rock",
            setOf(),
            true,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:01:00+09:00"),
            DateTime("2019-10-19T00:02:00+09:00")
        )

        // Assert
        assertEquals(todoItem1, todoItem2)
        assertEquals(todoItem1.hashCode(), todoItem2.hashCode())
    }

    @Test
    fun `edit a message should change a modified time`() {
        // Arrange
        val todoItem = TodoItem.create("Wake up 8:00 to climb a rock")

        // Act
        todoItem.editMessage("Wake up 9:00 to go swim!")

        // Assert
        assertEquals("Wake up 9:00 to go swim!", todoItem.message)
        assertNotNull(todoItem.modifiedAt)
    }

    @Test
    fun `complete should change a completed and a completed time`() {
        // Arrange
        val todoItem = TodoItem.create(
            1L,
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00"),
            null
        )

        // Act
        todoItem.complete()

        // Arrange
        assertEquals(true, todoItem.completed)
        assertNotNull(todoItem.completedAt)
    }

    @Test
    fun `incomplete should change a completed and a completed time`() {
        // Arrange
        val todoItem = TodoItem.create(
            1L,
            "Wake up 8:00 to climb a rock",
            setOf(),
            true,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:01:00+09:00"),
            DateTime("2019-10-19T00:02:00+09:00")
        )

        // Act
        todoItem.incomplete()

        // Assert
        assertEquals(false, todoItem.completed)
        assertEquals(null, todoItem.completedAt)
    }

    @Test
    fun `add a dependent Id should add an Id in a dependent Id set`() {
        // Arrange
        val todoItem = TodoItem.create(
            2L,
            "Wake up 8:00 to climb a rock",
            setOf(),
            true,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:01:00+09:00"),
            null
        )
        val dependentId = 1L

        // Act
        todoItem.addDependentId(dependentId)

        // Assert
        assertEquals(setOf(1L), todoItem.dependentIds)
        assertNotEquals(DateTime("2019-10-19T00:01:00+09:00"), todoItem.modifiedAt)
    }

    @Test
    fun `remove a dependent Id should remove an Id in a dependent Id set`() {
        // Arrange
        val todoItem = TodoItem.create(
            2L,
            "Wake up 8:00 to climb a rock",
            setOf(1L),
            true,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:01:00+09:00"),
            null
        )
        val dependentId = 1L

        // Act
        todoItem.removeDependentId(dependentId)

        // Assert
        assertEquals(setOf(), todoItem.dependentIds)
        assertNotEquals(DateTime("2019-10-19T00:01:00+09:00"), todoItem.modifiedAt)
    }
}