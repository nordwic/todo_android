package com.example.homework1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import java.time.LocalDateTime
import android.graphics.Color
import android.app.Application
import timber.log.Timber

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppNavigation()
        }
    }

    private fun testFileStorage() {
        println("=== ТЕСТ FILESTORAGE ===")

        val storage = FileStorage(this)

        val testTask1 = TodoItem(
            uid = "uid1",
            text = "Задача 1",
            importance = Importance.IMPORTANT,
            color = Color.RED,
            deadLine = LocalDateTime.now().plusDays(1)
        )

        val testTask2 = TodoItem(
            uid = "uid2",
            text = "Задача 2",
            importance = Importance.REGULAR,
            color = Color.BLUE,
            deadLine = LocalDateTime.now().plusDays(2)
        )

        storage.add(testTask1)
        storage.add(testTask2)

        println("Текущие задачи:")
        storage.items.forEach { println("${it.uid}: ${it.text}") }

        storage.saveToFile()

        storage.loadFromFile()
        println("Загружено задач после сохранения и загрузки: ${storage.items.size}")

        storage.delete("uid1")
        println("Осталось задач после удаления uid1: ${storage.items.size}")

        storage.items.forEach { println("${it.uid}: ${it.text}") }
    }
}


@Composable
fun SplashScreen(
    onSplashCompleted: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onSplashCompleted()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text(
            text = "ToDo App",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Организуйте свои задачи",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}