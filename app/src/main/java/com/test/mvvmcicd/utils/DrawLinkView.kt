package com.test.mvvmcicd.utils

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getColor
import com.test.mvvmcicd.BuildConfig
import com.test.mvvmcicd.R

class DrawLinkView(context: Context) : View(context) {
    var paint: Paint = Paint()
    private val position1: MutableList<Float> = ArrayList()
    private val position2: MutableList<Float> = ArrayList()

    init {
        paint.color = getColor(context, R.color.black)
        paint.strokeWidth = 2f.dp2px.toFloat()
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("on draw", "IN onDraw() position1:$position1 position2:$position2")
        if (BuildConfig.DEBUG && position1.size != position2.size) {
            error("Assertion failed")
        }
        var i = 0
        while (i < position1.size) {
            val x1 = position1[i]
            val y1 = position1[i + 1]
            val x2 = position2[i]
            val y2 = position2[i + 1]
            canvas.drawLine(x1, y1, x2, y2, paint)
            i += 2
        }
    }

    fun setSingalQuality(high_quality: Boolean){
        if(high_quality){
            paint.color = getColor(context, R.color.black)
        }else{
            paint.color = getColor(context, R.color.warning)
        }
    }

    fun setColor(color: Int){
        paint.color = getColor(context, color)
    }

    fun setDashLine(){
        paint.strokeWidth = 8f
        paint.pathEffect = DashPathEffect(floatArrayOf(16f,16f), 0f)
    }

    fun setDottedLine(){
        val effectPath = Path()
        effectPath.addOval(0f.dp2px.toFloat(), 0f.dp2px.toFloat(), 3f.dp2px.toFloat(), 3f.dp2px.toFloat(), Path.Direction.CW)
        paint.pathEffect = PathDashPathEffect(effectPath, 9f.dp2px.toFloat(), 9f.dp2px.toFloat(), PathDashPathEffect.Style.MORPH)
    }

    fun addSourcePoint(x1: Float, y1: Float) {
        position1.add(x1)
        position1.add(y1)
    }

    fun addDestinationPoint(x2: Float, y2: Float) {
        position2.add(x2)
        position2.add(y2)
        invalidate()
    }

    init {
        invalidate()
        Log.d("drawview", "In DrawView class position1:$position1 position2:$position2")
    }
}