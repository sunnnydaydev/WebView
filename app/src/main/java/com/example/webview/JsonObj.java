package com.example.webview;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by SunnyDay on 2020/7/5 19:00
 * 1、自定义对象用于对象映射
 * 2、自定义方法，映射后供js对象使用
 * 3、方法必须加JavascriptInterface注解
 */
public class JsonObj {
    @JavascriptInterface
    public void hello(String msg) {
        Log.d("JsCallAndroidActivity", "android method hello is called:"+msg);
    }
}
