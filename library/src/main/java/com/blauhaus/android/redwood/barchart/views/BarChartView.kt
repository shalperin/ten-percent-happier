package com.blauhaus.android.redwood.barchart.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat.getColor

import com.blauhaus.android.redwood.R


class BarChartView: View {

    constructor(context: Context?) : super(context) {
        init(null)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init(attrs) }

    //These are calculated - Modifications will be overwritten.
    private lateinit var barPaint: Paint
    private lateinit var gridLinesPaint: Paint
    private lateinit var labelLinePaint: Paint
    private lateinit var labelTextPaint: Paint
    private lateinit var labelBackgroundPaint: Paint
    private var bar = RectF()
    private var labelLine: Path? = null
    private var labelBackground: Path? = null
    private var barWidth = 0f
    private var totalWidth = 0f
    private var plotAreaTopY = 0f
    private var plotAreaBottomY = 0f
    private var plotAreaHeight = 0f
    private var labelText: String? = null
    private var labelX = 0f
    private var labelY = 0f

    //Attrs to twiddle.
    private var barColor = getColor(context, R.color.barColor)
    private var gridLineColor = getColor(context, R.color.gridLineColor)
    private var labelLineColor = getColor(context, R.color.labelColor)
    private var textColor = getColor(context, R.color.labelTextColor)
    private var barPadding = 8f
    private var gridLineStrokeSize = 5f
    private var graphPadding = 100f
    private var labelPosition = 40f
    private var labelTextPosition = 12f
    private var labelTextSize = 30f
    private var labelHeight = 70f
    private var labelWidth = 225f


    fun init(attrs: AttributeSet?) {

        attrs?.let {
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.BarChartView,
                0,0
            ).apply{
                try {
                    barColor = getColor(R.styleable.BarChartView_barColor, getColor(context, R.color.barColor))
                    gridLineColor = getColor(R.styleable.BarChartView_gridLineColor, getColor(context, R.color.gridLineColor))
                    labelLineColor = getColor(R.styleable.BarChartView_labelLineColor, getColor(context, R.color.labelColor))
                    textColor = getColor(R.styleable.BarChartView_textColor, getColor(context, R.color.labelTextColor))
                    gridLineStrokeSize = getFloat(R.styleable.BarChartView_gridLineStrokeSize, 5f)
                    barPadding = getFloat(R.styleable.BarChartView_barPadding, 8f)
                    graphPadding = getFloat(R.styleable.BarChartView_graphPadding, 100f)
                    labelPosition = getFloat(R.styleable.BarChartView_labelPosition, 40f)
                    labelTextPosition = getFloat(R.styleable.BarChartView_labelTextPosition, 12f)
                    labelTextSize = getFloat(R.styleable.BarChartView_labelTextSize, 30f)
                    labelHeight = getFloat(R.styleable.BarChartView_labelHeight, 70f)
                    labelWidth = getFloat(R.styleable.BarChartView_labelWidth, 225f)
                } finally {
                    recycle()
                }
            }
        }

        initPaints()
    }

    fun initPaints() {
        barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        barPaint.color = barColor
        barPaint.style = Paint.Style.FILL

        gridLinesPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gridLinesPaint.color = gridLineColor
        gridLinesPaint.style = Paint.Style.STROKE
        gridLinesPaint.strokeWidth = gridLineStrokeSize
        gridLinesPaint.pathEffect = DashPathEffect(floatArrayOf(35f, 5f), 0f)

        labelLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        labelLinePaint.color = labelLineColor
        labelLinePaint.style = Paint.Style.STROKE
        labelLinePaint.strokeWidth = gridLineStrokeSize

        labelBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        labelBackgroundPaint.color = labelLineColor
        labelBackgroundPaint.style = Paint.Style.STROKE
        labelBackgroundPaint.strokeCap = Paint.Cap.ROUND
        labelBackgroundPaint.strokeWidth = labelHeight

        labelTextPaint = Paint()
        labelTextPaint.color = textColor
        labelTextPaint.textAlign = Paint.Align.CENTER
        labelTextPaint.textSize = labelTextSize
    }

    override fun onSizeChanged(
        viewWidth: Int,
        viewHeight: Int,
        oldw: Int,
        oldh: Int
    ) {
        totalWidth = viewWidth.toFloat() - graphPadding * 2

        plotAreaTopY = graphPadding
        plotAreaBottomY = viewHeight.toFloat() - graphPadding
        plotAreaHeight = plotAreaBottomY - plotAreaTopY
    }

    var model: List<Pair<Float,String>>? = null
        set(value) {
            if (value != null) {
                if (value.isEmpty()) {
                    throw(Exception("Model can't be 0 length."))
                }
            }
            labelText = null
            labelBackground = null
            labelLine = null
            field = value
            invalidate()

        }

    fun recomputeProperties() {
        val numbars: Int = model?.size ?: 0
        barWidth = if (numbars != 0) {
            (totalWidth - (numbars + 1) * barPadding) / numbars
        } else {
            0f
        }
    }

    override fun onDraw(canvas: Canvas) {
        recomputeProperties()  //TODO I'm not sure why we can't call this in onModelChanged.

        if (model != null) {
            drawGrid(canvas, .5f * gridLineStrokeSize + graphPadding)
            drawGrid(canvas, plotAreaBottomY/2 + graphPadding/2)
            drawGrid(canvas, plotAreaBottomY - .5f * gridLineStrokeSize)
            drawLabelLine(canvas)
            drawLabelBackground(canvas)
            drawLabelText(canvas)
            drawBars(canvas)
        }
    }

    private fun drawLabelLine(canvas:Canvas) {
        if (labelLine != null) {  canvas.drawPath(labelLine!!, labelLinePaint) }
    }

    private fun drawLabelText(canvas: Canvas) {
        if (labelText != null) { canvas.drawText(labelText!!, labelX, labelY, labelTextPaint) }
    }

    private fun drawLabelBackground(canvas: Canvas) {
        if (labelBackground != null) { canvas.drawPath(labelBackground!!,labelBackgroundPaint) }
    }

    private fun drawBars(canvas:Canvas) {
        for (i in model!!.indices) { // Bar coordinates
            var topY = 0f
            if (model!!.map{it.first}.max() != 0f) {
                topY = plotAreaBottomY - plotAreaHeight * model!!.map{it.first}.get(i) / model!!.map{it.first}.max()!!
            }
            bar.left = barLeftX(i)
            bar.right = barRightX(i)
            bar.top = topY
            bar.bottom = plotAreaBottomY
            canvas.drawRect(bar, barPaint)
        }
    }

    private fun drawGrid(canvas: Canvas, y:Float) {
        val leftX = graphPadding + barPadding
        val rightX = barPadding * (model!!.size) + barWidth * model!!.size  + graphPadding
        val path = Path()
        path.moveTo(leftX, y)
        path.lineTo(rightX, y)
        canvas.drawPath(path, gridLinesPaint)
    }

    private fun whichBarClicked(x: Float): Int? {
        for (i in model!!.indices) { //yeah, I know.
            if (x  in barLeftX(i)..barRightX(i)) {
                return i
            }
        }
        return null
    }

    private fun barLeftX(barIndexInModel: Int):Float {
        return barPadding * (barIndexInModel + 1) + barWidth * barIndexInModel + graphPadding
    }

    private fun barRightX(barIndexInModel:Int):Float {
        return barLeftX(barIndexInModel) + barWidth
    }

    private fun barCenterX(barIndexInModel: Int):Float {
        return barLeftX(barIndexInModel) + .5f * barWidth
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null)  {
            createLabel(event.x)
        }
        return true
    }

    private fun createLabel(touchX: Float) {
        val barIndex = whichBarClicked(touchX)
        if (barIndex != null) {
            val x = barCenterX(barIndex)
            val y = labelPosition
            labelText = model!!.getOrNull(barIndex)?.second

            if (labelText != null) {
                createLabelStem(y, x)
                createLabelBackground(x, y)
                createLabelText(labelText!!, x, y)
                invalidate()
            } else {
                labelText = null
            }
        } else {
            labelText = null
        }
    }

    private fun createLabelBackground(x: Float, y: Float) {
        val p = Path()
        p.moveTo(x - .5f * labelWidth, y)
        p.lineTo(x + .5f * labelWidth, y)
        labelBackground = p
    }

    private fun createLabelStem(yTop: Float, x: Float) {
        val yBottom = plotAreaBottomY
        val p = Path()
        p.moveTo(x, yTop)
        p.lineTo(x, yBottom)
        labelLine = p
    }

    private fun createLabelText(text:String, x: Float, y: Float) {
        labelText = text
        labelX = x
        labelY = y + labelTextPosition
    }
}