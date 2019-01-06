package com.github.lubang.example.todolist.port.adapter.web.api

import com.github.lubang.example.todolist.application.TodoItemService
import com.github.lubang.example.todolist.domain.models.TodoItem
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.joda.time.DateTime
import javax.inject.Inject

class KtorRouteApi @Inject constructor(
    application: Application,
    todoItemService: TodoItemService
) {
    init {
        application.routing {
            static("/") {
                resources("/static")
                defaultResource("/static/index.html")
            }

            route("/api/todo-items") {
                post {
                    val request = call.receive<CreateTodoItemRequest>()
                    val todoItem: TodoItem = todoItemService.createTodoItem(request.message)
                    val response = toTodoItemResponse(todoItem)
                    call.respond(HttpStatusCode.Created, response)
                }

                get {
                    val offset = call.request.queryParameters["offset"]!!.toInt()
                    val count = call.request.queryParameters["count"]!!.toInt()
                    val (todoItems, totalCount) = todoItemService.findByPage(offset, count)
                    val response = TodoItemPageResponse(
                        offset,
                        count,
                        totalCount,
                        todoItems.map { i -> toTodoItemResponse(i) }
                    )
                    call.respond(HttpStatusCode.OK, response)
                }

                route("/{id}") {
                    put {
                        val id = call.parameters["id"]!!.toLong()
                        val request = call.receive<EditTodoItemRequest>()
                        val todoItem: TodoItem = todoItemService.editMessage(id, request.message)
                        val response = toTodoItemResponse(todoItem)
                        call.respond(HttpStatusCode.OK, response)
                    }

                    delete {
                        val id = call.parameters["id"]!!.toLong()
                        todoItemService.delete(id)
                        call.respond(HttpStatusCode.OK)
                    }

                    post("/completion") {
                        val id = call.parameters["id"]!!.toLong()
                        val todoItem: TodoItem = todoItemService.complete(id)
                        val response = toTodoItemResponse(todoItem)
                        call.respond(HttpStatusCode.OK, response)
                    }

                    delete("/completion") {
                        val id = call.parameters["id"]!!.toLong()
                        val todoItem: TodoItem = todoItemService.incomplete(id)
                        val response = toTodoItemResponse(todoItem)
                        call.respond(HttpStatusCode.OK, response)
                    }

                    post("/dependencies") {
                        val id = call.parameters["id"]!!.toLong()
                        val request = call.receive<AddDependencyRequest>()
                        val todoItem: TodoItem = todoItemService.addDependency(id, request.dependencyId)
                        val response = toTodoItemResponse(todoItem)
                        call.respond(HttpStatusCode.OK, response)
                    }

                    delete("/dependencies") {
                        val id = call.parameters["id"]!!.toLong()
                        val request = call.receive<RemoveDependencyRequest>()
                        val todoItem: TodoItem = todoItemService.removeDependency(id, request.dependencyId)
                        val response = toTodoItemResponse(todoItem)
                        call.respond(HttpStatusCode.OK, response)
                    }
                }
            }
        }
    }

    private fun toTodoItemResponse(todoItem: TodoItem): TodoItemResponse {
        return TodoItemResponse(
            todoItem.id,
            todoItem.message,
            todoItem.dependentIds,
            todoItem.completed,
            todoItem.writtenAt,
            todoItem.modifiedAt,
            todoItem.completedAt
        )
    }

    data class CreateTodoItemRequest(
        val message: String
    )

    data class EditTodoItemRequest(
        val message: String,
        val dependentIds: Set<Long>,
        val completed: Boolean
    )

    data class AddDependencyRequest(
        val dependencyId: Long
    )

    data class RemoveDependencyRequest(
        val dependencyId: Long
    )

    data class TodoItemResponse(
        val id: Long,
        val message: String,
        val dependentIds: Set<Long>,
        val completed: Boolean,
        val writtenAt: DateTime,
        val modifiedAt: DateTime,
        val completedAt: DateTime?
    )

    data class TodoItemPageResponse(
        val offset: Int,
        val count: Int,
        val totalCount: Int,
        val results: List<TodoItemResponse>
    )
}