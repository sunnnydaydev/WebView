package com.example.webview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_android_call_js.*

/**
 * Android call js method practice
 * 两种方式：
 * 1、webView的loadUri(" javascript:js方法()")
 * 2、webView的evaluateJavascript
 * */
class AndroidCallJsActivity : AppCompatActivity() {
    val mContext = this
    private lateinit var webSettings: WebSettings

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_call_js)

        webSettings = web_view.settings
        webSettings.javaScriptEnabled = true//允许启用js功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹窗

        // 加载Assets下html文件（这时js代码也就载入了）
        web_view.loadUrl("file:///android_asset/1.html")//加载assets文件夹下的html文件时使用固定格式语法即可：file:///android_asset/文件名.html
        web_view.webViewClient = object : WebViewClient(){}

        callByLoadUrl()
        //callByEvaluateJavascript()
        //webView只是载体，内容的渲染需要使用webViewChromeClient类去实现,所以在这里写回调处理。
        web_view.webChromeClient = object : WebChromeClient() {
            // 此方法可回调到alert警告框的信息。
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                val build = AlertDialog.Builder(mContext)
                    .setTitle("Js showAlertDialog Method")
                    .setMessage(message)
                    .setPositiveButton("确定") { dialog, which ->
                        result?.confirm()
                    }.setCancelable(false)
                build.create().show()
                return true
            }
        }
    }

    /**
     * 方式1：webView的loadUrl()
     * */
    private fun callByLoadUrl() {
        // 点击安卓按钮加载js方法
        btn_call_js_method.setOnClickListener {
            // 调用js时要保证网页加载完成，否则js代码调用失败。因为html中也是页面加载完成才响应的js。
            web_view.loadUrl("javascript:showAlertDialog(\"result\")")
        }

    }

    /**
     *方式2：webView的evaluateJavascript
     * */
    private fun callByEvaluateJavascript() {
        btn_call_js_method.setOnClickListener {
                web_view.evaluateJavascript("javascript:showAlertDialog()") {
                    Log.d("AndroidCallJsActivity", "" + it)// get value null ???
                }
        }
    }

    fun open(view:View){
        startActivity(Intent(this, JsCallAndroidActivity::class.java))
    }
}
