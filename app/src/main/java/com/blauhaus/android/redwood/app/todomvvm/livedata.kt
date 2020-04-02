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


/* TODO refactor - we probably don't want the whole show to end if there is one little todo document in the db without a **timestamp**.
                especially without the corresponding rule in firebase checking that timestamp isn't null.
                or Cloud function that automatically sets it, if that is a thing. */
//TODO: snap.doc can be null here if we are asking for a document that doesn't exist --  wouldve thought snap would be null and there would be an exception.
fun deserializeTodo(snap: DocumentSnapshot): TodoOrException  {
    try {
        val model = Todo(
            snap.id,
            snap.getString("text") ?: "",
            snap.getBoolean("complete") ?: false,
            convertToJoda(
                snap.getTimestamp("timestamp")!!
            )
        )
        return TodoOrException(model, null)
    } catch(e: Exception) {
        return TodoOrException(null, e)
    }
}

private fun convertToJoda(timestamp: Timestamp): DateTime {
    return DateTime(timestamp.toDate())
}

