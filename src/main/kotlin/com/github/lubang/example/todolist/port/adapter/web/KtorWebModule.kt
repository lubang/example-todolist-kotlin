package com.github.lubang.example.todolist.port.adapter.web

import com.github.lubang.example.todolist.port.adapter.serialization.GsonUtility
import com.github.lubang.example.todolist.port.adapter.web.api.KtorRouteApi
import com.google.inject.AbstractModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson

class KtorWebModule(private val application: Application) : AbstractModule() {
    init {
        application.install(ContentNegotiation) {
            gson {
                GsonUtility.configure(this)
            }
        }
    }

    override fun configure() {
        bind(KtorRouteApi::class.java).asEagerSingleton()
        bind(Application::class.java).toInstance(application)
    }
}