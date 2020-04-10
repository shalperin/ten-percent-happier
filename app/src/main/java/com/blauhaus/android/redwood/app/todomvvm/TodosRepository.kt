package com.blauhaus.android.redwood.app.todomvvm

import android.util.Log
import androidx.lifecycle.*
import com.blauhaus.android.redwood.app.common.DataOrException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*


//typealias ListTodosOrException = DataOrException<List<Todo>, Exception>
typealias ListOrException<T> = DataOrException<List<T>, Exception>

class TodoFirestoreRepository(val firestore: FirebaseFirestore, val auth: FirebaseAuth): ITodoRepository {
    val TAG = "TODO-REPO"
    val PATH_ROOT = "todoMVVM"
    val PATH_APPDATA = "appdata"
    val PATH_USERS = "users"
    val PATH_TODOS = "todos"
    val FIELD_TIMESTAMP = "timestamp"
    val FIELD_COMPLETE = "complete"
    val FIELD_TEXT = "text"

    val CACHE_KEY_ALL_TODOS = "todo_query"

    var userCache = mutableMapOf<String, Any>()

    init {
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "invalidating repo in-memory cache")
                invalidateUserCache()
            }
        }
    }

    fun invalidateUserCache() {
        userCache  = mutableMapOf<String, Any>()

    }

    fun allTodosQuery(): Query {
        var uid = auth.uid.toString()
        return firestore.collection(PATH_ROOT)
            .document(PATH_APPDATA)
            .collection(PATH_USERS)
            .document(uid)
            .collection(PATH_TODOS)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
    }

    override fun getTodo(todoId: String): TodoLiveData {
        val ref: DocumentReference = firestore.collection(PATH_ROOT)
            .document(PATH_APPDATA)
            .collection(PATH_USERS)
            .document(auth.uid.toString())
            .collection(PATH_TODOS)
            .document(todoId)
        return TodoLiveData(ref)
    }

    fun executeTodosQuery(query: Query) : LiveData<ListOrException<TodoOrException>> {

        return Transformations.map( TodoQueryLiveData(query) ) {
            if (it.data == null) {
                var exception = it.exception
                ListOrException(null, exception)
            } else {
                var list = it.data.map{ snapshot -> deserializeTodo(snapshot)}
                ListOrException(
                    list,
                    null
                )
            }
        }
    }

    override fun getAllTodosByTimestamp() : LiveData<ListOrException<TodoOrException>> {
        Log.d(TAG, "getAllTodosByTimestamp()")
        if (!userCache.containsKey(CACHE_KEY_ALL_TODOS)) {
            Log.d(TAG, "getAllTodosByTimestamp cache miss")
            userCache.put(CACHE_KEY_ALL_TODOS,  executeTodosQuery(allTodosQuery()))
        }
        return userCache[CACHE_KEY_ALL_TODOS] as LiveData<ListOrException<TodoOrException>>
    }

    override fun toggleTodoComplete(id: String, complete:Boolean) {
        firestore.collection(PATH_ROOT)
            .document(PATH_APPDATA)
            .collection(PATH_USERS)
            .document(auth.uid.toString())
            .collection(PATH_TODOS)
            .document(id).update(FIELD_COMPLETE, complete)
    }

    //todo can this throw errors?
    //what if I'm no longer logged in or something and I try to write?
    override fun addTodo(text:String) {
        val todo = hashMapOf(
            "text" to text,
            "complete" to false,
            "timestamp" to Timestamp(Date())
        )

        firestore.collection(PATH_ROOT)
            .document(PATH_APPDATA)
            .collection(PATH_USERS)
            .document(auth.uid.toString())
            .collection(PATH_TODOS)
            .add(todo)
    }

    override fun markAllTodosCompletion(complete:Boolean) {
        allTodosQuery().get().addOnSuccessListener { documents ->
            for (document in documents) {
                toggleTodoComplete(document.id, complete)
            }
        }
    }

    override fun deleteTodo(id: String) {
        firestore.collection(PATH_ROOT)
            .document(PATH_APPDATA)
            .collection(PATH_USERS)
            .document(auth.uid.toString())
            .collection(PATH_TODOS)
            .document(id).delete()
    }

    override fun updateTodo(id:String, newText: String) {
        firestore.collection(PATH_ROOT)
            .document(PATH_APPDATA)
            .collection(PATH_USERS)
            .document(auth.uid.toString())
            .collection(PATH_TODOS)
            .document(id)
            .update(FIELD_TEXT, newText)
    }
}

interface ITodoRepository {
    fun getTodo(todoId:String): TodoLiveData
    fun getAllTodosByTimestamp() : LiveData<ListOrException<TodoOrException>>
    fun toggleTodoComplete(id: String, complete:Boolean)
    fun addTodo(text: String)
    fun markAllTodosCompletion(complete:Boolean)
    fun deleteTodo(id: String)
    fun updateTodo(id: String, newText:String)
}