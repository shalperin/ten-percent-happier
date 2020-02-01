package com.blauhaus.android.redwood.sample.data.models

data class Content (
    val image: Int,
    val title: String,
    val pretitle: String,
    val description:String,
    val audioId:Int? = null,
    val videoId:String? = null) {
 fun isPlayable() : Boolean {
     return audioId != null || videoId != null
 }

}
