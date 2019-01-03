package com.github.lubang.example.todolist.domain.models

import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("A TodoItem")
internal class TodoItemTest {

    @Test
    fun `should be created with a message`() {
        // Arrange

        // Act
        val actual = TodoItem("This is a TodoItem message")

        // Assert
        assertEquals("This is a TodoItem message", actual.message)
    }

    @Test
    fun `should be created with a message and a dependency`() {
        val todo = TodoItem("This is 1st TodoItem Item")

        val actual = TodoItem("This is 2st TodoItem Item", mutableSetOf(todo))

        assertEquals(mutableSetOf(todo), actual.dependentItems)
    }

    @Test
    fun `should be changed a modifiedAt to current time when it modifies a field`() {
        val todoItem = TodoItem(
            "Wake up 8:00 to climb a rock",
            writtenAt = DateTime("2019-10-19T00:00:00+09:00"),
            modifiedAt = DateTime("2019-10-19T00:00:00+09:00")
        )

        todoItem.modify(
            message = "Wake up 9:00 to go swim!"
        )

        assertEquals("Wake up 9:00 to go swim!", todoItem.message)
        assertEquals(DateTime("2019-10-19T00:00:00+09:00"), todoItem.writtenAt)
        assertNotEquals(DateTime("2019-10-19T00:00:00+09:00"), todoItem.modifiedAt)
    }

    @Test
    fun `should be done status when it's done`() {
        val todoItem = TodoItem("This is a TodoItem message")

        todoItem.done()

        assertEquals(true, todoItem.isDone)
    }

    @Test
    fun `should be thrown IllegalStateException when it's done`() {
        val todoItem = TodoItem("This is a TodoItem message")
        todoItem.done()

        val exception = assertThrows(IllegalStateException::class.java) {
            todoItem.modify("It'll be thrown!")
        }
        assertEquals("TodoItem could not modified cause TodoItem was done", exception.message)
    }

    @Test
    fun `should be thrown IllegalArgumentException when it's modified with null`() {
        val todoItem = TodoItem("This is a TodoItem message")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            todoItem.modify()
        }
        assertEquals("TodoItem should be required a non-null parameter", exception.message)
    }
}