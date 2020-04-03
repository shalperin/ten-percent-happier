package com.blauhaus.android.redwood.app.todomvvm

import androidx.lifecycle.LiveData
import com.blauhaus.android.redwood.app.common.DataOrException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import org.joda.time.DateTime

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
    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }

    override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
        val documents: List<DocumentSnapshot>? = snapshot?.documents?.toList()
        postValue(DocumentSnapshotsOrException(documents, e))
    }
}

