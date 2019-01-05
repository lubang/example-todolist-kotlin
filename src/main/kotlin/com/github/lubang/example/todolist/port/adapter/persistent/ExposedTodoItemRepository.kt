package com.github.lubang.example.todolist.port.adapter.persistent

import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedTodoItemRepository : TodoItemRepository {
    init {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(TodoItems)
            SchemaUtils.createMissingTablesAndColumns(Dependencies)
        }
    }

    override fun save(todoItem: TodoItem): Long {
        return transaction {
            val id = TodoItems.insert {
                it[TodoItems.message] = todoItem.message
                it[TodoItems.completed] = todoItem.completed
                it[TodoItems.writtenAt] = todoItem.writtenAt
                it[TodoItems.modifiedAt] = todoItem.modifiedAt
                it[TodoItems.completedAt] = todoItem.completedAt
            } get TodoItems.id

            todoItem.dependentIds.forEach { dependentId ->
                Dependencies.insert {
                    it[Dependencies.todoItemId] = id!!
                    it[Dependencies.dependentId] = dependentId
                }
            }

            id!!
        }
    }

    override fun findById(id: Long): TodoItem {
        return transaction {
            TodoItems
                .select { TodoItems.id eq id }
                .limit(1)
                .map { toTodoItem(it) }
                .first()
        }
    }

    override fun find(offset: Int, count: Int): List<TodoItem> {
        return transaction {
            TodoItems
                .selectAll()
                .orderBy(TodoItems.writtenAt)
                .limit(count, offset)
                .map { toTodoItem(it) }
                .toList()
        }
    }

    override fun delete(id: Long) {
        transaction {
            TodoItems
                .deleteWhere { TodoItems.id eq id }
        }
    }

    override fun update(todoItem: TodoItem) {
        transaction {
            TodoItems
                .update({ TodoItems.id eq todoItem.id }) {
                    it[TodoItems.message] = todoItem.message
                    it[TodoItems.completed] = todoItem.completed
                    it[TodoItems.writtenAt] = todoItem.writtenAt
                    it[TodoItems.modifiedAt] = todoItem.modifiedAt
                    it[TodoItems.completedAt] = todoItem.completedAt
                }

            if (!todoItem.dependentIds.isEmpty()) {
                val dependencies = Dependencies
                    .slice(Dependencies.dependentId)
                    .select { Dependencies.todoItemId eq todoItem.id }
                    .map { it[Dependencies.dependentId] }

                if (todoItem.dependentIds != dependencies) {
                    val addIds = todoItem.dependentIds.subtract(dependencies)
                    for (addId in addIds) {
                        Dependencies.insert {
                            it[Dependencies.todoItemId] = todoItem.id
                            it[Dependencies.dependentId] = addId
                        }
                    }

                    val removeIds = dependencies.subtract(todoItem.dependentIds)
                    for (removeId in removeIds) {
                        Dependencies.deleteWhere {
                            (Dependencies.todoItemId eq todoItem.id)
                                .and(Dependencies.dependentId eq removeId)
                        }
                    }
                }
            }
        }
    }

    object TodoItems : Table() {
        val id = long("id").autoIncrement().primaryKey()
        val message = varchar("message", 250)
        val completed = bool("completed")
        val writtenAt = datetime("writtenAt")
        val modifiedAt = datetime("modifiedAt")
        val completedAt = datetime("completedAt").nullable()
    }

    private fun toTodoItem(row: ResultRow): TodoItem {
        val id = row[TodoItems.id]

        val dependentIds = Dependencies
            .slice(Dependencies.dependentId)
            .select { Dependencies.todoItemId eq id }
            .map { it[Dependencies.dependentId] }
            .toSet()

        return TodoItem.create(
            row[TodoItems.id],
            row[TodoItems.message],
            dependentIds,
            row[TodoItems.completed],
            row[TodoItems.writtenAt],
            row[TodoItems.modifiedAt],
            row[TodoItems.completedAt]
        )
    }

    object Dependencies : Table() {
        val todoItemId = long("todoItemId")
            .references(TodoItems.id, ReferenceOption.CASCADE)
            .index()
        val dependentId = long("dependentId")
            .references(TodoItems.id, ReferenceOption.CASCADE)
            .index()
    }
}