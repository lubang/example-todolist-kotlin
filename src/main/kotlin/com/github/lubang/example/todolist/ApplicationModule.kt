package com.github.lubang.example.todolist

import com.github.lubang.example.todolist.domain.models.TodoItemRepository
import com.github.lubang.example.todolist.port.adapter.persistent.ExposedTodoItemRepository
import com.google.inject.AbstractModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import org.h2.tools.Server
import org.jetbrains.exposed.sql.Database
import java.util.concurrent.Executors

class ApplicationModule(private val application: Application) : AbstractModule() {
    init {
        initializeDatabase()
    }

    override fun configure() {
        bind(TodoItemRepository::class.java).to(ExposedTodoItemRepository::class.java)
    }

    private fun initializeDatabase() {
        val config = HikariConfig()
        config.dataSourceClassName = "org.h2.jdbcx.JdbcDataSource"
        config.dataSourceProperties["url"] = "jdbc:h2:mem:todolist"
        config.validate()

        val database = Database.connect(HikariDataSource(config))
        launchH2Web(database)
    }

    private fun launchH2Web(database: Database) {
        @Suppress("EXPERIMENTAL_API_USAGE")
        val h2Web = application.environment.config.propertyOrNull("database.h2.web")?.getString()
            ?: "false"
        if (h2Web.toBoolean()) {
            Executors.newSingleThreadExecutor().submit {
                Server.startWebServer(database.connector())
            }
        }
    }
}
