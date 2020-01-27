package com.blauhaus.android.redwood.sample.data

data class Session(val image: Int?, val title: String, val pretitle: String, val description:String, val type:SessionType)

sealed class SessionType(val audioId:Int?, videoId:Int?) {
    class Meditation(audioId:Int, videoId:Int): SessionType(audioId, videoId)
    class Update: SessionType(null, null)
}