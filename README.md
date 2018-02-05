## TapeView

一款卷尺风格的滑动选择控件，可自定义样式

### 预览图

![TapeView](tapeView.gif)

### 1.基本使用

Gradle
```java
审核中(- -!)
```
在布局文件中直接使用
```xml
<com.lqh.library.TapeView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        tapeView:current="160"
        tapeView:max="200"
        tapeView:min="100"
        tapeView:interval="10"
        tapeView:space="0.1" />
```

也可以通过java代码设置初始化参数
```java
        tapeView.setMin(100);       //设置最大值
        tapeView.setMax(200);       //设置最小值
        tapeView.setInterval(10);   //设置长刻度之间的间隔数
        tapeView.setSpace(0.1f);    //设置刻度线之间的间距的物理值
        tapeView2.setCurrent(160);  //设置当前值
```

回调
```java
tapeView.setOnScaleChangeListener(new OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scale) {
                //TODO
            }
        });
```

### 2.自定义属性
|name|explain|
|:--|:--|
|min|最小值|
|max|最大值|
|current|当前值|
|interval|长刻度之间的间隔数|
|space|刻度线之间间距的物理值|
|textSize|刻度值的字体大小|
|scaleLineColor|刻度线颜色|
|scaleTextColor|刻度值颜色|
|indicatorColor|指示器颜色|
|backgroundColor|背景颜色|
