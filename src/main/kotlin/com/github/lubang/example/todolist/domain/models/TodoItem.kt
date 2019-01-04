package com.github.lubang.example.todolist.domain.models

import org.joda.time.DateTime
import java.util.*

class TodoItem(
    val id: UUID,
    key: String,
    message: String,
    dependentIds: Set<UUID>,
    isCompleted: Boolean,
    val writtenAt: DateTime,
    modifiedAt: DateTime
) {
    var key: String = key
        set(value) {
            field = value
            modifiedAt = DateTime()
        }

    var message: String = message
        set(value) {
            field = value
            modifiedAt = DateTime()
        }

    var modifiedAt: DateTime = modifiedAt
        private set

    var isCompleted: Boolean = isCompleted
        private set(value) {
            field = value
            modifiedAt = DateTime()
        }

    var dependentIds: Set<UUID> = dependentIds
        private set(value) {
            field = value
            modifiedAt = DateTime()
        }

    fun dependOn(dependentId: UUID) {
        val mutableSet = this.dependentIds.toMutableSet()
        mutableSet.add(dependentId)
        this.dependentIds = mutableSet
    }

    fun complete() {
        this.isCompleted = true
    }

    fun incomplete() {
        this.isCompleted = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TodoItem

        if (id != other.id) return false
        if (writtenAt != other.writtenAt) return false
        if (key != other.key) return false
        if (message != other.message) return false
        if (modifiedAt != other.modifiedAt) return false
        if (isCompleted != other.isCompleted) return false
        if (dependentIds != other.dependentIds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + writtenAt.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + modifiedAt.hashCode()
        result = 31 * result + isCompleted.hashCode()
        result = 31 * result + dependentIds.hashCode()
        return result
    }
}
