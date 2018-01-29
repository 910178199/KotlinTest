package app.test.hook

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Created by think on 2018-01-23.
 */

class HookUtils {

    private var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    /**
     * hook start activity
     */
    @Throws(Exception::class)
    fun hookStartActivity() {
        // 先获取ActivityManagerNative中的gDefault
        val amnClazz = Class.forName("android.app.ActivityManagerNative")
        val defaultField = amnClazz.getDeclaredField("gDefault")
        defaultField.isAccessible = true
        val gDefaultObj = defaultField.get(null)

        // 获取Singleton里面的mInstance
        val singletonClazz = Class.forName("android.util.Singleton")
        val amsField = singletonClazz.getDeclaredField("mInstance")
        amsField.isAccessible = true
        var amsObj = amsField.get(gDefaultObj)

        // 动态代理Hook下钩子
        amsObj = Proxy.newProxyInstance(this.javaClass.classLoader, amsObj.javaClass.interfaces, StartActivityInvocationHandler(amsObj))
        // 注入
        amsField.set(gDefaultObj, amsObj)
    }

    /**
     * Start Activity Invocation Handler
     */
    private inner class StartActivityInvocationHandler(private val mAmsObj: Any) : InvocationHandler {

        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
            // 拦截到所有ActivityManagerService的方法
            Log.e("TAG", "methodName" + method.name)

            if ("startActivity" == method.name) {
                Log.e("TAG", "拦截到" + method.name)
                //替换Intent
                for (i in args.indices) {
                    val any: Any = args[i]
                    if (any is Intent) {
                        val intent: Intent = Intent()
                        val componentName: ComponentName = ComponentName(context, SecondActivity::class.java)
                        intent.component = componentName
                        intent.putExtra("oldIntent", any as Intent)
                        args[i] = intent
                        return method.invoke(mAmsObj, *args)
                    }
                }
            }
            return method.invoke(mAmsObj, *args)
        }
    }


    fun startLaunchActivity() {

        val activityThread = Class.forName("android.app.ActivityThread")
        val sCurrentActivityThread = activityThread.getDeclaredField("sCurrentActivityThread")
        sCurrentActivityThread.isAccessible = true
        //获取静态属性对象实例
        val sCurrentActivityThreadInstance = sCurrentActivityThread.get(null)

        //获取Handler实例
        val mH = activityThread.getDeclaredField("mH")
        mH.isAccessible = true
        //获取到handler对象实例
        val mHInstance = mH.get(sCurrentActivityThreadInstance)

        val mHandlerCallBack = Handler::class.java.getDeclaredField("mCallback")
        mHandlerCallBack.isAccessible = true
        mHandlerCallBack.set(mHInstance, Handler.Callback {
            if (it.what == 100) {
                //ActivityClientRecord
                val activityClientRecord = it.obj

                //获取Intent对象
                val intentField = activityClientRecord.javaClass.getDeclaredField("intent")
                intentField.isAccessible = true
                val intent: Intent = intentField.get(activityClientRecord) as Intent
                //取出之前存的activity
                val targetIntent = intent.getParcelableExtra<Intent>("oldIntent")

                if (targetIntent != null) {
                    intent.component = targetIntent.component
                }

                Log.e("TAG--",""+targetIntent.component.className)
            }
            return@Callback false
        })
    }


}
