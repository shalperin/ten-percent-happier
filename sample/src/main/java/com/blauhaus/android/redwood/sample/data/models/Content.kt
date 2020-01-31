package com.blauhaus.android.redwood.sample.data.models

sealed class Content {
    data class Meditation(val image: Int, val title: String, val pretitle: String, val description:String,  val audioId:Int, val videoId:Int): Content()
    data class Update(val image: Int, val title: String, val pretitle: String, val description:String ): Content()
}
