package com.example.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_android_call_js.*

import kotlinx.android.synthetic.main.activity_js_call_android.*
import android.R.id.message
import android.net.Uri


/**
 * js call android method practise
 * 1、webView的addJavascriptInterface（自定义对象，“映射给的自定义字符串对象”）
 * 这种方式最简单，自定义对象，内部定义一系列方法供js调用。js拿到映射对象即可调用安卓自定义的对象方法
 *
 * 2、重写webViewClient的shouldOverrideUrlLoading方法，筛选请求的url是否为js规定的url，是规定的则调用安卓的相应方法。
 *
 * 3、通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（），原理与2一致。
 * */
class JsCallAndroidActivity : AppCompatActivity() {
    private lateinit var webSettings: WebSettings

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_js_call_android)

        webSettings = web_view_th.settings
        webSettings.javaScriptEnabled = true//允许启用js功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹窗
        web_view_th.loadUrl("file:///android_asset/2.html")
        web_view_th.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                //view?.url //file:///android_asset/2.html ，注意这里的两个url不相同 get的url为webViewLoad的，request的为动态定义的
                val requestUrl = request?.url
                Log.i("JsCallAndroidActivity", "" + requestUrl) //js://webview?arg1=111&arg2=222
                return if (requestUrl.toString() == "js://webview?arg1=111&arg2=222") {
                    // 符合筛选，调用安卓方法。
                    printLog()
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, request)
                }
            }

        }

        web_view_th.webChromeClient = object : WebChromeClient() {
            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                val uri = Uri.parse(message)
                if ("js" == uri.scheme) {
                    if ("demo" == uri.authority) {
                        result?.confirm("JS 调用了 Android 的方法")
                        // 符合筛选，调用安卓方法。
                        printLog()
                    }
                    return true
                }

                return super.onJsPrompt(view, url, message, defaultValue, result)
            }
        }

        //js调用安卓方法方式1：
        // 定义对象映射，把自定义的JsonObj映射给"test",供js对象使用。
        web_view_th.addJavascriptInterface(JsInterface(), "test")
    }

    private fun printLog() {
        Log.i("JsCallAndroidActivity", "android method：print log ！")
    }
}
