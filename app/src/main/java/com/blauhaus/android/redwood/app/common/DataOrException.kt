package com.blauhaus.android.redwood.app.common

data class DataOrException<T, E: Exception?>(val data: T?, val exception: E?)