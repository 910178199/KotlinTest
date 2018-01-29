package app.test.kotlintest.anim

import android.graphics.Matrix
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Created by think on 2017-12-15.
 */
public class TVOpenAnimation : Animation() {

    var halfWidth: Int = 0
    var halfHeight: Int = 0

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        duration = 200
        fillAfter = true
        halfHeight = height / 2
        halfWidth = width / 2
        setInterpolator(AccelerateDecelerateInterpolator())
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)

        /*  if (interpolatedTime < 0.8) {
         matrix.preScale(1 + 0.625f * interpolatedTime, 1 - interpolatedTime / 0.8f + 0.01f, halfWidth, halfHeight);
     } else {
         matrix.preScale(7.5f * (1 - interpolatedTime), 0.01f, halfWidth, halfHeight);
     }*/

        val matrix: Matrix = t!!.matrix
        if (interpolatedTime < 0.4) {
            matrix.preScale(interpolatedTime / (1 - 0.8f), 0.005f, halfWidth.toFloat(), halfHeight.toFloat());
        } else {
            matrix.preScale(1F, interpolatedTime, halfWidth.toFloat(), halfHeight.toFloat());
        }
    }

}