package com.example.homework1.data.local

import com.example.homework1.FileStorage
import com.example.homework1.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class LocalDataSource(private val fileStorage: FileStorage) {
    
    private val _items = MutableStateFlow<List<TodoItem>>(emptyList())
    val items: Flow<List<TodoItem>> = _items.asStateFlow()

    suspend fun loadItems() {
        try {
            fileStorage.loadFromFile()
            _items.value = fileStorage.items
            Timber.d("LocalDataSource: загружено ${_items.value.size} дел из кэша")
        } catch (e: Exception) {
            Timber.e(e, "LocalDataSource: ошибка при загрузке из кэша")
        }
    }

    suspend fun getItem(uid: String): TodoItem? {
        return try {
            fileStorage.getItem(uid)
        } catch (e: Exception) {
            Timber.e(e, "LocalDataSource: ошибка при получении дела $uid")
            null
        }
    }

    suspend fun saveItem(item: TodoItem) {
        try {
            val existingItem = fileStorage.getItem(item.uid)
            if (existingItem != null) {
                fileStorage.update(item)
            } else {
                fileStorage.add(item)
            }
            fileStorage.saveToFile()
            _items.value = fileStorage.items
            Timber.d("LocalDataSource: сохранено дело ${item.uid} в кэш")
        } catch (e: Exception) {
            Timber.e(e, "LocalDataSource: ошибка при сохранении в кэш")
        }
    }

    suspend fun deleteItem(uid: String) {
        try {
            fileStorage.delete(uid)
            fileStorage.saveToFile()
            _items.value = fileStorage.items
            Timber.d("LocalDataSource: удалено дело $uid из кэша")
        } catch (e: Exception) {
            Timber.e(e, "LocalDataSource: ошибка при удалении из кэша")
        }
    }

    suspend fun saveItems(items: List<TodoItem>) {
        try {
            items.forEach { item ->
                val existingItem = fileStorage.getItem(item.uid)
                if (existingItem != null) {
                    fileStorage.update(item)
                } else {
                    fileStorage.add(item)
                }
            }
            fileStorage.saveToFile()
            _items.value = fileStorage.items
            Timber.d("LocalDataSource: сохранено ${items.size} дел в кэш")
        } catch (e: Exception) {
            Timber.e(e, "LocalDataSource: ошибка при сохранении списка в кэш")
        }
    }
}

