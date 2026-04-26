package com.example.homework1.domain.repository

import com.example.homework1.TodoItem
import com.example.homework1.data.local.LocalDataSource
import com.example.homework1.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class TodoRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    
    val items: Flow<List<TodoItem>> = localDataSource.items

    suspend fun loadItems() {
        Timber.d("TodoRepository: загрузка списка дел")
        // Сначала загружаем из кэша
        localDataSource.loadItems()
        
        // Затем пытаемся загрузить с бэкенда
        try {
            val remoteItems = remoteDataSource.loadItems()
            if (remoteItems.isNotEmpty()) {
                // Сохраняем полученные данные в кэш
                localDataSource.saveItems(remoteItems)
            }
        } catch (e: Exception) {
            Timber.e(e, "TodoRepository: ошибка при загрузке с бэкенда, используем кэш")
        }
    }

    suspend fun getItem(uid: String): TodoItem? {
        Timber.d("TodoRepository: получение дела $uid")
        // Сначала пытаемся получить из кэша
        val localItem = localDataSource.getItem(uid)
        if (localItem != null) {
            Timber.d("TodoRepository: дело $uid найдено в кэше")
            return localItem
        }
        
        // Если не найдено в кэше, пытаемся загрузить с бэкенда
        try {
            val remoteItem = remoteDataSource.getItem(uid)
            if (remoteItem != null) {
                // Сохраняем в кэш
                localDataSource.saveItem(remoteItem)
                Timber.d("TodoRepository: дело $uid загружено с бэкенда и сохранено в кэш")
                return remoteItem
            }
        } catch (e: Exception) {
            Timber.e(e, "TodoRepository: ошибка при загрузке дела $uid с бэкенда")
        }
        
        return null
    }

    suspend fun saveItem(item: TodoItem) {
        Timber.d("TodoRepository: сохранение дела ${item.uid}")
        // Сохраняем в кэш сразу
        localDataSource.saveItem(item)
        
        // Отправляем на бэкенд асинхронно
        try {
            remoteDataSource.saveItem(item)
        } catch (e: Exception) {
            Timber.e(e, "TodoRepository: ошибка при отправке дела ${item.uid} на бэкенд")
        }
    }

    suspend fun deleteItem(uid: String) {
        Timber.d("TodoRepository: удаление дела $uid")
        // Удаляем из кэша сразу
        localDataSource.deleteItem(uid)
        
        // Удаляем с бэкенда асинхронно
        try {
            remoteDataSource.deleteItem(uid)
        } catch (e: Exception) {
            Timber.e(e, "TodoRepository: ошибка при удалении дела $uid с бэкенда")
        }
    }
}

