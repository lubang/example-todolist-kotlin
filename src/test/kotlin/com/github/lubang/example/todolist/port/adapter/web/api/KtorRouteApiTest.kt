package com.github.lubang.example.todolist.port.adapter.web.api

import com.github.lubang.example.todolist.TestApplicationModule
import com.github.lubang.example.todolist.port.adapter.serialization.GsonUtility
import com.github.lubang.example.todolist.port.adapter.web.KtorWebModule
import com.google.inject.Guice
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class KtorRouteApiTest {
    private val gson = GsonUtility.createInstance()

    @Test
    fun `route POST |api|todo-items should call a create of TodoItemService`() {
        // Arrange
        withTestApplication({
            Guice.createInjector(
                TestApplicationModule(),
                KtorWebModule(this)
            )
        }) {
            // Act
            handleRequest(HttpMethod.Post, "/api/todo-items") {
                val request = KtorRouteApi.CreateTodoItemRequest(
                    "This is a TodoItem message"
                )
                setBody(gson.toJson(request))
            }.apply {
                val actual = gson.fromJson(
                    response.content,
                    KtorRouteApi.TodoItemResponse::class.java
                )

                // Assert
                assertEquals(HttpStatusCode.Created, response.status())
                assertNotNull(actual.id)
                assertEquals("This is a TodoItem message", actual.message)
                assertEquals(setOf(), actual.dependentIds)
                assertEquals(false, actual.isCompleted)
                assertNotNull(actual.writtenAt)
                assertNotNull(actual.modifiedAt)
            }
        }
    }

    @Test
    fun `route PUT |api|todo-items|{id} should call a modify of TodoItemService`() {
        // Arrange
        withTestApplication({
            Guice.createInjector(
                TestApplicationModule(),
                KtorWebModule(this)
            )
        }) {
            val createResponse = handleRequest(HttpMethod.Post, "/api/todo-items") {
                val request = KtorRouteApi.CreateTodoItemRequest(
                    "This is a TodoItem message"
                )
                setBody(gson.toJson(request))
            }
            val todoItemResponse = gson.fromJson(
                createResponse.response.content,
                KtorRouteApi.TodoItemResponse::class.java
            )


            // Act
            handleRequest(HttpMethod.Put, "/api/todo-items/${todoItemResponse.id}") {
                val request = KtorRouteApi.ModifyTodoItemContentRequest(
                    "This is a TodoItem message (Modified message)"
                )
                setBody(gson.toJson(request))
            }.apply {
                val actual = gson.fromJson(
                    response.content,
                    KtorRouteApi.TodoItemResponse::class.java
                )

                // Assert
                assertEquals(HttpStatusCode.OK, response.status())
                assertNotNull(actual.id)
                assertEquals("This is a TodoItem message (Modified message)", actual.message)
                assertEquals(setOf(), actual.dependentIds)
                assertEquals(false, actual.isCompleted)
                assertNotNull(actual.writtenAt)
                assertNotNull(actual.modifiedAt)
            }
        }
    }
}