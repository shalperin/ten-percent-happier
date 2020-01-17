package com.blauhaus.android.redwood.barchart.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.blauhaus.android.redwood.R


class BarChartView: View {

    //TODO: Label text isn't perfect, good enough for a first pass.

    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) { init() }

    //These are calculated - Modifications will be overwritten.
    private lateinit var barPaint: Paint
    private lateinit var gridLinesPaint: Paint
    private lateinit var labelLinePaint: Paint
    private lateinit var labelTextPaint: Paint
    private lateinit var labelBackgroundPaint: Paint
    private var model: List<Pair<Float, String>>?= null
    private var bar = RectF()
    private var labelLine: Path? = null
    private var labelBackground: Path? = null
    private var barWidth = 0f
    private var totalWidth = 0f
    private var plotAreaTopY = 0f
    private var plotAreaBottomY = 0f
    private var plotAreaHeight = 0f
    private var barPadding = 0f
    private var labelText: String? = null
    private var labelX = 0f
    private var labelY = 0f

    //These are knobs to twiddle.
    private var barPadFactor = .01f         // as a function of total width
    private var labelPositionFactor = .5f   // as a function of graph padding
    private var gridLineStrokeSize = 5f     // absolute
    private var graphPadding = 100f         // absolute
    private val labelTextSize = 30f         // absolute
    private val labelHeight = 70f           // absolute
    private val labelWidth = 225f           // absolute


    fun init() {
        initPaints()
    }

    fun initPaints() {
        val barColor = ContextCompat.getColor(context, R.color.barColor)
        barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        barPaint.color = barColor
        barPaint.style = Paint.Style.FILL

        val gridLineColor = ContextCompat.getColor(context, R.color.gridLineColor)
        gridLinesPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gridLinesPaint.color = gridLineColor
        gridLinesPaint.style = Paint.Style.STROKE
        gridLinesPaint.setStrokeWidth(gridLineStrokeSize)
        gridLinesPaint.pathEffect = DashPathEffect(floatArrayOf(35f, 5f), 0f)

        val labelLineColor = ContextCompat.getColor(context, R.color.labelColor)
        labelLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        labelLinePaint.color = labelLineColor
        labelLinePaint.style = Paint.Style.STROKE
        labelLinePaint.setStrokeWidth(gridLineStrokeSize)

        labelBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        labelBackgroundPaint.color = labelLineColor
        labelBackgroundPaint.style = Paint.Style.STROKE
        labelBackgroundPaint.strokeCap = Paint.Cap.ROUND
        labelBackgroundPaint.setStrokeWidth(labelHeight)

        val textPaintColor = ContextCompat.getColor(context, R.color.labelTextColor)
        labelTextPaint = Paint()
        labelTextPaint.color = textPaintColor
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
        barPadding = totalWidth * barPadFactor

        plotAreaTopY = graphPadding
        plotAreaBottomY = viewHeight.toFloat() - graphPadding
        plotAreaHeight = plotAreaBottomY - plotAreaTopY
    }

    fun setData(model:List<Pair<Float, String>>?) {
        if (model != null) {
            if (model.isEmpty()) {
                throw(Exception("Model can't be 0 length"))
            }
            this.model = model
            onModelChanged()
            invalidate()
        }
    }

    fun onModelChanged() {
        labelText = null
        labelBackground = null
        labelLine = null
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
        val rightX = barPadding * (model!!.size) + barWidth * model!!.size!!  + graphPadding
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
            val centerX = barCenterX(barIndex)
            val centerY = graphPadding * labelPositionFactor
            labelText = model!!.getOrNull(barIndex)?.second ?: null

            if (labelText != null) {
                createLabelStem(centerY, centerX)
                createLabelBackground(centerX, centerY)
                createLabelText(labelText!!, centerX, centerY)
                invalidate()
            } else {
                labelText = null
            }
        } else {
            labelText = null
        }
    }

    private fun createLabelBackground(centerX: Float, centerY: Float) {
        val p = Path()
        p.moveTo(centerX - .5f * labelWidth, centerY - .5f * labelTextSize)
        p.lineTo(centerX + .5f * labelWidth, centerY - .5f * labelTextSize)
        labelBackground = p
    }

    private fun createLabelStem(yTop: Float, centerX: Float) {
        val yBottom = plotAreaBottomY
        val p = Path()
        p.moveTo(centerX, yTop)
        p.lineTo(centerX, yBottom)
        labelLine = p
    }

    private fun createLabelText(text:String, centerX: Float, centerY: Float) {
        labelText = text
        labelX = centerX
        labelY = centerY
    }
}