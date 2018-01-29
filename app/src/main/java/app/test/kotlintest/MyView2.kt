package app.test.kotlintest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by think on 2017-12-27.
 */
class MyView2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var RADIUS: Float = 100F;
    lateinit var mPaint: Paint

    private lateinit var color: String

    fun getColor(): String {
        return color
    }

    fun setColor(color: String) {
        this.color = color
        //设置颜色
        mPaint.color = Color.parseColor(color)
        // 调用了invalidate()方法,即画笔颜色每次改变都会刷新视图，然后调用onDraw()方法重新绘制圆
        // 而因为每次调用onDraw()方法时画笔的颜色都会改变,所以圆的颜色也会改变
        invalidate()
    }

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制圆圈
        canvas?.drawCircle(500F, 500F, RADIUS, mPaint)
    }

}