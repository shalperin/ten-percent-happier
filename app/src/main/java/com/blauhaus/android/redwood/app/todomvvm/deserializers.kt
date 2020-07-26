package com.blauhaus.android.redwood.app.todomvvm

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import org.joda.time.DateTime


/* TODO refactor - we probably don't want the whole show to end if there
   is one little document in the db without a timestamp.
   especially without the corresponding rule in firebase checking that timestamp isn't null.
   or Cloud function that automatically sets it, if that is a thing. */

/* TODO: snap.doc can be null here if we are asking for a document that doesn't exist --  wouldve thought snap would be null
   and there would be an exception.
*/

fun deserializeTodo(snap: DocumentSnapshot): TodoOrException  {
    try {
        val timestamp = snap.getTimestamp("timestamp")
        val model = Todo(
            snap.id,
            snap.getString("text") ?: "",
            snap.getBoolean("complete") ?: false,
            convertToJoda(
                timestamp!!
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

