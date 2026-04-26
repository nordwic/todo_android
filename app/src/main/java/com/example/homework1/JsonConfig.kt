package com.example.homework1

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val module = SerializersModule {
    contextual(LocalDateTimeSerializer)
}

val appJson = Json {
    serializersModule = module
    prettyPrint = true
}
