package com.blauhaus.android.redwood.sample.components.index

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.blauhaus.android.redwood.sample.R
import kotlinx.android.synthetic.main.view_thumbnail_description.view.*


class ThumbnailDescriptionView : ConstraintLayout {

    // When using the one-arg programmatic constructor,
    // we will need to be able to set these.
    public var title = "Not initialized"
        set(value) {
            field = value
            initSubViews()
        }
    public var description = "Not initialized"
        set(value) {
            field = value
            initSubViews()
        }

    public var thumbnail = 0
        set(value) {
            field = value
            initSubViews()
        }

    public var textColor = 0
        set(value) {
            field = value
            initSubViews()
        }



    //all three constructors call init below
    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        // When we are using the constructors for XML, peel
        // the styles out of the XML so we can use the values
        // later.
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
                        R.drawable.ic_all_inclusive_24dp
                    )
                    textColor = getColor(R.styleable.ThumbnailDescriptionView_textColor, Color.RED)
                } finally {
                    recycle()
                }
            }
        }

        // Inflate the view, attatching to 'this'.
        inflate(context, R.layout.view_thumbnail_description, this)

        // Load the subviews with our styling attributes.
        initSubViews()
    }

    private fun initSubViews() {
        rootView?.titleText?.text = title
        rootView?.titleText?.setTextColor(textColor)

        rootView?.description?.text = description
        rootView?.description?.setTextColor(textColor)

        rootView?.thumbnail?.setImageDrawable(ContextCompat.getDrawable(context, thumbnail))

    }

}