package com.blauhaus.android.redwood.features.lastfourweeks.views

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.blauhaus.android.redwood.R

class DotView : ImageView {

    //TODO: See if we can use un-tinted vector drawables so that we can style this any way we want.

    // Note to self: get more muscle memory of the Code->Generate menu above.
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var d: DotViewState =
        DotViewState.Future()

    private val metTodayAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out).apply{
        setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                setImageResource(R.drawable.dot_met)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                setImageResource(R.drawable.dot_met_today)
                startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
            }
        })
    }

    fun show (d: DotViewState) {
        this.d = d
        show()
    }

    private fun show() {
        when(d) {
            is DotViewState.Skipped -> {
                setImageResource(R.drawable.dot_skipped)
            }
            is DotViewState.Met -> {
                setImageResource(R.drawable.dot_met)
            }
            is DotViewState.MetToday -> {
                startAnimation(metTodayAnim)
            }
            is DotViewState.Future -> {
                setImageResource(R.drawable.dot_future)
            }
        }
    }

    sealed class DotViewState() {
        class Skipped() : DotViewState()
        class Met(): DotViewState()
        class MetToday(): DotViewState()
        class Future(): DotViewState()
   }
}