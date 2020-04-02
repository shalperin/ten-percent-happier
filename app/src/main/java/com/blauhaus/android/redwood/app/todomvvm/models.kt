package com.blauhaus.android.redwood.app.todomvvm

import com.google.firebase.Timestamp
import org.joda.time.DateTime

data class Todo(val id: String, val text:String, val complete:Boolean, val timestamp: DateTime)