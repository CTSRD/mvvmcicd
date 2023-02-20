package com.test.mvvmcicd.utils

import android.util.Log

object Logger {

    fun addLogInfo(tag: String, message: String){
        Log.i(tag, message)
    }

    fun addLogDebug(tag: String, message: String){
        Log.d(tag, message)
    }

    fun addLogError(tag: String, message: String){
        Log.e(tag, message)
    }

}