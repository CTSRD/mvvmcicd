package com.test.mvvmcicd.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.core.widget.NestedScrollView


class CustomNestedScrollView : NestedScrollView {
    private val tag = CustomNestedScrollView::class.java.simpleName

    constructor(context: Context) : super(context)
    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun fling(velocityY: Int) {
        super.fling(0)
        Log.i(tag, "fling, $velocityY")
    }
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
//        return super.onNestedPreFling(target, velocityX, velocityY)
        Log.i(tag, "onNestedPreFling, $velocityY")
        return false
    }
    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
//        return super.onNestedFling(target, velocityX, velocityY, consumed)
        Log.i(tag, "onNestedFling, $velocityY")
        return false
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        Log.i(tag, "onScrollChanged")
    }

}