package com.blauhaus.android.redwood.barchart.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.blauhaus.android.redwood.R


class BarChartView: View {
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

    private lateinit var barPaint: Paint
    private lateinit var gridLinesPaint: Paint
    private var barPadding = 2f
    private var barPadFactor = .01f
    private var barWidth = 0f
    private var totalWidth = 0f
    private var model: List<Pair<Float, String>>?= null
    private var bar = RectF()
    private var plotAreaTopY = 0f
    private var plotAreaBottomY = 0f
    private var plotAreaHeight = 0f
    private var gridStrokeHeight = 5f


    fun init() {
        initPaints()
    }

    fun initPaints() {
        var barColor = ContextCompat.getColor(context, R.color.barColor)
        barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        barPaint.color = barColor
        barPaint.style = Paint.Style.FILL

        var gridLineColor = ContextCompat.getColor(context, R.color.gridLineColor)
        gridLinesPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gridLinesPaint.color = gridLineColor
        gridLinesPaint.style = Paint.Style.STROKE
        gridLinesPaint.setStrokeWidth(gridStrokeHeight)
        gridLinesPaint.pathEffect = DashPathEffect(floatArrayOf(35f, 5f), 0f)
    }

    override fun onSizeChanged(
        viewWidth: Int,
        viewHeight: Int,
        oldw: Int,
        oldh: Int
    ) {
        totalWidth = viewWidth.toFloat()
        barPadding = viewWidth * barPadFactor

        //Bar width
        val numbars: Int = model?.size ?: 0
        barWidth = if (numbars != 0) {
            (totalWidth - (numbars + 1) * barPadding) / numbars
        } else {
            0f
        }

        plotAreaTopY = 0f //TODO plus label UI height
        plotAreaBottomY = viewHeight.toFloat()
        plotAreaHeight = plotAreaBottomY - plotAreaTopY
    }

    fun setData(model:List<Pair<Float, String>>?) {
        if (model != null) {
            if (model.isEmpty()) {
                throw(Exception("Model can't be 0 length"))
            }
            this.model = model
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) { //Title
        //Draw grid lines TODO
        //canvas.drawLine(0f, plotAreaBottomY, totalWidth, plotAreaBottomY, labelTextPaint)
        if (model != null) {
            drawGrid(canvas, 0f + .5f * gridStrokeHeight)
            drawGrid(canvas, plotAreaBottomY/2)
            drawGrid(canvas, plotAreaBottomY - .5f * gridStrokeHeight)
            drawBars(canvas)
        }

    }

    private fun drawBars(canvas:Canvas) {
        for (i in model!!.indices) { // Bar coordinates
            val leftX = barPadding * (i + 1) + barWidth * i
            val rightX = leftX + barWidth
            var topY = 0f
            if (model!!.map{it.first}.max() != 0f) {
                topY = plotAreaBottomY - plotAreaHeight * model!!.map{it.first}.get(i) / model!!.map{it.first}.max()!!
            }
            bar.left = leftX
            bar.right = rightX
            bar.top = topY
            bar.bottom = plotAreaBottomY
            canvas.drawRect(bar, barPaint)
        }
    }

    private fun drawGrid(canvas: Canvas, y:Float) {
        val leftX = 0f
        val rightX = barPadding * (1 + model!!.size) + barWidth * model!!.size!!
        val path = Path()
        path.moveTo(leftX, y)
        path.lineTo(rightX, y)
        canvas.drawPath(path, gridLinesPaint)
    }
}