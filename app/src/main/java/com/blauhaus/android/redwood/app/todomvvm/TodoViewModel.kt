package com.blauhaus.android.redwood.app.todomvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class TodoViewModel(val repo: ITodoRepository): ViewModel( ) {
    var TAG="TODO_ViewModel"
    var todoLiveDataCache= mutableMapOf<String, TodoLiveData>()

    fun getTodoLiveData(todoId:String):TodoLiveData {
        if (!todoLiveDataCache.containsKey(todoId)) {
            Log.d(TAG, "todo Live Data cache miss")
            todoLiveDataCache[todoId] = repo.getTodo(todoId)
        } else {
            Log.d(TAG, "todo live data cache hit")
        }
        return todoLiveDataCache[todoId]!!
    }

    fun getAllTodosByTimestampAsc(): LiveData<ListOrException<TodoOrException>> {
        return repo.getAllTodosByTimestampAsc()
    }


}