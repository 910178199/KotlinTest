package app.test.kotlintest

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import app.test.kotlintest.anim.Point
import app.test.kotlintest.anim.PointEvaluator

/**
 * Created by think on 2017-12-18.
 */
class MyView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //变量
    val RADIUS: Float = 70f//圆半径
    var currentPoint: Point? = null//当前坐标点
    var mPaint: Paint//画笔

    init {
        //初始化画笔
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (null == currentPoint) {
            currentPoint = Point(RADIUS, RADIUS)
            val x: Float = currentPoint!!.x
            val y: Float = currentPoint!!.y
            canvas?.drawCircle(x, y, RADIUS, mPaint)
            //绘制一个圆

            //属性动画使用
            val startPoint: Point = Point(RADIUS, RADIUS)
            val endPoint: Point = Point(700f, 1000f)
            var valueAnimator: ValueAnimator = ValueAnimator.ofObject(PointEvaluator(), startPoint, endPoint)
            valueAnimator.setDuration(5000)
            valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                override fun onAnimationUpdate(animation: ValueAnimator?) {
                    currentPoint = animation?.getAnimatedValue() as Point?
                    // 将每次变化后的坐标值（估值器PointEvaluator中evaluate（）返回的Piont对象值）到当前坐标值对象（currentPoint）
                    // 从而更新当前坐标值（currentPoint）

                    //每次赋值后就从新绘制
                    invalidate()
                    // 调用invalidate()后,就会刷新View,即才能看到重新绘制的界面,即onDraw()会被重新调用一次
                    // 所以坐标值每改变一次,就会调用onDraw()一次
                }
            })
            //启动动画
            valueAnimator.start()
        } else {
            //如果坐标不为0，则画圆
            //坐标没改变一次，就会调用一次onDraw()一次，就会画一次圆，从而实现动画效果
            var x: Float = currentPoint!!.x
            var y: Float = currentPoint!!.y
            canvas?.drawCircle(x, y, RADIUS, mPaint)
        }
    }
}