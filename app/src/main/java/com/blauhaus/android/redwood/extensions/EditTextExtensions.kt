package com.blauhaus.android.redwood.extensions

import android.widget.EditText
import com.blauhaus.android.redwood.Resource


fun EditText.parseFloat(): Resource<Float> {
    try {
        return Resource.Success(this.text.toString().toFloat())
    } catch (e: Exception) {
        return Resource.Error(null, e.message?: "No details")
    }
}