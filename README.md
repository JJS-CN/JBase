# JBase
<br>##android 自用
<br>项目层的gradle需要添加这2个库的支持
<br>        maven { url "https://jitpack.io" }
<br>        maven { url 'https://maven.google.com' }
<br>  每个modle需要添加butterkinfe支持
<br>        annotationProcessor 'com.jakewharton:butterknife-compiler:8.6.0'
<br>
<br>0.0.7
<br>0.0.8  1：删除CreatView抽象、手动将butterkinfe赋予变量mUnbinder、BaseDialog替换为BaseDialogFragment、
<br>2：增加手势图片控制PinchImageView
<br>
<br>
<br>2017.9.14  1：更新ALog的设置log开关方法、2：demo转移至外部、3：更新第三方版本号、4：JJsActivity中删除CreatView方法，修改butterkinfe等使用、5:增加手势图片控制PinchImageView
<br>2017.9.22  1：增加爆炸view 2：更新请求方法
