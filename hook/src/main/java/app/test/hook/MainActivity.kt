package app.test.hook

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var hookUtils: HookUtils = HookUtils(this)
        hookUtils.hookStartActivity()
        hookUtils.startLaunchActivity()

        startActivity(Intent(this, AClass::class.java))
    }
}
