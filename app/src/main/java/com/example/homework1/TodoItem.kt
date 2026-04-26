package com.example.homework1

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import android.graphics.Color
import java.util.UUID


enum class Importance{
    UNIMPORTANT, REGULAR, IMPORTANT
}

@Serializable
data class TodoItem(
    val uid: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance = Importance.REGULAR,
    val color: Int = Color.WHITE,
    @Serializable(with = LocalDateTimeSerializer::class)
    val deadLine: LocalDateTime? = null,
    val isDone: Boolean = false,
)
