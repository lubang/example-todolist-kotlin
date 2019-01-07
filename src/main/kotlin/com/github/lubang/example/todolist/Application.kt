package com.github.lubang.example.todolist

import com.github.lubang.example.todolist.port.adapter.web.KtorWebModule
import com.google.inject.Guice
import io.ktor.application.Application


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Guice.createInjector(
        ApplicationModule(this),
        KtorWebModule(this)
    )
}