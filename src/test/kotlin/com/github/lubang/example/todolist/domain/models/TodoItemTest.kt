package com.github.lubang.example.todolist.domain.models

import org.joda.time.DateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class TodoItemTest {
    @Test
    fun `should be created with a message`() {
        // Arrange
        val id = UUID.randomUUID()

        // Act
        val actual = TodoItem(
            id,
            "1",
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        // Assert
        assertEquals(id, actual.id)
        assertEquals("1", actual.key)
        assertEquals("Wake up 8:00 to climb a rock", actual.message)
    }

    @Test
    fun `should be changed a modifiedAt to current time when it modifies a message field`() {
        val todoItem = TodoItem(
            UUID.randomUUID(),
            "1",
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        todoItem.message = "Wake up 9:00 to go swim!"

        assertEquals("Wake up 9:00 to go swim!", todoItem.message)
        assertEquals(DateTime("2019-10-19T00:00:00+09:00"), todoItem.writtenAt)
        assertNotEquals(DateTime("2019-10-19T00:00:00+09:00"), todoItem.modifiedAt)
    }

    @Test
    fun `should be changed a modifiedAt to current time when it modifies a key field`() {
        val todoItem = TodoItem(
            UUID.randomUUID(),
            "1",
            "Wake up 8:00 to climb a rock",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        todoItem.key = "36"

        assertEquals("36", todoItem.key)
        assertEquals(DateTime("2019-10-19T00:00:00+09:00"), todoItem.writtenAt)
        assertNotEquals(DateTime("2019-10-19T00:00:00+09:00"), todoItem.modifiedAt)
    }

    @Test
    fun `should be done status when it's done`() {
        val todoItem = TodoItem(
            UUID.randomUUID(),
            "1",
            "This is a TodoItem message",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        todoItem.done()

        assertEquals(true, todoItem.isCompleted)
        assertNotEquals(DateTime("2019-10-19T00:00:00+09:00"), todoItem.modifiedAt)
    }

    @Test
    fun `should be equals with the exactly same values`() {
        val id = UUID.randomUUID()
        val firstItem = TodoItem(
            id,
            "1",
            "This is a TodoItem message",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )
        val secondItem = TodoItem(
            id,
            "1",
            "This is a TodoItem message",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        assertEquals(firstItem, secondItem)
        assertEquals(firstItem.hashCode(), secondItem.hashCode())
    }

    @Test
    fun `should be depended on an other topic item`() {
        val dependentId = UUID.randomUUID()
        val todoItem = TodoItem(
            UUID.randomUUID(),
            "1",
            "This is a TodoItem message",
            setOf(),
            false,
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        todoItem.dependOn(dependentId)

        assertEquals(dependentId, todoItem.dependentIds.first())
        assertNotEquals(DateTime("2019-10-19T00:00:00+09:00"), todoItem.modifiedAt)
    }

}