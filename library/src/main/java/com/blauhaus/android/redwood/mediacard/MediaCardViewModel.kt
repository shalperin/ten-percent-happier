package com.blauhaus.android.redwood.mediacard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaCardViewModel : ViewModel() {

    val transferLdMap = mutableMapOf<Int, MutableLiveData<Transport>>()

    fun data(id: Int): MutableLiveData<Transport> {
        if (!transferLdMap.contains(id)) {
            transferLdMap[id] = MutableLiveData()
        }
        return transferLdMap[id]!!
    }

     sealed class Transport {
        data class Playable(
            val image: Int,
            val title: String,
            val preTitle: String,
            val description: String,
            val audioId:Int,
            val videoId:Int): Transport()
        data class Static(
            val image: Int,
            val title: String,
            val preTitle: String,
            val description: String): Transport()
    }



}
