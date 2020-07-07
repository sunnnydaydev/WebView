package com.example.webview

import android.util.Log
import android.webkit.JavascriptInterface

/**
 * Create by SunnyDay on 21:30 2020/07/07
 * 1、自定义对象用于对象映射
 * 2、自定义方法，映射后供js对象使用
 * 3、方法必须加JavascriptInterface注解
 */
class JsInterface {
    @JavascriptInterface
    fun hello(msg: String) {
        Log.d("JsCallAndroidActivity", "android method hello is called:$msg")
    }
}