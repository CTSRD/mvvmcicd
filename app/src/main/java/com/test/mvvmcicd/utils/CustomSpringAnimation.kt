package com.cts.cicd.utils

import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

internal object CustomSpringAnimation {

    // The constant for spring force stiffness.
    private val SPRING_FORCE_STIFFNESS = SpringForce.STIFFNESS_MEDIUM

    // Animation.
    private lateinit var springForce: SpringForce
    private var springAnimator: SpringAnimation? = null

    fun setSpringAnimation(view: View) {
        springForce = SpringForce()
        springForce.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        springForce.stiffness = SPRING_FORCE_STIFFNESS
        springForce.finalPosition = view.x
        springAnimator = SpringAnimation(view, DynamicAnimation.X)
        springAnimator!!.setStartVelocity(3000f)
        springAnimator!!.spring = springForce
    }

    fun startSpringAnimation(){
        springAnimator?.start()
    }

}