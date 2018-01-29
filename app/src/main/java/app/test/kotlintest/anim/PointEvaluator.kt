package app.test.kotlintest.anim

import android.animation.TypeEvaluator

/**
 * Created by think on 2017-12-18.
 */
public class PointEvaluator() : TypeEvaluator<Any> {
    override fun evaluate(fraction: Float, startValue: Any?, endValue: Any?): Any {

        val startPoint: Point = startValue as Point
        val endPoint: Point = endValue as Point
        //转换成Point类型

        var x: Float = startPoint.x + fraction * (endPoint.x - startPoint.x)
        var y: Float = endPoint.y + fraction * (endPoint.y - startPoint.y)
        //根据当前的fraction(完成度)来计算当前动画的x和y

        var point: Point = Point(x, y)
        return point
        //将计算的坐标封装到一个新的Point对象中
    }
}