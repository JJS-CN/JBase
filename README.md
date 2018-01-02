# JBase
<br>[![](https://jitpack.io/v/JJS-CN/JBase.svg)](https://jitpack.io/#JJS-CN/JBase)
<br>##android 自用
<br>  项目层的gradle需要添加这2个库的支持
<br>        maven { url "https://jitpack.io" }
<br>        maven { url 'https://maven.google.com' }
<br>
<br>
<br>  每个modle需要添加butterkinfe和ARouter支持
<br>        annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
<br>        annotationProcessor 'com.alibaba:arouter-compiler:1.1.4'
<br>  并在每个build.gradle中android->defaultConfig目录下，增加ARouter支持
<br>         javaCompileOptions {
<br>             annotationProcessorOptions {
<br>                  arguments = [ moduleName : project.getName() ]
<br>             }
<br>         }
<br>
<br>
<br>2017.9.14  1：更新ALog的设置log开关方法、2：demo转移至外部、3：更新第三方版本号、4：JJsActivity中删除CreatView方法，修改butterkinfe等使用、5:增加手势图片控制PinchImageView
<br>2017.9.22  1：增加爆炸view 2：更新请求方法
<br>2017.9.29  1：解决activity切换黑屏bug（切换动画设置未2个参数都设置）、2：增加activity背景设置方法（setContentView方法中自动设置，可在之前通过init或update更新）、3：新增Rsa加密工具类（已做分段加密处理）4：新增验证码图片工具类CodeImgUtils
<br>2017.12.25 1：优化RetrofitUtils类，简化代码。提供拦截器替换方法

<br> #使用方法
<br> 1、请将application继承于MultiDexAPP，调用initDebug方法初始化大部分框架。或者从中抽取initDebug方法中的代码也可以。
<br> 2、BaseLauncherActivity,BaseObserver,BaseStore,BaseActivity 请继承之后使用，其他随意