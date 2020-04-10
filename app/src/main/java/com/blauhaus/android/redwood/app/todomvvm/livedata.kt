package com.blauhaus.android.redwood.app.todomvvm

import android.os.Handler
import androidx.lifecycle.LiveData
import com.blauhaus.android.redwood.app.common.DataOrException
import com.google.firebase.firestore.*


typealias TodoOrException = DataOrException<Todo, Exception>
typealias DocumentSnapshotsOrException = DataOrException<List<DocumentSnapshot>?, FirebaseFirestoreException>

class TodoLiveData(private val ref: DocumentReference): LiveData<TodoOrException>(), EventListener<DocumentSnapshot> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = ref.addSnapshotListener(this)
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }

    override fun onEvent(snapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (snapshot != null) {
            setValue(deserializeTodo(snapshot))
        } else {
            setValue(TodoOrException(null, e))
        }
    }
}

class TodoQueryLiveData(private val query: Query): LiveData<DocumentSnapshotsOrException>() , EventListener<QuerySnapshot> {
    //delayed active/inactive pattern from: https://firebase.googleblog.com/2017/12/using-android-architecture-components_22.html

    private var listenerRegistration: ListenerRegistration? = null
    private var listenerRemovePending = false
    private val handler = Handler()

    private val removeListener = Runnable {
        listenerRegistration?.remove()
        listenerRemovePending = false
    }

    override fun onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener)
        }
        else {
            listenerRegistration = query.addSnapshotListener(this)
        }
        listenerRemovePending = false
    }

    override fun onInactive() {
        handler.postDelayed(removeListener, 2000)
        listenerRemovePending = true
    }

    override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
        val documents: List<DocumentSnapshot>? = snapshot?.documents?.toList()
        postValue(DocumentSnapshotsOrException(documents, e))
    }
}

