package com.example.homework1

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homework1.data.local.LocalDataSource
import com.example.homework1.data.remote.RemoteDataSource
import com.example.homework1.domain.repository.TodoRepository
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    startDestination: String = "list"
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val fileStorage = remember { FileStorage(context) }
    val localDataSource = remember { LocalDataSource(fileStorage) }
    val remoteDataSource = remember { RemoteDataSource() }
    val repository = remember { TodoRepository(localDataSource, remoteDataSource) }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("list") {
            TodoListScreen(
                repository = repository,
                onAddClick = { navController.navigate("edit_new") },
                onEditClick = { id -> navController.navigate("edit/$id") }
            )
        }
        composable("edit_new") {
            EditTodoScreen(
                initial = null,
                onSave = { item ->
                    scope.launch {
                        repository.saveItem(item)
                        navController.popBackStack()
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable("edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            var item by remember { mutableStateOf<TodoItem?>(null) }
            
            LaunchedEffect(id) {
                if (id != null) {
                    item = repository.getItem(id)
                }
            }
            
            EditTodoScreen(
                initial = item,
                onSave = { savedItem ->
                    scope.launch {
                        repository.saveItem(savedItem)
                        navController.popBackStack()
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}