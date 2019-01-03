package com.github.lubang.example.todolist.port.adapter.persistent

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import java.util.*

object ExposedConnector {

    fun initialize(properties: Properties) {
        val config = HikariConfig(properties)
        config.validate()

        Database.connect(HikariDataSource(config))
    }

}