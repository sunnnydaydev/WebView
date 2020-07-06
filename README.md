# Android WebView

![](https://github.com/sunnnydaydev/WebViewPractise/blob/master/WebView.png)

### 简介

> `WebView`是一个基于`webkit`引擎、展现`web`页面的控件。Android的Webview在低版本和高版本采用了不同的webkit版本内核，4.4后直接使用了Chrome。

### 重要类

> 以WebView类为基础，WebSettings、WebViewClient、WebChromeClient为辅助共同完成安卓段加载网页的操作。

###### WebView

```kotlin
        //加载网页相关
        my_web_view.loadUrl("https://www.baidu.com")//直接加载服务器网页
        my_web_view.loadUrl("file:///android_asset/1.html");// 直接加载本地网页（file:///android_asset/ 固定写法）

        // 缓存历史清理
        my_web_view.clearCache(true)//Clears the resource cache. Note that the cache is per-application, so this will clear the cache for all WebViews used.
        my_web_view.clearHistory()// 清空历史（本webView浏览的）
        
        // 结合工具类
        my_web_view.webViewClient = object : WebViewClient() // 设置webViewClient
        my_web_view.webChromeClient = object : WebChromeClient()// 设置WebChromeClient

    /**
     * 返回键处理，网页中可返回上一页，不处理返回键直接退出App
     * */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //when user press back code and canGoBack
        if (keyCode == KeyEvent.KEYCODE_BACK && my_web_view.canGoBack()) {
            my_web_view.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
```

###### WebSettings

```kotlin
   /**
     * WebSettings类：对webView 进行配置管理
     * */
    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun webViewSettings() {
        val webSettings = my_web_view.settings
        // js交互控制
        webSettings.javaScriptEnabled = true //支持js交互，动画等操作会造成cpu、电量消耗可在activity、fragment的onResume、onStop进行开关控制
        // 适配
        webSettings.useWideViewPort = true//自适应屏幕->自动将图片调整到自适应webView 大小
        webSettings.loadWithOverviewMode = true//自适应屏幕->缩放到屏幕大小
        //缩放
        webSettings.setSupportZoom(true)//支持缩放，默认为true。
        webSettings.builtInZoomControls = true//设置是否展示内置的缩放控件，默认为false
        webSettings.displayZoomControls = true//显示原生的缩放控件

        //其他
        webSettings.allowFileAccess = true // 可访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 支持js 自动打开新窗口
        webSettings.loadsImagesAutomatically = true // 支持自动加载图片
        webSettings.defaultTextEncodingName = "UTF-8" // 默认值也是UTF-8

        //缓存控制
        webSettings.domStorageEnabled = true // 开启dom缓存功能
        webSettings.databaseEnabled = true // 开启数据库缓存功能
        webSettings.setAppCacheEnabled(true)// 开启application 缓存功能
        webSettings.cacheMode =
            WebSettings.LOAD_CACHE_ELSE_NETWORK //设置缓存模式（LOAD_CACHE_ELSE_NETWORK，有缓存时加载缓存，即使缓存过期，没有时从网络加载）
        webSettings.setAppCachePath("") // 设置app缓存目录，api已弃用。

        // 5.1以上默认禁止了https和http混用,开启如下
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

    }
```



###### WebViewClient 

> 协助WebView工作，有一些列回调方法，用于处理各种通知 & 请求事件等。

```java
    my_web_view.webViewClient = object : WebViewClient() {
            //加载url、返回false 代表使用webView 加载url 不使用系统浏览器。
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
// 如下loadUrl，return true 这种也代表不使用系统浏览器。但是官方建议直接 return false
//                view?.loadUrl("https://www.baidu.com")
//                return true
                return false// 直接return false即可
            }

            // 页面加载时回调
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            // 页面加载完成时回调
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            // 加载页面资源时调用（如页面上有好多图片，没加载一张就会回调一次）
            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }

            // 加载页面时服务器出现错误（例如404）
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                // todo 展示自定义html页面，提示错误。
            }

            // 处理https请求
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
                // super.onReceivedSslError(view, handler, error);
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler?.proceed() // 等待证书相应
                // handler?.cancel() // 挂起连接，默认方式。

            }
        }
```

###### WebChromeClient

```java
  /**
     * WebChromeClient类：辅助webView 处理js dialog、网站icon、网站title 等
     * */
    private fun webChromeClient() {
        my_web_view.webChromeClient = object : WebChromeClient() {
            // 获得网页进度加载（newProgress 100为加载完成）
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            // 获取网页的标题
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }

            // 是否由客户端处理js Alert 事件，true 代表客户端处理，false 代表客户端不处理。
            // 一般客户端处理时客户端 展示对话框让用户进行选择
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                AlertDialog.Builder(mContext)
                    .setTitle("Title")
                    .setMessage(message)
                    .setPositiveButton("确定") { dialog, which ->
                        result?.confirm()
                    }.setCancelable(false)
                    .show()
                return true
            }

            // 支持js Confirm 对话框，true代表点击确定，false 代表点击取消。
            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }
           // 弹出输入框的拦截，true 代表返回输入框的值，false 代表返回null
            override fun onJsPrompt(
                view: WebView?,
                url: String?,
                message: String?,
                defaultValue: String?,
                result: JsPromptResult?
            ): Boolean {
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

        }
    }
```



### Android 调用 JS 代码

###### 方式

- webView的loadUrl(" javascript:js方法()")
- webView的evaluateJavascript（）

```html
<html>
<head>
    <meta charset="UTF-8">
    <title>SunnyDay</title>
    <!--内部引入法-->
    <script>
        function showAlertDialog(result){
         alert("i am js alert dialog，i am js method!"+result)
        }
    </script>
</head>
<body>
    <h3>i am a simple html page</h3>
</body>
</html>>
```

###### 

（1）webView#loadUrl 加载原理及其代码

>借助webChromeClient 的各种回调处理。一般为js的各种dialog 方法触发时被安卓webViewClient的各种回调监听方法拦截消费。



```java
        webSettings = web_view.settings
        webSettings.javaScriptEnabled = true//允许启用js功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹窗
        // 加载Assets下html文件（这时js代码也就载入了）
        web_view.loadUrl("file:///android_asset/1.html")//加载assets文件夹下的html文件时使用固定格式语法即可：file:///android_asset/文件名.html
        
        web_view.webViewClient = object : WebViewClient(){}
        callByLoadUrl()
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
```



（2）webView#evaluateJavascript

```java
        webSettings = web_view.settings
        webSettings.javaScriptEnabled = true//允许启用js功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹窗

        // 加载Assets下html文件（这时js代码也就载入了）
        web_view.loadUrl("file:///android_asset/1.html")//加载assets文件夹下的html文件时使用固定格式语法即可：file:///android_asset/文件名.html
        web_view.webViewClient = object : WebViewClient(){}
        callByEvaluateJavascript()
     
   /**
     *方式2：webView的evaluateJavascript
     * js 方法无返回值时it为 null
     */
    private fun callByEvaluateJavascript() {
        btn_call_js_method.setOnClickListener {
                web_view.evaluateJavascript("javascript:showAlertDialog()") {
                    Log.d("AndroidCallJsActivity", "" + it)
                }
        }
    }
```



待续！！！

