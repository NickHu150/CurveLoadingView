package zrzn.com.damping_view.fruitview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import zrzn.com.damping_view.R;

/**
 * Created by 胡亚敏 on 2016/4/6.
 */
public class FruitView extends FrameLayout {

    private final int[] mResDrawable = {R.mipmap.p1, R.mipmap.p3, R.mipmap.p5, R.mipmap.p7};
    private int mIndex;//当前图片的下标
    private boolean mSkip = true;
    private final int MAX_HEIGHT = 100;
    private final int DURATION = 600;
    private OnViewAnimEndListener mViewAnimEndListener;
    RotateAnimation rotateAnimation;
    TranslateAnimation translateAnimation, backAnimation;
    AnimationSet animationSet2, animationSet;
    private Context mContext;
    private ImageView mImageView;
    CurveHeadLoadingView curveHeadLoadingView;
    public FruitView(Context context) {
        super(context);
        init(context);
    }

    public FruitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FruitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_fruit_loading,this);
        this.mContext = context;
        mImageView = (ImageView) view.findViewById(R.id.view_fruit_image);
        mImageView.setImageResource(mResDrawable[1]);
        curveHeadLoadingView = (CurveHeadLoadingView) view.findViewById(R.id.curheadloadingview);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.
                WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,MAX_HEIGHT,0,0);
        curveHeadLoadingView.setLayoutParams(layoutParams);
    }


    public void setOnViewAnimEndListener(OnViewAnimEndListener mViewAnimEndListener) {
        this.mViewAnimEndListener = mViewAnimEndListener;
    }

    public void changeIcon() {
        mImageView.clearAnimation();
        if (mSkip) {
            mIndex = 2;
            mSkip = false;
        } else
            mIndex = (mIndex == mResDrawable.length - 1) ? 0 : mIndex + 1;
        mImageView.setImageResource(mResDrawable[mIndex]);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (MAX_HEIGHT + mImageView.getMeasuredHeight()+curveHeadLoadingView.getMeasuredHeight()));
        loadAnim();
    }

    void loadAnim() {
        translateAnimation = new TranslateAnimation(0, 0, 0, MAX_HEIGHT);
        translateAnimation.setDuration(DURATION);
        translateAnimation.setFillAfter(true);
        backAnimation = new TranslateAnimation(0, 0, MAX_HEIGHT, 0);
        backAnimation.setDuration(DURATION);
        backAnimation.setFillAfter(true);
        rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, //0.5 = 1/2的自己父控件的长度
                Animation.RELATIVE_TO_SELF, 0.5f);//0.5 = 1/2的自己的长度
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setDuration(DURATION);


//        rotateAnimation.setAnimationListener(animationListener);


        animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);


        animationSet2 = new AnimationSet(true);
        animationSet2.addAnimation(rotateAnimation);
        animationSet2.addAnimation(backAnimation);


        translateAnimation.setAnimationListener(animationListener);
        backAnimation.setAnimationListener(animationListener);
        mImageView.setAnimation(animationSet);
        animationSet2.setInterpolator(mContext, android.R.anim.decelerate_interpolator);
        animationSet.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
        animationSet.start();

    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation == translateAnimation) {
//                if (mViewAnimEndListener == null)
//                    throw new NullPointerException("please use setonViewAnimEndListener method before use fruitView");
//                else mViewAnimEndListener.onDropDown();
                curveHeadLoadingView.startAnim();
                changeIcon();
                mImageView.setAnimation(animationSet2);
                animationSet2.start();
            } else {
                mImageView.setAnimation(animationSet);
                animationSet.start();
            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };


    /**
     * 此View向下的俯冲动画结束的回调
     */
    public interface OnViewAnimEndListener {
        public void onDropDown();
    }


}
