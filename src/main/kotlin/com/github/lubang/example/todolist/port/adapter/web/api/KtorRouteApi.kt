package com.github.lubang.example.todolist.port.adapter.web.api

import com.github.lubang.example.todolist.application.TodoItemService
import com.github.lubang.example.todolist.domain.models.TodoItem
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.routing.routing
import org.joda.time.DateTime
import javax.inject.Inject

class KtorRouteApi @Inject constructor(
    application: Application,
    todoItemService: TodoItemService
) {
    init {
        application.routing {
            route("/api/todo-items") {
                post {
                    val request = call.receive<CreateTodoItemRequest>()
                    val todoItem: TodoItem = todoItemService.createTodoItem(request.message)
                    val response =
                        TodoItemResponse(
                            todoItem.id,
                            todoItem.message,
                            todoItem.dependentIds,
                            todoItem.completed,
                            todoItem.writtenAt,
                            todoItem.modifiedAt
                        )
                    call.respond(HttpStatusCode.Created, response)
                }

                route("/{id}") {
                    put {
                        val id = call.parameters["id"]!!.toLong()
                        val request = call.receive<ModifyTodoItemContentRequest>()
                        val todoItem: TodoItem =
                            todoItemService.modify(id, request.message)
                        val response =
                            TodoItemResponse(
                                todoItem.id,
                                todoItem.message,
                                todoItem.dependentIds,
                                todoItem.completed,
                                todoItem.writtenAt,
                                todoItem.modifiedAt
                            )
                        call.respond(HttpStatusCode.OK, response)
                    }
                }
            }
        }
    }

    data class CreateTodoItemRequest(
        val message: String
    )

    data class ModifyTodoItemContentRequest(
        val message: String
    )

    data class TodoItemResponse(
        val id: Long,
        val message: String,
        val dependentIds: Set<Long>,
        val isCompleted: Boolean,
        val writtenAt: DateTime,
        val modifiedAt: DateTime
    )
}