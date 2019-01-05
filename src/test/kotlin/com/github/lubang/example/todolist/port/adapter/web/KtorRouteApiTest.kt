package com.github.lubang.example.todolist.port.adapter.web

import com.github.lubang.example.todolist.module
import com.github.lubang.example.todolist.port.adapter.serialization.GsonUtility
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class KtorRouteApiTest {
    @Test
    fun `route |api|todo-items|{id} should invoke a create of TodoItemService`() {
        // Arrange
        val gson = GsonUtility.createInstance()
        val request = KtorRouteApi.CreateTodoItemRequest(
            "1",
            "This is a TodoItem message"
        )

        // Act
        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Post, "/api/todo-items") {
                setBody(gson.toJson(request))
            }.apply {
                val actual = gson.fromJson(
                    response.content,
                    KtorRouteApi.TodoItemResponse::class.java
                )

                // Assert
                assertEquals(HttpStatusCode.OK, response.status())
                assertNotNull(actual.id)
                assertEquals("1", actual.key)
                assertEquals("This is a TodoItem message", actual.message)
                assertEquals(setOf(), actual.dependentIds)
                assertEquals(false, actual.isCompleted)
                assertNotNull(actual.writtenAt)
                assertNotNull(actual.modifiedAt)
            }
        }
    }
}