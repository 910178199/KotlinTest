package app.test.kotlintest.anim

import android.graphics.Matrix
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Created by think on 2017-12-15.
 */
public class TVOffAnimation : Animation() {

    var halfWidth: Int = 0;
    var halfHeight: Int = 0;

    /**
     * 初始化
     */
    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        duration = 1000
        fillAfter = true //动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        halfHeight = width / 2
        halfWidth = height / 2
        //设置插值器  开始结束缓慢，中间加速。
        setInterpolator(AccelerateDecelerateInterpolator())
    }

    /**
     * applyTransformation 调用动画更新函数,计算动画数据
     */
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        val matrix: Matrix = t!!.getMatrix();
        if (interpolatedTime < 0.8) {
            matrix.preScale(1 + 0.625f * interpolatedTime, 1 - interpolatedTime / 0.8f + 0.01f, halfWidth.toFloat(), halfHeight.toFloat());
        } else {
            matrix.preScale(7.5f * (1 - interpolatedTime), 0.005f, halfWidth.toFloat(), halfHeight.toFloat());
        }
    }
}