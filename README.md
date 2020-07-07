# Android WebView

![](https://github.com/sunnnydaydev/WebView/blob/master/WebView.png)


### 一、简介

> `WebView`是一个基于`webkit`引擎、展现`web`页面的控件。Android的Webview在低版本和高版本采用了不同的webkit版本内核，4.4后直接使用了Chrome。

### 二、重要类

> 以WebView类为基础，WebSettings、WebViewClient、WebChromeClient为辅助共同完成安卓段加载网页的操作。

###### 1、WebView

```kotlin
        //加载网页相关
        my_web_view.loadUrl("https://www.baidu.com")//直接加载服务器网页
        my_web_view.loadUrl("file:///android_asset/1.html");// 直接加载本地网页               （file:///android_asset/ 固定写法） 

        // 缓存历史清理
        my_web_view.clearCache(true)//Clears the resource cache. Note that the cache is per-               application, so this will clear the cache for all WebViews used.
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

###### 2、WebSettings

```kotlin
   /**
     * WebSettings类：对webView 进行配置管理
     * */
    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun webViewSettings() {
        val webSettings = my_web_view.settings
        // js交互控制
        webSettings.javaScriptEnabled = true //支持js交互，动画等操作会造成cpu、电量消耗可在activity、         fragment的onResume、onStop进行开关控制
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
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //设置缓存模式                           （LOAD_CACHE_ELSE_NETWORK，有缓存时加载缓存，即使缓存过期，没有时从网络加载）
        webSettings.setAppCachePath("") // 设置app缓存目录，api已弃用。

        // 5.1以上默认禁止了https和http混用,开启如下
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

    }
```



###### 3、WebViewClient 

> 协助WebView工作，有一些列回调方法，用于处理各种通知 & 请求事件等。

```java
    my_web_view.webViewClient = object : WebViewClient() {
            //加载url、返回false 代表使用webView 加载url 不使用系统浏览器。
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
               // 如下loadUrl，return true 这种也代表不使用系统浏览器。但是官方建议直接 return false
              //  view?.loadUrl("https://www.baidu.com")
              //  return true
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

###### 4、WebChromeClient

```java
    /**
     * WebChromeClient类：辅助webView 处理js dialog、网站icon、网站title。
     * js的各种dialog事件触发时可由webChromeClient的响应方法回调到。
     * */
    private fun webChromeClient() {
        my_web_view.webChromeClient = object : WebChromeClient() {
            // 获得网页进度时回调
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            // 获取网页的标题时回调
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

            // js Confirm 对话框拦截，true代表点击确定，false 代表点击取消。
            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }
           // 输入框拦截，true 代表返回输入框的值，false 代表返回null
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



### 三、Android 调用 JS 代码

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
          return "i am js method!"
        }
    </script>
</head>
<body>
    <h3>i am a simple html page</h3>
</body>
</html>>
```

> 如上准备个1.html文件

###### 1、webView#loadUrl 加载原理及其代码

>借助webChromeClient 的各种回调处理。一般为js的各种dialog 方法触发时被安卓webViewClient的各种回调监听方法拦截消费。



```java
        webSettings = web_view.settings
        webSettings.javaScriptEnabled = true//允许启用js功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹窗
        // 加载Assets下html文件（这时js代码也就载入了）
        web_view.loadUrl("file:///android_asset/1.html")//加载assets文件夹下的html文件时使用固定格式语         法即可：file:///android_asset/文件名.html
        
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
        web_view.loadUrl("file:///android_asset/1.html")//加载assets文件夹下的html文件时使用固定格式语          法即可：file:///android_asset/文件名.html
        web_view.webViewClient = object : WebViewClient(){}
        callByEvaluateJavascript()
     
   /**
     *方式2：webView的evaluateJavascript
     * js 方法无返回值时it为 null
     */
    private fun callByEvaluateJavascript() {
        btn_call_js_method.setOnClickListener {
                web_view.evaluateJavascript("javascript:showAlertDialog()") {
                    Log.d("AndroidCallJsActivity", "" + it)//AndroidCallJsActivity"i am js method!"
                }
        }
    }
```

（3）总结

```java
/**
 * Android call js method practice
 * 两种方式：
 * 1、webView的loadUri(" javascript:js方法()")
 *    这种方式一般调用的为js的dialog方法，使用安卓的WebChromeClient 对应回调方法进行拦截处理。
 * 2、webView的evaluateJavascript
 *     直接调用js的方法，还可以获得js方法返回值回调
 *     js方法结果回调为String 类型值
 *     js方法无返回值，这里回调为null
 *
 *     建议以这种方式，既可快捷获得返回值，又可通过1中的dialog回调处理。
 *
 *     小结：Android call js method
 *          安卓调用js方法后可以在安卓端获得结果回调处理。
 * */
```



| 调用方式               | 优点   | 缺点          | 适用场景      |
| ------------------ | ---- | ----------- | --------- |
| loadUrl            | 方便简洁 | 效率低，获取返回值麻烦 | 不需要获取返回值时 |
| evaluateJavascript | 效率高  | 安卓4.4以上才能使用 | 安卓4.4以上   |



### 四、Js调用安卓代码

###### 方式

- 通过webView的addJavascriptInterface方法
- 通过重写webViewClient的shouldOverrideUrlLoading方法筛选url调用
- 通过 WebChromeClient 的dialog 回调判断筛选url调用

```html
<html>
<head>
    <meta charset="UTF-8">
    <title>SunnyDay</title>
    <!--内部引入法-->
    <script>
        //方式1：对象映射方式
        function callAndroid(){
             // 调用安卓的hello方法，由于安卓中添加了对象映射，test.hello()就相当于安卓的      
             //JsInterface().hello()
             test.hello(" i am js code！ ");
         }
        //方式2：重写shouldOverrideUrlLoading 进行拦截
        function callAndroid2(){
             document.location = "js://webview?arg1=111&arg2=222";
         }
        //方式3：WebChromeClient 的 onJsAlert、onJsConfirm、onJsPrompt 方法回调拦截 JS 对话框
        function callAndroid3(){
              //1、首先搞个输入框，触发时安卓可获得回调
              //2、收到回调后安卓处理，点击确定返回输入框值，点击取消返回null
              var result=prompt("js://demo?arg1=111&arg2=222");
              alert("demo " + result);
         }

    </script>
</head>
<body>
<h3>i am a simple html page</h3>
//方式1
<button type="button" id="btn" onclick="callAndroid()">点我即可调用Android的hello方法</button>
//方式2
<button type="button" id="btn2" onclick="callAndroid2()">方式2</button>
//方式3
<button type="button" id="button3" onclick="callAndroid3()">方式3</button>

</body>
</html>>
```



###### 1、addJavascriptInterface 

（1）定义对象映射方法

```java

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
```

（2）映射

```java
        //js调用安卓方法方式1：
        webSettings = web_view_th.settings
        webSettings.javaScriptEnabled = true//允许启用js功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹窗
        web_view_th.loadUrl("file:///android_asset/2.html")
          
        // 定义对象映射，把自定义的JsInterface映射给"test",供js对象使用。
        web_view_th.addJavascriptInterface(JsInterface(), "test")// 一行代码十分简单
```

###### 2、重写webViewClient的shouldOverrideUrlLoading方法

```java
        webSettings = web_view_th.settings
        webSettings.javaScriptEnabled = true//允许启用js功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹窗
        web_view_th.loadUrl("file:///android_asset/2.html")
          
        web_view_th.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                //view?.url //file:///android_asset/2.html ，注意这里的两个url不相同 get的url为   webViewLoad的，request的为动态定义的
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
 private fun printLog() {
        Log.i("JsCallAndroidActivity", "android method：print log ！")
    }
```

###### 3、通过 WebChromeClient 的dialog 回调判断筛选url调用

```java

webSettings = web_view_th.settings
        webSettings.javaScriptEnabled = true//允许启用js功能
        webSettings.javaScriptCanOpenWindowsAutomatically = true // 允许js弹窗
        web_view_th.loadUrl("file:///android_asset/2.html")
  
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
```

###### 4、小结

```java
/**
 * js call android method practise
 * 1、webView的addJavascriptInterface（自定义对象，“映射给的自定义字符串对象”）
 * 这种方式最简单，自定义对象，内部定义一系列方法供js调用。js拿到映射对象即可调用安卓自定义的对象方法
 *
 * 2、重写webViewClient的shouldOverrideUrlLoading方法，筛选请求的url是否为js规定的url，是规定的则调用安卓的相应方法。
 *
 * 3、通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（），原理与2一致。
 * */
```

| 调用方式                                     | 优点    | 缺点                 | 适用场景                    |
| ---------------------------------------- | ----- | ------------------ | ----------------------- |
| WebView.addJavascriptInterface 对象映射      | 方便简洁  | Android 4.2 一下存在漏洞 | Android 4.2 以上相对简单的应用场景 |
| WebViewClient.shouldOverrideUrlLoading 回调拦截 | 不存在漏洞 | 使用复杂，需要协议约束        | 不需要返回值情况下               |
| WebChormeClient.onJsAlert / onJsConfirm / onJsPrompt 方法回调拦截 | 不存在漏洞 | 使用复杂，需要协议约束        | 能满足大多数场景                |



### 五、webView相关优化建议

###### 1、给 WebView 加一个加载进度条

> 为了友好展示，重写 WebChromeClient 的 onProgressChanged 方法。未加载完成时展示loading进度条

###### 2、提高 HTML 网页加载速度，等页面 finsh 在加载图片

```java
public void load () {
    if(Build.VERSION.SDK_INT >= 19) {
        webView.getSettings().setLoadsImagesAutomatically(true);
    } else {
        webView.getSettings().setLoadsImagesAutomatically(false);
    }
}
```

###### 3、onReceivedError 时加载自定义界面

> web的error 页面比较丑，我们可以在加载失败时，展示安卓自定义的错误展示页。

4、动画、银屏、视频 合适加载释放

>动画、银屏、视频 加载会造成cpu、电量消耗可在activity、 fragment的onResume、onStop进行开关控制。



### 六、采坑

###### 1、明文传输控制

> Android P 阻止加载任何 http 的请求,在清单文件application节点 添加android:usesCleartextTraffic="true"

###### 2、Android 5.0 之后 WebView 禁止加载 http 与 https 混合内容 开启如下：

```java
if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
          webview.getSettings().
              setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
```

###### 3、硬件加速开启导致问题

>比如不能打开 PDF，播放视频花屏等等。关闭硬件加速。

###### 4、重写webViewClient的shouldOverrideUrlLoading

> 否则系统不知道你是想用webview打开url 还是使用系统浏览器。

参考：

https://www.jianshu.com/p/3c94ae673e2a

https://blog.csdn.net/carson_ho/article/details/64904691

https://developer.android.google.cn/reference/kotlin/android/webkit/WebView?hl=en

https://blog.csdn.net/harvic880925/article/details/51464687