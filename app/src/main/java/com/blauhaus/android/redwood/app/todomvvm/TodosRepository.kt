package com.blauhaus.android.redwood.app.todomvvm

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

    val allTodosQuery = firestore.collection(PATH_ROOT)
        .document(PATH_APPDATA)
        .collection(PATH_USERS)
        .document(auth.uid.toString())
        .collection(PATH_TODOS)
        .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)

    override fun getTodo(todoId: String): TodoLiveData {
        //TODO create a constant for the path below.
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
                ListOrException(null, it.exception)
            } else {
                ListOrException(
                    it.data.map{ snapshot -> deserializeTodo(snapshot)},
                    null
                )
            }
        }
    }

    override fun getAllTodosByTimestamp() : LiveData<ListOrException<TodoOrException>> {
        return executeTodosQuery(allTodosQuery)
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
        allTodosQuery.get().addOnSuccessListener { documents ->
            for (document in documents) {
                toggleTodoComplete(document.id, complete)
            }
        }
    }
}

interface ITodoRepository {
    fun getTodo(todoId:String): TodoLiveData
    fun getAllTodosByTimestamp() : LiveData<ListOrException<TodoOrException>>
    fun toggleTodoComplete(id: String, complete:Boolean)
    fun addTodo(text: String)
    fun markAllTodosCompletion(complete:Boolean)
}