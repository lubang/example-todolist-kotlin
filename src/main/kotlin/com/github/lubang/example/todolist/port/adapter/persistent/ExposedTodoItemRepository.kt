package com.github.lubang.example.todolist.port.adapter.persistent

import com.github.lubang.example.todolist.domain.models.TodoItem
import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class ExposedTodoItemRepository : TodoItemRepository {

    init {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(ExposedTodoItemRepository.TodoItems)
        }
    }

    override fun save(
        message: String,
        dependentIds: Set<Long>,
        writtenAt: DateTime,
        modifiedAt: DateTime
    ): TodoItem {
        return transaction {
            val id = TodoItems.insert {
                it[TodoItems.message] = message
                it[TodoItems.dependentIds] = dependentIds.joinToString(",")
                it[TodoItems.writtenAt] = writtenAt
                it[TodoItems.modifiedAt] = modifiedAt
            } get TodoItems.id

            TodoItems.select { TodoItems.id eq id!! }.limit(1).map {
                TodoItems.toTodoItem(it)
            }.first()
        }
    }

    object TodoItems : Table() {
        val id = long("id").primaryKey().autoIncrement()
        val message = varchar("message", 250)
        val dependentIds = varchar("dependentIds", 250)
        val writtenAt = datetime("writtenAt")
        val modifiedAt = datetime("modifiedAt")

        fun toTodoItem(row: ResultRow): TodoItem {
            return TodoItem(
                row[TodoItems.id],
                row[TodoItems.message],
                toDependentIds(row[dependentIds]),
                row[TodoItems.writtenAt],
                row[TodoItems.modifiedAt]
            )
        }

        private fun toDependentIds(ids: String): Set<Long> {
            if (ids.isEmpty()) {
                return setOf()
            }
            return ids
                .split(",")
                .map { it.toLong() }
                .toSet()
        }
    }
}