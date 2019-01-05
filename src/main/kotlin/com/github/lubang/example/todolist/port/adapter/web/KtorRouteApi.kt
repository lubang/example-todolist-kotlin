package com.github.lubang.example.todolist.port.adapter.web

import com.github.lubang.example.todolist.application.TodoItemService
import com.github.lubang.example.todolist.domain.models.TodoItem
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.joda.time.DateTime
import java.util.*
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
                    val todoItem: TodoItem = todoItemService.create(request.key, request.message)
                    val response = TodoItemResponse(
                        todoItem.id,
                        todoItem.key,
                        todoItem.message,
                        todoItem.dependentIds,
                        todoItem.isCompleted,
                        todoItem.writtenAt,
                        todoItem.modifiedAt
                    )
                    call.respond(response)
                }
            }
        }
    }

    data class CreateTodoItemRequest(
        val key: String,
        val message: String
    )

    data class TodoItemResponse(
        val id: UUID,
        val key: String,
        val message: String,
        val dependentIds: Set<UUID>,
        val isCompleted: Boolean,
        val writtenAt: DateTime,
        val modifiedAt: DateTime
    )
}