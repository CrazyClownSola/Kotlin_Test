package com.sola.github.java_library.globe_round;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sola.github.java_library.R;

/**
 * Created by Sola
 * 2017/6/16.
 */

public class GlobeRoundView extends View {

    private static final String TAG = "Sola_Globe_Round";

    private static final int DEFAULT_DURATION = 5000;
    private static final int DEFAULT_RADIUS = 420;

    private boolean isRounding;

    private float sweepAngle, rectRange = 1;

    private int centerX, centerY, roundDegree, duration, mRadius;

    private Paint mPaint, mClearPaint;

    private AnimatorSet animatorSet;

    public GlobeRoundView(Context context) {
        this(context, null);
    }

    public GlobeRoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public GlobeRoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initParams();
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public void setRectRange(float rectRange) {
        this.rectRange = rectRange;
        invalidate();
    }

    public void start() {
        if (animatorSet != null && !animatorSet.isRunning())
            animatorSet.start();
    }

    public void destroy() {
        if (animatorSet != null && animatorSet.isRunning())
            animatorSet.end();
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top, left, right, bottom;
        View child;
//        for (int i = 0; i < getChildCount(); i++) {
//
//        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
////        if (!isRounding) {
////            canvas.drawPaint(createClearPaint());
////            return;
////        }
//
//
//    }

    private int lastRange;

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw() sw[" + sweepAngle + "]");

//        canvas. sav();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(sweepAngle);
//        canvas.drawLine(0, 0, 0, -mRadius, createPaint(R.color.color_default_gray));
//        canvas.drawLine(0, 0, mRadius, 0, createPaint(R.color.color_default_blue));
//        canvas.drawLine(0, 0, 0, mRadius, createPaint(R.color.color_default_black));
//        canvas.drawLine(0, 0, -mRadius, 0, createPaint(R.color.color_default_red));

        int range = (int) (60 * rectRange);

        canvas.save();
        canvas.translate(0, -mRadius);
        canvas.rotate(-sweepAngle);
        canvas.drawRect(-range, -range, range, range, createPaint(R.color.color_default_blue));
        canvas.restore();
        canvas.save();
        canvas.translate(0, mRadius);
        canvas.rotate(-sweepAngle);
        canvas.drawRect(-range, -range, range, range, createPaint(R.color.color_default_blue));
        canvas.restore();
        canvas.save();
        canvas.translate(-mRadius + 80, 0);
        canvas.rotate(-sweepAngle);
        canvas.drawRect(-range, -range, range, range, createPaint(R.color.color_default_blue));
        canvas.restore();
        canvas.save();
        canvas.translate(mRadius - 80, 0);
        canvas.rotate(-sweepAngle);
        canvas.drawRect(-range, -range, range, range, createPaint(R.color.color_default_blue));
        canvas.restore();
//        canvas.drawRect(-mRadius / 2, -3 * mRadius / 2, mRadius / 2, -mRadius / 2, createPaint(R.color.color_default_blue));

//        canvas.restore();
        super.onDraw(canvas);

    }
    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return isRounding;
//    }

    private int currentX, currentY;
    private boolean isTouch;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = (int) event.getX();
                currentY = (int) event.getY();
                final Animator scaleAnim = ObjectAnimator.ofFloat(this, "rectRange", 1f, 0.4f);
                scaleAnim.setDuration(200);
                scaleAnim.start();
                Log.d(TAG, "onTouchEvent() [" + event + "]");
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (x - currentX != 0) {
                    float offset = ((x - currentX) * 360 / getWidth() + (currentY - y) * 360 / getHeight());
                    Log.d(TAG, "Move() sw[" + sweepAngle + "]offset[" + offset + "]x[" + x + "]cx[" + currentX + "]w[" + getWidth() + "]");
                    setSweepAngle(sweepAngle + offset);
                }
                currentX = x;
                currentY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                sweepAngle = sweepAngle % 360;
                setRectRange(1);
                break;
        }
        return true;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        duration = DEFAULT_DURATION;
        mRadius = DEFAULT_RADIUS;
    }

    private void initParams() {
        buildAnimator();
    }

    private float cacheInput;
    private int cacheTime;

    private void buildAnimator() {
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
            final Animator scaleAnim = ObjectAnimator.ofFloat(this, "rectRange", 1f, 0.4f);
            scaleAnim.setDuration(200);
            final Animator scaleOutAnim = ObjectAnimator.ofFloat(this, "rectRange", 0.4f, 1);
            scaleOutAnim.setDuration(200);
            ObjectAnimator loadingAnim = ObjectAnimator.ofFloat(this, "sweepAngle", 0, 360);
            loadingAnim.setRepeatMode(ValueAnimator.RESTART);
            loadingAnim.setRepeatCount(ValueAnimator.INFINITE);
            loadingAnim.setDuration(duration);
            loadingAnim.setInterpolator(new LinearInterpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 减少不必要的数据变化
                    int currentTime = (int) System.currentTimeMillis();
                    if (input == 0 || input == 1 || cacheTime == 0 || currentTime - cacheTime >= 16) {
                        cacheTime = currentTime;
                        cacheInput = input;
                        return super.getInterpolation(input);
                    } else {
                        return cacheInput;
                    }
//                    return super.getInterpolation(input);
                }
            });
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    isRounding = false;
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    isRounding = true;
                }
            });
            animatorSet.playSequentially(scaleAnim, loadingAnim, scaleOutAnim);
        }
    }

    private Paint createPaint(int color) {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setStrokeWidth(1);
        }
        mPaint.setColor(ContextCompat.getColor(getContext(), color));
        return mPaint;
    }

    private Paint createPaint(int color, int alpha) {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(1);
        }
        mPaint.setColor(ContextCompat.getColor(getContext(), color));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(alpha);
        return mPaint;
    }

    private Paint createClearPaint() {
        if (mClearPaint == null) {
            mClearPaint = new Paint();
//            mClearPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_default_white));
            mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        }
        return mClearPaint;
    }

}
