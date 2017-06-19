package com.sola.github.java_library.loading_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sola.github.java_library.R;


/**
 * Created by 禄骥
 * 2016/9/7.
 */
@SuppressWarnings("unused")
public class EdgeLoadingView extends View {

    // ===========================================================
    // Constants
    // ===========================================================

//    private final static String TAG = "Sola_Edge";

    private final static int DEFAULT_CYCLE_DURATION = 2400;
    private final static float DEFAULT_CYCLE_RADIUS = 60;
//    private final static float MAX_RADIUS = 100;

    // ===========================================================
    // Fields
    // ===========================================================

    private Paint mPaint, mClearPaint;

    private Path disappearPath, appearPath, coverPath, boundsPath;

    private ObjectAnimator loading;

    private float sweepAngle, mRadius, innerAngle;

    private int triangleCount, duration, roundDegree;

    private int[] triangleColors;
    private RectF rectF;

    private boolean isRunning;

    // ===========================================================
    // Constructors
    // ===========================================================

    public EdgeLoadingView(Context context) {
        this(context, null);
    }

    public EdgeLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EdgeLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initParams();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @SuppressWarnings("unused")
    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
//        Log.d(TAG, "setSweepAngle: ");
    }

    @SuppressWarnings("unused")
    public void setTriangleColors(@ColorInt int[] colors) {
        destroy();
        if (colors == null)
            throw new NullPointerException("EdgeLoadingView setColors is Empty");
        int length = 360 % colors.length == 0 ? colors.length : (colors.length - 360 % colors.length);
        this.triangleColors = new int[length];
        // 数组复制
        System.arraycopy(colors, 0, this.triangleColors, 0, length);
        triangleCount = length;
        innerAngle = 360 / triangleCount;
        clean();
        invalidate();
//        start();
    }

    @SuppressWarnings("unused")
    public void start(@ColorInt int[] colors) {
        setTriangleColors(colors);
        start();
    }

    public void start() {
        if (loading != null && !loading.isRunning())
            loading.start();
    }

    @SuppressWarnings("unused")
    public void destroy() {
        if (loading != null && loading.isRunning())
            loading.cancel();
        clean();
        invalidate();
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
//        invalidate();
    }
// ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        Log.e(TAG, "onDetachedFromWindow: ");
        destroy();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            if (!isRunning) {
            canvas.drawPaint(createClearPaint());
            return;
        }
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);// 画布调整到中心位置
        canvas.rotate(roundDegree);
//        canvas.drawLine(0, 0, 0, -120, createPaint(R.color.color_default_black));// 辅助线1
//
//        // 辅助线2
//        canvas.save();
//        canvas.rotate(sweepAngle);
//        canvas.drawLine(0, 0, 0, -120, createPaint(R.color.color_default_red));
//        canvas.restore();

        drawRhombus(canvas);

        canvas.restore();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet,
                R.styleable.EdgeLoadingView);
        if (attr == null) {
            return;
        }
        try {
            duration = attr.getInteger(
                    R.styleable.EdgeLoadingView_edge_cycle_duration,
                    DEFAULT_CYCLE_DURATION);
            mRadius = attr.getDimension(
                    R.styleable.EdgeLoadingView_edge_cycle_radius,
                    DEFAULT_CYCLE_RADIUS
            );
        } finally {
            attr.recycle();
        }
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    /**
     * 初始化参数
     */
    private void initParams() {
        buildAnimator();
        rectF = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        setTriangleColors(new int[]{
                R.color.color_edge_loading_1,
                R.color.color_edge_loading_2,
                R.color.color_edge_loading_3,
                R.color.color_edge_loading_4,
                R.color.color_edge_loading_5,
                R.color.color_edge_loading_6
        });
    }

    private void buildAnimator() {
        if (loading == null) {
            loading = ObjectAnimator.ofFloat(this, "sweepAngle", 0, 720);
            loading.setRepeatMode(ValueAnimator.RESTART);
            loading.setRepeatCount(ValueAnimator.INFINITE);
            loading.setDuration(duration);
            loading.setInterpolator(new LinearInterpolator());
            loading.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isRunning = true;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    roundDegree += innerAngle;
                    if (roundDegree > 360)
                        roundDegree = 0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isRunning = false;
                }
            });
        }
    }

    /**
     * 开始绘制菱形
     *
     * @param canvas 画布
     */
    private void drawRhombus(Canvas canvas) {
        if (sweepAngle < 360) {
            drawBackground(canvas, false);
            drawAppear(canvas);
        } else {
            drawDisappear(canvas);
            drawBackground(canvas, true);
        }
    }

    private void drawBackground(Canvas canvas, boolean isInverse) {
        if (boundsPath == null) {
            boundsPath = new Path();
        }
        if (boundsPath.isEmpty()) {
            boundsPath.moveTo(0, 0);
            boundsPath.lineTo(0, -mRadius);
            float x = mRadius * (float) Math.abs(Math.sin(Math.toRadians(innerAngle)));
            float y = -mRadius * (float) Math.abs(Math.cos(Math.toRadians(innerAngle)));
            boundsPath.lineTo(x, y);
            boundsPath.close();
        }

        int degree = (int) (sweepAngle / innerAngle);
        canvas.save();
        int i = 0;
        if (!isInverse)
            while (i < degree) {
                if (i != 0)
                    canvas.rotate(innerAngle);
                canvas.drawPath(boundsPath, createPaint(triangleColors[i]));
                i++;
            }
        else {
            i = triangleCount * 2 - 1;
            while (i > degree) {
                canvas.rotate(-innerAngle);
                canvas.drawPath(boundsPath, createPaint(triangleColors[i - triangleCount]));
                i--;
            }
        }
        canvas.restore();

    }

    // 绘制出现
    private void drawAppear(Canvas canvas) {
        canvas.save();
        clipCanvas(canvas);
        canvas.rotate(sweepAngle);

        if (appearPath == null) {
            appearPath = new Path();
        }
        if (appearPath.isEmpty()) {
            appearPath.moveTo(0, 0);
            appearPath.lineTo(0, -mRadius);
            float x = -mRadius * (float) Math.abs(Math.sin(Math.toRadians((triangleCount - 1) * innerAngle)));
            float y = -mRadius * (float) Math.abs(Math.cos(Math.toRadians((triangleCount - 1) * innerAngle)));
            appearPath.lineTo(x, y);
            appearPath.close();
            appearPath.addArc(rectF, (-90 - innerAngle), innerAngle);
        }
        float alpha = (sweepAngle % innerAngle / innerAngle) * 255f;
        int degree = (int) (sweepAngle / innerAngle);
        canvas.drawPath(appearPath, createPaint(triangleColors[degree], (int) alpha));
        canvas.restore();
    }

    // 绘制消失
    private void drawDisappear(Canvas canvas) {
        canvas.save();
        clipCanvas(canvas);
        canvas.rotate(sweepAngle);

        if (disappearPath == null) {
            disappearPath = new Path();
        }
        if (disappearPath.isEmpty()) {
            disappearPath.moveTo(0, 0);
            disappearPath.lineTo(0, -mRadius);
            float x = mRadius * (float) Math.abs(Math.sin(Math.toRadians(innerAngle)));
            float y = -mRadius * (float) Math.abs(Math.cos(Math.toRadians(innerAngle)));
            disappearPath.lineTo(x, y);
            disappearPath.close();
            disappearPath.addArc(rectF, -90, innerAngle);
        }
        float alpha = (1 - sweepAngle % innerAngle / innerAngle) * 255f;
        int degree = (int) ((sweepAngle - 360) / innerAngle);
        if (degree >= triangleColors.length)
            degree = triangleColors.length - 1;
        canvas.drawPath(disappearPath, createPaint(triangleColors[degree], (int) alpha));
        canvas.restore();
    }

    /**
     * 剪裁画布，将整个画布剪裁成多边形
     *
     * @param canvas 画布
     */
    private void clipCanvas(Canvas canvas) {
        if (coverPath == null) {
            coverPath = new Path();
        }
        if (coverPath.isEmpty()) {
            float innerAngle = 360 / triangleCount;
            for (int i = 0; i < triangleCount; i++) {
                float x = mRadius * (float) Math.sin(Math.toRadians(i * innerAngle));
                float y = -mRadius * (float) Math.cos(Math.toRadians(i * innerAngle));
                if (coverPath.isEmpty()) {
                    coverPath.moveTo(x, y);
                } else {
                    coverPath.lineTo(x, y);
                }
            }
            if (!coverPath.isEmpty()) {
                coverPath.close();
            }
        }
        if (!coverPath.isEmpty()) {
            canvas.clipPath(coverPath);
//            canvas.drawColor(ContextCompat.getColor(getContext(), R.color.color_default_green));
        }
    }

    public void clean() {
        if (coverPath != null)
            coverPath.reset();
        if (appearPath != null)
            appearPath.reset();
        if (disappearPath != null)
            disappearPath.reset();
        if (boundsPath != null)
            boundsPath.reset();
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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
