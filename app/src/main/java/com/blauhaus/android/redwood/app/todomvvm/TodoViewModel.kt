package com.blauhaus.android.redwood.app.todomvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.combine

class TodoViewModel(val repo: ITodoRepository): ViewModel() {
    enum class TodoFilterMode { ALL, COMPLETED, ACTIVE }

    var TAG = "TODO_ViewModel"
    var todoLiveDataCache = mutableMapOf<String, TodoLiveData>()
    val filterMode = MutableLiveData<TodoFilterMode>()

    fun getTodoLiveData(todoId: String): TodoLiveData {
        if (!todoLiveDataCache.containsKey(todoId)) {
            Log.d(TAG, "todo Live Data cache miss")
            todoLiveDataCache[todoId] = repo.getTodo(todoId)
        } else {
            Log.d(TAG, "todo live data cache hit")
        }
        return todoLiveDataCache[todoId]!!
    }

    fun toggleTodoComplete(id: String, complete: Boolean) {
        repo.toggleTodoComplete(id, complete)
    }

    fun addTodo(text: String) {
        repo.addTodo(text)
    }

    fun setFilterMode(mode: TodoFilterMode) {
        filterMode.postValue(mode)
    }

    var _allTodos: LiveData<ListOrException<TodoOrException>>? = null
    fun allTodos(): LiveData<ListOrException<TodoOrException>> {
        if (_allTodos == null) {
            _allTodos = repo.getAllTodosByTimestamp()
        }
        return _allTodos!!
    }


    fun getFilteredTodos(): LiveData<ListOrException<TodoOrException>> { return _filteredTodos }
    val _filteredTodos = MediatorLiveData<ListOrException<TodoOrException>>()
    private fun combineFilterResult(
        todos: LiveData<ListOrException<TodoOrException>>,
        mode: LiveData<TodoFilterMode>
    ) {

        val todos = todos.value
        val mode = mode.value

        if (todos != null && mode != null) {
            if (todos.data == null) {
                _filteredTodos.value = todos // repost the exception
            } else {
                when (mode) {
                    TodoFilterMode.ACTIVE -> {
                        val filteredData = todos.data.filter { it.data?.complete != true }
                        _filteredTodos.value = ListOrException(filteredData, null)
                    }
                    TodoFilterMode.ALL -> {
                        _filteredTodos.value = todos
                    }
                    TodoFilterMode.COMPLETED -> {
                        val filteredData = todos.data.filter { it.data?.complete == true }
                        _filteredTodos.value = ListOrException(filteredData, null)
                    }
                }
            }
        }
    }


    init {
        //todo we could create a local cache for repo.getAllTodosByTimestamp()
        _filteredTodos.addSource(filterMode) {
            combineFilterResult(allTodos(), filterMode)
        }
        _filteredTodos.addSource(allTodos()) {
            combineFilterResult(allTodos(), filterMode)
        }
        filterMode.value = TodoFilterMode.ALL
    }

}