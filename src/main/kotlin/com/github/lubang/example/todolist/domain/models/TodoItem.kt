package com.github.lubang.example.todolist.domain.models

import org.joda.time.DateTime

class TodoItem
private constructor(
    val id: Long,
    message: String,
    dependentIds: Set<Long> = setOf(),
    completed: Boolean = false,
    val writtenAt: DateTime = DateTime(),
    modifiedAt: DateTime = DateTime(),
    completedAt: DateTime? = null
) {
    var message: String = message
        private set

    var dependentIds: Set<Long> = dependentIds
        private set

    var completed: Boolean = completed
        private set

    var modifiedAt: DateTime = modifiedAt
        private set

    var completedAt: DateTime? = completedAt
        private set

    fun editMessage(message: String) {
        this.message = message
        this.modifiedAt = DateTime()
    }

    fun addDependentId(dependentId: Long) {
        val newDependentIds = this.dependentIds.toMutableSet()
        newDependentIds.add(dependentId)
        this.updateDependentIds(newDependentIds)
    }

    fun removeDependentId(dependentId: Long) {
        val newDependentIds = this.dependentIds.toMutableSet()
        newDependentIds.remove(dependentId)
        this.updateDependentIds(newDependentIds)
    }

    fun updateDependentIds(dependentIds: Set<Long>) {
        this.dependentIds = dependentIds
        this.modifiedAt = DateTime()
    }

    fun complete() {
        this.completed = true
        this.completedAt = DateTime()
    }

    fun incomplete() {
        this.completed = false
        this.completedAt = null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TodoItem

        if (id != other.id) return false
        if (writtenAt != other.writtenAt) return false
        if (message != other.message) return false
        if (dependentIds != other.dependentIds) return false
        if (completed != other.completed) return false
        if (modifiedAt != other.modifiedAt) return false
        if (completedAt != other.completedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + writtenAt.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + dependentIds.hashCode()
        result = 31 * result + completed.hashCode()
        result = 31 * result + modifiedAt.hashCode()
        result = 31 * result + (completedAt?.hashCode() ?: 0)
        return result
    }

    companion object {
        private const val TRANSIENT_ID = -1L

        fun create(message: String): TodoItem {
            return TodoItem(TRANSIENT_ID, message)
        }

        fun create(
            id: Long,
            message: String,
            dependentIds: Set<Long>,
            completed: Boolean,
            writtenAt: DateTime,
            modifiedAt: DateTime,
            completedAt: DateTime?
        ): TodoItem {
            return TodoItem(id, message, dependentIds, completed, writtenAt, modifiedAt, completedAt)
        }
    }
}
