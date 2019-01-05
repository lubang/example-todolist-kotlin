package com.github.lubang.example.todolist.port.adapter.web.api

import com.github.lubang.example.todolist.TestApplicationModule
import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import com.github.lubang.example.todolist.port.adapter.persistent.ExposedTodoItemRepository
import com.github.lubang.example.todolist.port.adapter.serialization.GsonUtility
import com.github.lubang.example.todolist.port.adapter.web.KtorWebModule
import com.google.inject.Guice
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

internal class KtorRouteApiTest {
    private val gson = GsonUtility.createInstance()

    @AfterTest
    fun teardown() {
        transaction {
            ExposedTodoItemRepository.TodoItems.deleteAll()
            ExposedTodoItemRepository.Dependencies.deleteAll()
        }
    }

    @Test
    fun `route POST |api|todo-items should call a create`() {
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
                assertEquals(false, actual.completed)
                assertNotNull(actual.writtenAt)
                assertNotNull(actual.modifiedAt)
                assertNull(actual.completedAt)
            }
        }
    }

    @Test
    fun `route PUT |api|todo-items|{id} should call a editMessage`() {
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
                val request = KtorRouteApi.EditTodoItemRequest(
                    "This is a TodoItem message (Modified message)",
                    setOf(),
                    false
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
                assertEquals(false, actual.completed)
                assertNotNull(actual.writtenAt)
                assertNotNull(actual.modifiedAt)
                assertNull(actual.completedAt)
            }
        }
    }

    @Test
    fun `route POST |api|todo-items|{id}|completion should call a complete`() {
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
            handleRequest(HttpMethod.Post, "/api/todo-items/${todoItemResponse.id}/completion")
                .apply {
                    val actual = gson.fromJson(
                        response.content,
                        KtorRouteApi.TodoItemResponse::class.java
                    )

                    // Assert
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertNotNull(actual.id)
                    assertEquals("This is a TodoItem message", actual.message)
                    assertEquals(setOf(), actual.dependentIds)
                    assertEquals(true, actual.completed)
                    assertNotNull(actual.writtenAt)
                    assertNotNull(actual.modifiedAt)
                    assertNotNull(actual.completedAt)
                }
        }
    }

    @Test
    fun `route DELETE |api|todo-items|{id}|completion should call a incomplete`() {
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
            handleRequest(HttpMethod.Delete, "/api/todo-items/${todoItemResponse.id}/completion")
                .apply {
                    val actual = gson.fromJson(
                        response.content,
                        KtorRouteApi.TodoItemResponse::class.java
                    )

                    // Assert
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertNotNull(actual.id)
                    assertEquals("This is a TodoItem message", actual.message)
                    assertEquals(setOf(), actual.dependentIds)
                    assertEquals(false, actual.completed)
                    assertNotNull(actual.writtenAt)
                    assertNotNull(actual.modifiedAt)
                    assertNull(actual.completedAt)
                }
        }
    }

    @Test
    fun `route DELETE |api|todo-items|{id} should call a delete`() {
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
            handleRequest(HttpMethod.Delete, "/api/todo-items/${todoItemResponse.id}")
                .apply {
                    // Assert
                    assertEquals(HttpStatusCode.OK, response.status())
                }
        }
    }

    @Test
    fun `route POST |api|todo-items|{id}|dependencies should call a add dependency`() {
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
            handleRequest(HttpMethod.Post, "/api/todo-items/${todoItemResponse.id}/dependencies") {
                val request = KtorRouteApi.AddDependencyRequest(
                    todoItemResponse.id
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
                assertEquals("This is a TodoItem message", actual.message)
                assertEquals(setOf(todoItemResponse.id), actual.dependentIds)
                assertEquals(false, actual.completed)
                assertNotNull(actual.writtenAt)
                assertNotNull(actual.modifiedAt)
                assertNull(actual.completedAt)
            }
        }
    }

    @Test
    fun `route DELETE |api|todo-items|{id}|dependencies should call a remove dependency`() {
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

            handleRequest(HttpMethod.Post, "/api/todo-items/${todoItemResponse.id}/dependencies") {
                val request = KtorRouteApi.AddDependencyRequest(
                    todoItemResponse.id
                )
                setBody(gson.toJson(request))
            }

            // Act
            handleRequest(HttpMethod.Delete, "/api/todo-items/${todoItemResponse.id}/dependencies") {
                val request = KtorRouteApi.RemoveDependencyRequest(
                    todoItemResponse.id
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
                assertEquals("This is a TodoItem message", actual.message)
                assertEquals(setOf(), actual.dependentIds)
                assertEquals(false, actual.completed)
                assertNotNull(actual.writtenAt)
                assertNotNull(actual.modifiedAt)
                assertNull(actual.completedAt)
            }
        }
    }

    @Test
    fun `route GET |api|todo-items?offset={offset}&count={count} should call a find`() {
        // Arrange
        withTestApplication({
            Guice.createInjector(
                TestApplicationModule(),
                KtorWebModule(this)
            )
        }) {
            val repository: TodoItemRepository = ExposedTodoItemRepository()
            for (i in 0L..19L) {
                val todoItem = TodoItem.create("Wake up 8:00 to climb a rock $i")
                repository.save(todoItem)
            }

            // Act
            handleRequest(HttpMethod.Get, "/api/todo-items?offset=10&count=5").apply {
                val actual = gson.fromJson(
                    response.content,
                    KtorRouteApi.TodoItemPageResponse::class.java
                )

                // Assert
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(10, actual.offset)
                assertEquals(5, actual.count)
                assertEquals(20, actual.totalCount)
                assertEquals(5, actual.results.size)
            }
        }
    }

    @Test
    fun `route GET |api|todo-items?offset=0&count=5 should call a find should return empty instance`() {
        // Arrange
        withTestApplication({
            Guice.createInjector(
                TestApplicationModule(),
                KtorWebModule(this)
            )
        }) {
            // Act
            handleRequest(HttpMethod.Get, "/api/todo-items?offset=0&count=5").apply {
                val actual = gson.fromJson(
                    response.content,
                    KtorRouteApi.TodoItemPageResponse::class.java
                )

                // Assert
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(0, actual.offset)
                assertEquals(5, actual.count)
                assertEquals(0, actual.totalCount)
                assertEquals(0, actual.results.size)
            }
        }
    }
}