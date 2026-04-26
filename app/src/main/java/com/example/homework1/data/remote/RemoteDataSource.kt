package com.example.homework1.data.remote

import com.example.homework1.TodoItem
import kotlinx.coroutines.delay
import timber.log.Timber

class RemoteDataSource {
    
    suspend fun loadItems(): List<TodoItem> {
        Timber.d("RemoteDataSource: загрузка дел с бэкенда (заглушка)")
        // Имитация сетевой задержки
        delay(500)
        // Заглушка - возвращаем пустой список
        // В реальной реализации здесь будет HTTP запрос
        Timber.d("RemoteDataSource: получено 0 дел с бэкенда (заглушка)")
        return emptyList()
    }

    suspend fun getItem(uid: String): TodoItem? {
        Timber.d("RemoteDataSource: загрузка дела $uid с бэкенда (заглушка)")
        delay(300)
        // Заглушка - возвращаем null
        Timber.d("RemoteDataSource: дело $uid не найдено на бэкенде (заглушка)")
        return null
    }

    suspend fun saveItem(item: TodoItem) {
        Timber.d("RemoteDataSource: отправка дела ${item.uid} на бэкенд (заглушка)")
        delay(400)
        // Заглушка - просто логируем
        // В реальной реализации здесь будет HTTP POST/PUT запрос
        Timber.d("RemoteDataSource: дело ${item.uid} отправлено на бэкенд (заглушка)")
    }

    suspend fun deleteItem(uid: String) {
        Timber.d("RemoteDataSource: удаление дела $uid с бэкенда (заглушка)")
        delay(300)
        // Заглушка - просто логируем
        // В реальной реализации здесь будет HTTP DELETE запрос
        Timber.d("RemoteDataSource: дело $uid удалено с бэкенда (заглушка)")
    }
}

