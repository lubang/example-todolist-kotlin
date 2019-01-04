package com.github.lubang.example.todolist.port.adapter.persistent

import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ExposedTodoItemRepository : TodoItemRepository {
    init {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(TodoItems)
            SchemaUtils.createMissingTablesAndColumns(Dependencies)
        }
    }

    override fun save(todoItem: TodoItem) {
        transaction {
            TodoItems.insert {
                it[TodoItems.id] = todoItem.id
                it[TodoItems.key] = todoItem.key
                it[TodoItems.message] = todoItem.message
                it[TodoItems.isCompleted] = todoItem.isCompleted
                it[TodoItems.writtenAt] = todoItem.writtenAt
                it[TodoItems.modifiedAt] = todoItem.modifiedAt
            }

            todoItem.dependentIds.forEach { dependentId ->
                Dependencies.insert {
                    it[Dependencies.todoItemId] = todoItem.id
                    it[Dependencies.dependentId] = dependentId
                }
            }

        }
    }

    override fun findById(id: UUID): TodoItem {
        return transaction {
            TodoItems
                .select { TodoItems.id eq id }
                .limit(1)
                .map { toTodoItem(it) }
                .first()
        }
    }

    override fun delete(id: UUID) {
        transaction {
            TodoItems
                .deleteWhere { TodoItems.id eq id }
        }
    }

    object TodoItems : Table() {
        val id = uuid("id").primaryKey()
        val key = varchar("key", 100).uniqueIndex()
        val message = varchar("message", 250)
        val isCompleted = bool("isCompleted")
        val writtenAt = datetime("writtenAt")
        val modifiedAt = datetime("modifiedAt")
    }

    private fun toTodoItem(row: ResultRow): TodoItem {
        val id = row[TodoItems.id]

        val dependentIds = Dependencies
            .slice(Dependencies.dependentId)
            .select { Dependencies.todoItemId eq id }
            .map { it[Dependencies.dependentId] }
            .toSet()

        return TodoItem(
            row[TodoItems.id],
            row[TodoItems.key],
            row[TodoItems.message],
            dependentIds,
            row[TodoItems.isCompleted],
            row[TodoItems.writtenAt],
            row[TodoItems.modifiedAt]
        )
    }

    object Dependencies : Table() {
        val todoItemId = uuid("todoItemId")
            .references(TodoItems.id, ReferenceOption.CASCADE)
            .index()
        val dependentId = uuid("dependentId")
            .references(TodoItems.id, ReferenceOption.CASCADE)
            .index()
    }
}