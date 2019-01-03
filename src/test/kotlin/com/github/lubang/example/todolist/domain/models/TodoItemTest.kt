package com.github.lubang.example.todolist.domain.models

import org.joda.time.DateTime
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

internal class TodoItemTest {

    @Test
    fun `should be changed a modifiedAt to current time when it modifies a field`() {
        val todoItem = TodoItem(
            1L,
            "Wake up 8:00 to climb a rock",
            mutableSetOf(),
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
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
        val todoItem = TodoItem(
            1L,
            "This is a TodoItem message",
            mutableSetOf(),
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        todoItem.done()

        assertEquals(true, todoItem.isDone)
    }

    @Test
    fun `should be thrown IllegalStateException when it's done`() {
        val todoItem = TodoItem(
            1L,
            "This is a TodoItem message",
            mutableSetOf(),
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )
        todoItem.done()

        val exception = assertFailsWith(IllegalStateException::class) {
            todoItem.modify("It'll be thrown!")
        }
        assertEquals("TodoItem could not modified cause TodoItem was done", exception.message)
    }

    @Test
    fun `should be thrown IllegalArgumentException when it's modified with null`() {
        val todoItem = TodoItem(
            1L,
            "This is a TodoItem message",
            mutableSetOf(),
            DateTime("2019-10-19T00:00:00+09:00"),
            DateTime("2019-10-19T00:00:00+09:00")
        )

        val exception = assertFailsWith(IllegalArgumentException::class) {
            todoItem.modify()
        }
        assertEquals("TodoItem should be required a non-null parameter", exception.message)
    }

}