package org.ar25.androidno.util

import android.animation.Animator
import android.view.View


fun View.animateToTransparent(afterAnimation: () -> Unit = {}){
    this.alpha = 1.0f

    this.animate().alpha(0.0f).setDuration(200L).setListener(
            object: Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) = afterAnimation()
            })
}

fun View.animateToVisible(afterAnimation: () -> Unit = {}){
    this.alpha = 0.0f

    this.animate().alpha(1.0f).setDuration(200L).setListener(
            object: Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) = afterAnimation()
            })
}