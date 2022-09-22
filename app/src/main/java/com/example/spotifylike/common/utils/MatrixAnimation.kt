package com.example.spotifylike.common.utils

import android.graphics.Matrix
import android.view.animation.Animation
import android.view.animation.Transformation

class MatrixAnimation(private val matrix: Matrix?): Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        t.getMatrix().set(matrix)
    }
}