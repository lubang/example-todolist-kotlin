package com.github.lubang.example.todolist

import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import com.github.lubang.example.todolist.port.adapter.persistent.ExposedTodoItemRepository
import com.google.inject.AbstractModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class TestApplicationModule : AbstractModule() {
    init {
        initializeDatabase()
    }

    override fun configure() {
        bind(TodoItemRepository::class.java).to(ExposedTodoItemRepository::class.java)
    }

    private fun initializeDatabase() {
        val config = HikariConfig()
        config.dataSourceClassName = "org.h2.jdbcx.JdbcDataSource"
        config.dataSourceProperties["url"] = "jdbc:h2:mem:test"
        config.validate()

        Database.connect(HikariDataSource(config))
    }
}
