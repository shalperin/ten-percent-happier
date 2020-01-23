package com.blauhaus.android.redwood.sample.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.blauhaus.android.redwood.sample.R
import kotlinx.android.synthetic.main.view_thumbnail_description.view.*


class ThumbnailDescriptionView : ConstraintLayout {

    private lateinit var title: String
    private lateinit var description: String
    private var thumbnail = 0

    constructor(context: Context) : super(context) { init(null)}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {init(attrs)}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs)}

    private fun init(attrs:AttributeSet?) {
        attrs?.let {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ThumbnailDescriptionView,
                0, 0
            ).apply {
                try {
                    title = getString(R.styleable.ThumbnailDescriptionView_title) ?: "Title Missing"
                    description = getString(R.styleable.ThumbnailDescriptionView_description)
                        ?: "Description Missing"
                    thumbnail = getResourceId(
                        R.styleable.ThumbnailDescriptionView_thumbnail,
                        R.drawable.ic_all_inclusive_black_24dp
                    )
                } finally {
                    recycle()
                }
            }
        }

        val view = inflate(context,
            R.layout.view_thumbnail_description, this)
        view.titleText.text = title
        view.description.text = description
        view.thumbnail.setImageDrawable(ContextCompat.getDrawable(context, thumbnail))
    }
}