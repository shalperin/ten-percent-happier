package com.blauhaus.android.redwood.app.todomvvm

import androidx.lifecycle.*
import com.blauhaus.android.redwood.app.common.DataOrException
import com.blauhaus.android.redwood.app.login.LoginViewModel

class TodoViewModel(val repo: ITodoRepository): ViewModel() {
    private var TAG = TodoViewModel::class.java.simpleName

    enum class TodoFilterMode { ALL, COMPLETED, ACTIVE }
    val _filteredTodos = MediatorLiveData<List<TodoOrException>>()
    var _incompleteCount:LiveData<Int>? = null
    var _allTodosComplete: LiveData<Boolean>? = null
    var _authState = MutableLiveData<LoginViewModel.AuthenticationState>()
    val filterMode = MutableLiveData<TodoFilterMode>()

    fun setAuthState(state: LoginViewModel.AuthenticationState) {
        _authState.value = state
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

    fun markAllTodosCompletion(complete: Boolean) {
        repo.markAllTodosCompletion(complete)
    }

    fun deleteTodo(id: String) {
        repo.deleteTodo(id)
    }

    fun updateTodo(id:String, newText: String) {
        repo.updateTodo(id, newText)
    }

    var _queryResult = Transformations.switchMap(_authState){
        if (_authState.value == LoginViewModel.AuthenticationState.AUTHENTICATED) {
            // do not fire this unless authenticated
            // otherwise you will get a dead query
            // because it has uid in its path, and it would be null.
            repo.getAllTodosByTimestamp()
        } else {
            MutableLiveData<ListOrException<TodoOrException>>()
        }
    }

    var _allTodos = Transformations.map(_queryResult){
        if (it.data == null) {
            //Todo emit on error LD to handle exception
            listOf<DataOrException<Todo, Exception>>()
        } else {
            it.data
        }
    }

    //Emit a filtered list of todos based on the filter mode.
    fun combineForFilteredTodosMediator(todos: LiveData<List<TodoOrException>>,
                                        filterMode: LiveData<TodoViewModel.TodoFilterMode>) {

        val todos:List<TodoOrException>? = todos.value
        val filterMode: TodoViewModel.TodoFilterMode? = filterMode.value

        if(todos != null && filterMode!=null) {
            when (filterMode) {
                TodoViewModel.TodoFilterMode.ACTIVE -> {
                    val filteredData = todos.filter { it.data?.complete != true }
                    _filteredTodos.value = filteredData
                }
                TodoViewModel.TodoFilterMode.ALL -> {
                    _filteredTodos.value = todos
                }
                TodoViewModel.TodoFilterMode.COMPLETED -> {
                    val filteredData = todos.filter { it.data?.complete == true }
                    _filteredTodos.value = filteredData
                }
            }
        }
    }

    fun allTodosAreComplete(): LiveData<Boolean> {
        if (_allTodosComplete == null) {
            _allTodosComplete = Transformations.switchMap(_allTodos){
                val retval = MutableLiveData<Boolean>()
                retval.value = it.all { todoOrException ->
                    if (todoOrException.data != null) {
                        todoOrException.data.complete
                    } else {
                        false
                    }
                }
                retval
            }
        }
        return _allTodosComplete!!
    }

    fun incompleteCount():LiveData<Int> {
        if (_incompleteCount == null) {
            _incompleteCount = Transformations.switchMap(_allTodos) {
                val retval = MutableLiveData<Int>()
                var incompleteCount = it.count { todoOrException ->
                    if (todoOrException.data != null) {
                        !todoOrException.data.complete
                    } else {
                        false
                    }
                }
                retval.value = incompleteCount
                retval
            }
        }
        return _incompleteCount!!
    }



    init {
        _filteredTodos.addSource(_allTodos) {
            combineForFilteredTodosMediator(_allTodos, filterMode)
        }
        _filteredTodos.addSource(filterMode) {
            combineForFilteredTodosMediator(_allTodos, filterMode)
        }

        filterMode.value = TodoFilterMode.ALL
    }
}

