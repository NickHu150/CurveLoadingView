###带有阻尼效果（橡皮筋效果）跳动的Loading动画
> 之前公司需要我等程序员左右一个特殊的loading效果，我们的APP是生鲜O2O的某公司，将原来listview的下拉loading动画更换成一个带有阻尼效果也就是橡皮筋效果的loading具体的就是水果往下砸的过程中，下面的文字会弯曲，会把水果原封不动的弹回上面。动手之前觉得这个效果很难，动画效果太丰富，对于文字的弯曲弹回效果几乎是束手无策，后来经过同事的点拨，有了一个大概的方向。并最终实现了先看一个大概的效果图
![这里写图片描述](http://img.blog.csdn.net/20160415114455324)



* 1    至于怎么去实现文字弯曲的效果，我之前想过文字用切图，然后用扭曲切图的方式来实现文字弯曲，结果发现这个做法过于复杂且最终效果不太好，所以经过同事提醒使用了另外一种做法，利用Path路径来做，通过实时的变化Path路径来实现动画效果，同时应用canvas.drawTextOnPath的方法可以让文字根据Path路径来绘制，这样就实现了文字的弯曲效果

* 2 水果动画加速的效果，这个就比较简单，利用animation原生的加速器即可实现，上下坐标移动，且绕自身旋转，这里我们用AnimationSet集合把RotateAnimation和TranslateAnimation同时应用，并且在TranslatAnimtion的结束的时候更换view的图片，达到砸中文字的时候图片立刻更换的效果，水果的图片要放在一个固定的数组里面

* 3为了实现动画的平滑过渡，这里要求移动动画和RotateAnimation的速度一致，RotateAnimation绕360度的结束的时候正好是TranslateAnimation从上到下的过程，否则动画就会很生硬，体验可以改DEMO里面的数值来体验

* 4 还有一点要注意，canvas.drawText的方法的Y坐标并不是指画文字的左上角的坐标，而是文字的baseline的坐标，至于什么事baseline，请百度

* 5本例最大的难点还是文字弯曲的效果，其中一开始需要画的是直线，等到animationEnd的时候我们要让文字开始弯曲，弯曲的过程中要设置一个变量，变量每次都在增长，弯曲的程度会越来越大，并且要设置一个封顶值，到了MAX之后便要往回收缩，文字弯曲的时候还要设置三种状态，平直状态，向下弯曲过程中的状态以及往回收缩的状态，并且你的变量值要根据当前是何种状态来决定变量是继续增大还是减小。

```
  /**
     * 画出直线的文字
     *
     * @param canvas
     */
    void drawLinePathAndText(Canvas canvas) {
        if (mPath == null) {
            mPath = new Path();
            drawLinePath();
        } else {
            drawArcPath();
            mRecfSpace = getRecfSpace();
            if (mRecfSpace >= MAX_RECF_SPACE){
                mCurveStatus = STATUS_UP_CURVE;
            }else if (mRecfSpace <= MIN_RECF_SPACE){
                mCurveStatus = STATUS_DOWN_CURVE;
            }

        }
        if (mSringCount<MAX_SPRING_COUNT){
            mSringCount++;
            invalidate();
        }else reset(canvas);
        canvas.drawTextOnPath(mResText, mPath, 0, 0, mTextPaint);
    }
```


```java
    /**
     * 画出弧线路径
     */
    void drawArcPath() {
        mPath.reset();
//        RectF rectF = new RectF(0, PAINT_TEXT_BASEIINE, mTextWidth, PAINT_TEXT_BASEIINE + mRecfSpace);
//        mPath.addOval(rectF, Path.Direction.CCW);
        mPath.moveTo(0, PAINT_TEXT_BASEIINE); //设定起始点
//        mPath.lineTo(mTextWidth/5,PAINT_TEXT_BASEIINE);
        mPath.quadTo(0,PAINT_TEXT_BASEIINE,5,PAINT_TEXT_BASEIINE);
        mPath.quadTo(mTextWidth/2,PAINT_TEXT_BASEIINE + mRecfSpace,mTextWidth-5,PAINT_TEXT_BASEIINE);
        mPath.quadTo(mTextWidth*5/6,PAINT_TEXT_BASEIINE,mTextWidth,PAINT_TEXT_BASEIINE);
        mPath.close();
    }
```
* 6最后一个难点就是弧线的绘制，一开始用的是根据rect 来drawArc，这种方法根据rect矩形中的包含的最大的椭圆形，从椭圆形的右侧绘制到左侧形成一个规则的弧线，后来看这种效果用在这里并不好，后来在网上发现了path.quadTO方法，这个方法默认是绘制一条直线，但是他可以再前面两个参数设置一个XY坐标，相当于人的手把这条直线的某个点拉到了XY这个坐标的位置， 从而实现了弧线的效果，弧线的大小就取决于这个Y值。Y值越大，等于这个人把这条线拉的越狠，弯曲程度自然越大。不说了，看代码吧

代码Demo：https://github.com/Huyamin150/-dampView-springingView  望Follow一下或者watch一下，以后会有更多原创出品


###个人原创，如需转载，请注明出处

