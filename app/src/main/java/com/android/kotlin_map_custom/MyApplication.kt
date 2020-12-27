package com.android.kotlin_map_custom

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    init{
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null
        fun ApplicationContext() : Context {
            return instance!!.applicationContext
        }
    }

}