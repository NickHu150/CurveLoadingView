package zrzn.com.damping_view.fruitview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by 胡亚敏 on 2016/3/25.
 */
public class CurveHeadLoadingView extends View {
    private Context mContext;
    Path mPath;
    private int mTextColor;//画出文字的具体颜色
    private Paint mTextPaint;//文字画笔
    private final int MIN_HEIGHT = 200;//最小的高度
    private int PAINT_TEXTSIZE = 40;
    private final int PAINT_TEXT_BASEIINE = PAINT_TEXTSIZE;//BASELINE的高度
    private String mResText;//画出来的文字
    private int mTextWidth;//文字的宽度
    private  String mDefaultText ;
    private final int DEFAULT_RECF_SPACE = 6;//默认的画弧形的时候的间距,值越大速度越快，不能超过最大值
    private final int MAX_RECF_SPACE = 36;//最大的画弧形的时候的间距
    private final int MIN_RECF_SPACE = -12;//最大的画弧形的时候的间距
    private int mRecfSpace = 0;//矩形RECF间距
    private final int STATUS_DOWN_CURVE = 0;//向下弯曲的状态
    private final int STATUS_UP_CURVE = 1;//向上恢复的状态
    private final int STATUS_FLAT_CURVE = 2;//平的状态
    private int mCurveStatus = STATUS_FLAT_CURVE;
    private final int MAX_SPRING_COUNT = 18;//来回弹动的时间
    private int mSringCount = MAX_SPRING_COUNT;//当前弹动的次数

    public CurveHeadLoadingView(Context context) {
        super(context);
        this.mContext = context;
        initPaint();
    }

    public CurveHeadLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    public CurveHeadLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initPaint();
    }

    void initPaint() {
        mDefaultText = "新鲜到家每一天";
        mResText = mDefaultText;
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#666666"));
        mTextPaint.setTextSize(PAINT_TEXTSIZE);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextWidth = (int) mTextPaint.measureText(mResText);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int baseLineSpace = 20;
        setMeasuredDimension(mTextWidth, PAINT_TEXTSIZE+baseLineSpace);
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mTextPaint.setColor(color);
    }

    public void startAnim() {
        mSringCount = 0;
        mCurveStatus = STATUS_DOWN_CURVE;
        invalidate();
    }





    public void setText(@StringRes int res) {
        mResText = mContext.getString(res);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLinePathAndText(canvas);


    }

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

    void reset(Canvas canvas){
        mRecfSpace = 0;
        drawArcPath();
        mCurveStatus = STATUS_FLAT_CURVE;

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    /**
     * 当矩形间距这块如果到了最大弯曲值就
     * 每次递减，反之则递增，平行状态不变
     *
     * @return
     */
    int getRecfSpace() {
        if (mCurveStatus == STATUS_DOWN_CURVE) {
            return mRecfSpace + DEFAULT_RECF_SPACE;
        } else if (mCurveStatus == STATUS_UP_CURVE) {
            return mRecfSpace - DEFAULT_RECF_SPACE;
        } else {
            Log.v("huyamin","return 0");
            return 0;
        }
    }

    /**
     * 初始化直线路径
     */
    void drawLinePath() {
        mPath.moveTo(0, PAINT_TEXT_BASEIINE); //设定起始点
        mPath.lineTo(mTextWidth, PAINT_TEXT_BASEIINE);//第一条直线的终点，也是第二条直线的起点
        mPath.close();
    }

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


}
