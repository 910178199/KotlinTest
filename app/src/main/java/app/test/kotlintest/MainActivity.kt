package app.test.kotlintest

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.*
import app.test.kotlintest.anim.ColorEvaluator
import app.test.kotlintest.anim.TVOffAnimation
import app.test.kotlintest.anim.TVOpenAnimation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.close_btn -> {
                //关机动画
                constraint.startAnimation(TVOffAnimation())
            }
            R.id.open_btn -> {
                //开机动画
                constraint.startAnimation(TVOpenAnimation())
            }
            R.id.duang_btn -> {
                //duang动画
                val height: Float = constraint.height.toFloat()
                val animatorSet: AnimatorSet = AnimatorSet()
                //添加一组动画，播放顺序为一起播放
                animatorSet.playTogether(ObjectAnimator.ofFloat(constraint, "translationY", -height, 0f).setDuration(2000))
                //变化在末端处弹跳。
                animatorSet.interpolator = BounceInterpolator()
                animatorSet.start()
            }
            else -> {

            }
        }
        startActivity()
    }

    fun log(any: Any?) {
        Log.e("log", any.toString())
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        close_btn.setOnClickListener(this)
        open_btn.setOnClickListener(this)
        duang_btn.setOnClickListener(this)

        //设置一个vector动画
        val drawable = image.drawable
        //AnimatedVectorDrawableCompat实现了Animatable接口
        if (drawable is Animatable) {
            drawable.start()
        }

        //京东 帧动画 FrameAnimation
        jd_frame_anim_img.setBackgroundResource(R.drawable.jd_anim)
        val animationDrawable = jd_frame_anim_img.background as AnimationDrawable
        animationDrawable.start()

        /*
        * 补间动画 Tweened Amimation
        * alpha（淡入淡出），translate（位移），scale（缩放大小），rotate（旋转）。
        */
        //XML实现
        var loadAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.tween_transition_anim)
        tween_translate.startAnimation(loadAnimation)

        //代码实现
        var alpha: AlphaAnimation = AlphaAnimation(0f, 1f)
        alpha.duration = 2000
        var translate: TranslateAnimation = TranslateAnimation(0f, 100f, 0f, 100f)
        translate.duration = 2000
        translate.startOffset = 3000//延迟执行
        var animationSet: AnimationSet = AnimationSet(false)
        animationSet.addAnimation(alpha)
        animationSet.addAnimation(translate)
        constraint.startAnimation(animationSet)

        /**
         * 属性动画
         */
        //创建动画实例，传入初始值和结束值
        val ofInt: ValueAnimator = ValueAnimator.ofInt(0, 3)
        //ofInt 作用有两个，创建实例，将传入多个值平滑的过度
        //动画时长
        ofInt.setDuration(500)
        //设置动画重复播放次数=重复次数+1
        //动画播放次数为infinite时，动画无限重复
        ofInt.repeatCount = 0
        //设置动画重复播放动画模式
        ofInt.repeatMode = ValueAnimator.RESTART
        //ValueAnimator.RESTART:正序从放
        //ValueAnimator.REVERSE:倒序回放

        //将改变的值手动赋值给对象属性值，通过动画的更新监听器
        ofInt.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                val animatedValue = animation!!.getAnimatedValue()
                //获取改变后的值

                log(animatedValue.toString())
                //输出值

                //View.setproperty(animatedValue)
                //将改变后的值赋给对象属性值

                //View.requestLayout()
                //刷新视图，从新绘制，实现动画效果
            }
        });

        /**
         * XML中使用属性动画
         */
        val animator: Animator = AnimatorInflater.loadAnimator(this, R.animator.set_animator)
        //载入动画
        animator.setTarget(constraint)
        //设置对象
        animator.start()

        /**
         * 实例操作
         */
        val buttonValueAnimator: ValueAnimator = ValueAnimator.ofInt(close_btn.width, 500)
        buttonValueAnimator.duration = 2000
        buttonValueAnimator.repeatMode = ValueAnimator.INFINITE
        buttonValueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {

                val value: Int = animation!!.getAnimatedValue() as Int
                log(value)
                close_btn.width = value
                close_btn.requestLayout()
            }
        })
        buttonValueAnimator.start()


        /**
         * ObjectAnimator
         */
        //渐变
        var alphaAnimator: ObjectAnimator = ObjectAnimator.ofFloat(close_btn, "alpha", 1f, 0f, 1f)
        alphaAnimator.duration = 2000
        alphaAnimator.start()

        //旋转
        var rotationAnimator: ObjectAnimator = ObjectAnimator.ofFloat(close_btn, "rotation", 0f, 360f)
        rotationAnimator.duration = 2000
        rotationAnimator.start()

        //平移
        val translationX = close_btn.translationX.toFloat()
        var translationAnimator: ObjectAnimator = ObjectAnimator.ofFloat(close_btn, "translationX", translationX, 300f, translationX)
        translationAnimator.duration = 2000
        translationAnimator.start()

        //旋转
        val scaleAnimator: ObjectAnimator = ObjectAnimator.ofFloat(close_btn, "scaleX", 1f, 3f, 1f)
        scaleAnimator.duration = 2000
        scaleAnimator.start()

        //组合动画
        var animatorSet: AnimatorSet = AnimatorSet()
        animatorSet.
                play(alphaAnimator).//播放
                with(rotationAnimator).//同时执行
                before(translationAnimator).//在..之前执行
                after(scaleAnimator)//在..之后执行
        animatorSet.setDuration(5000)
        animatorSet.start()

        //XML组合动画
        var animators: AnimatorSet = AnimatorInflater.loadAnimator(this, R.animator.set_animation) as AnimatorSet
        animators.setTarget(duang_btn)
        animators.start()


        //通过自定义对象属性实现动画效果
        var objectAnimator: ObjectAnimator = ObjectAnimator.ofObject(myView2, "color", ColorEvaluator(), "#0000FF", "#FF0000")
        objectAnimator.setDuration(8000)
        objectAnimator.start()

        /**
         * 对于没有get,set属性，手动设置：
         * ·1.继承原始类，添加上get,set方法
         *  2.通过报告原始动画对象，间接加入get,set方法
         */

        //第二种
        var viewWrapperButton: ViewWrapper = ViewWrapper(open_btn)
        ObjectAnimator.ofInt(viewWrapperButton, "width", 1000).setDuration(2000).start()

        //ViewPropertyAnimator用法
        duang_btn.animate()
                .alpha(0f)
                .x(500f)
                .y(500f)
                .setDuration(2000)
                .translationX(100f)
                .rotation(360f)
                .scaleX(180f)
                .setInterpolator(BounceInterpolator())
                .start()
    }


    private class ViewWrapper() {
        lateinit var mTarget: View

        public constructor(mTarget: View) : this() {
            this.mTarget = mTarget
        }

        // 为宽度设置get（） & set（）
        fun getWidth(): Int {
            return mTarget.layoutParams.width
        }

        fun setWidth(width: Int) {
            mTarget.layoutParams.width = width
            mTarget.requestLayout()
        }
    }

}

