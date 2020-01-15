package com.blauhaus.android.jazz.lastfourweeks.views

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.blauhaus.android.jazz.R


class DayView : ImageView {

    // Note to self: get more muscle memory of the Code->Generate menu above.
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val metVector = R.drawable.ic_last4weeks_circle_met
    private val futureVector = R.drawable.ic_last4weeks_circle_future
    private val metTodayVector = R.drawable.ic_last4weeks_check_circle
    private val notMetVector = R.drawable.ic_last4weeks_x_mark
    private val notMetTodayVector = R.drawable.ic_last4weeks_stroked_circle

    private var d: ViewState =
        ViewState.Future()

    private val metTodayAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out).apply{
        setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
               // setImageResource(metVector)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                setImageResource(metTodayVector)
                startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
            }
        })
    }

    fun show (d: ViewState) {
        this.d = d
        show()
    }

    private fun show() {
        when(d) {
            is ViewState.Skipped -> {
                setImageResource(notMetVector)
            }
            is ViewState.Met -> {
                setImageResource(metVector)
            }
            is ViewState.MetToday -> {
                startAnimation(metTodayAnim)
            }
            is ViewState.DidntMeetYetToday -> {
                setImageResource(notMetTodayVector)
            }
            is ViewState.Future -> {
                setImageResource(futureVector)
            }
        }
    }

//    fun tintDrawable(drawableR_Id:Int, colorR_Id:Int):Drawable {
//        var d: Drawable? =
//            VectorDrawableCompat.create(resources, drawableR_Id, null)
//        d = DrawableCompat.wrap(d!!)
//        DrawableCompat.setTint(d, ContextCompat.getColor(context, colorR_Id))
//        return d
//    }

    sealed class ViewState() {
        class Skipped() : ViewState()
        class Met(): ViewState()
        class MetToday(): ViewState()
        class DidntMeetYetToday(): ViewState()
        class Future(): ViewState()
   }

}