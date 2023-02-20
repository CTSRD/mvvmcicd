package com.test.mvvmcicd

import android.app.Application
import android.os.Handler
import android.os.Looper

class MyApplication: Application() {
    private val tag = MyApplication::class.java.simpleName

    init {
        mInstance = this
    }

    companion object {
        private var mInstance: MyApplication? = null

        fun applicationContext() : MyApplication {
            return mInstance as MyApplication
        }

        fun runOnUiThread(runnable: Runnable) {
            if (Thread.currentThread() === Looper.getMainLooper().thread) {
                runnable.run()
            } else {
                Handler(Looper.getMainLooper()).post(runnable)
            }
        }
    }
}