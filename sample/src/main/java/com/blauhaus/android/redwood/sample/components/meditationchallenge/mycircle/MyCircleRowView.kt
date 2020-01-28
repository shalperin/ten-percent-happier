package com.blauhaus.android.redwood.sample.components.meditationchallenge.mycircle

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.blauhaus.android.redwood.sample.R
import kotlinx.android.synthetic.main.view_my_circle_row.view.*

class MyCircleRowView : ConstraintLayout {
    constructor(context: Context?) : super(context) {init()}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {init()}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init()}

    var name: String? = null
    set(value) {
        name_letter.text = (value?.get(0) ?: "").toString()
        _name.text = value
        field = value
    }

    var firstLetter: String? = null
        set(value) {
            name_letter.text = value
            field = value
        }


    var message: String? = null
    set(value) {
        _message.text = value
        field = value
    }

    var didCompleteChallenge: Boolean = true
    set (value) {
        if (value) {
            challenge_complete.visibility = View.VISIBLE
        } else {
            challenge_complete.visibility = View.GONE
        }

    }

    var progress = 0
    set(value) {
        field = value
        _progress.progress = progress
    }

    fun init() {
        // Inflate the view, attatching to 'this'.
        inflate(context, R.layout.view_my_circle_row, this)
    }
}