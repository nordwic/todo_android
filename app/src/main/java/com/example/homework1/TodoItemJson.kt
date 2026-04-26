package com.example.homework1
import org.json.JSONObject
import android.graphics.Color
import java.time.LocalDateTime

fun TodoItem.parse (json: JSONObject): TodoItem? {
    return try {
        val uid = json.getString("uid")
        val text = json.getString("text")
        val importance = if (json.has("importance")) {
            Importance.valueOf(json.getString("importance"))
        } else {
            Importance.REGULAR
        }
        val color = if (json.has("color")) json.getInt("color") else Color.WHITE
        val deadLine = if(json.has("deadLine")) {
            LocalDateTime.parse(json.getString("deadLine"))
        } else {
            null
        }
        val isDone = json.getBoolean("isDone")

        TodoItem(uid, text, importance, color, deadLine, isDone)
    } catch (_: Exception) {
        return null
    }
}

val TodoItem.json: JSONObject
    get() = JSONObject().apply {
        put("text", text)
        put("uid", uid)
        put("isDone", isDone)

        if (color != Color.WHITE) {
            put("color", color)
        }

        if (importance != Importance.REGULAR) {
            put("importance", importance)
        }

        if (deadLine != null) {
            put("deadLine", deadLine)
        }

    }