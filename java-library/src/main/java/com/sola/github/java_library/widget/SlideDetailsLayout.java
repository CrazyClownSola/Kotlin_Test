package com.sola.github.java_library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sola.github.java_library.R;


@SuppressWarnings("unused")
public class SlideDetailsLayout extends ViewGroup {

    /**
     * Callback for panel OPEN-CLOSE status changed.
     */
    interface OnSlideDetailsListener {
        /**
         * Called after status changed.
         *
         * @param status {@link Status}
         */
        void onStatucChanged(Status status);
    }

    private enum Status {
        /**
         * Panel is closed
         */
        CLOSE,
        /**
         * Panel is opened
         */
        OPEN;

        public static Status valueOf(int stats) {
            if (0 == stats) {
                return CLOSE;
            } else if (1 == stats) {
                return OPEN;
            } else {
                return CLOSE;
            }
        }
    }

    private static final float DEFAULT_PERCENT = 0.2f;
    private static final int DEFAULT_DURATION = 300;

    private View mFrontView;
    private View mBehindView;

    private float mTouchSlop;
    private float mInitMotionY;

    private View mTarget;
    private float mSlideOffset, maxRange;
    private Status mStatus = Status.CLOSE;
    private boolean isFirstShowBehindView = true;
    private float mPercent = DEFAULT_PERCENT;
    private long mDuration = DEFAULT_DURATION;
    private int mDefaultPanel = 0;

    private OnSlideDetailsListener mOnSlideDetailsListener;

    public SlideDetailsLayout(Context context) {
        this(context, null);
    }

    public SlideDetailsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideDetailsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideDetailsLayout, defStyleAttr, 0);
        mPercent = a.getFloat(R.styleable.SlideDetailsLayout_percent, DEFAULT_PERCENT);
        mDuration = a.getInt(R.styleable.SlideDetailsLayout_duration, DEFAULT_DURATION);
        mDefaultPanel = a.getInt(R.styleable.SlideDetailsLayout_default_panel, 0);
        a.recycle();

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * Set the callback of panel OPEN-CLOSE status.
     *
     * @param listener {@link OnSlideDetailsListener}
     */
    public void setOnSlideDetailsListener(OnSlideDetailsListener listener) {
        this.mOnSlideDetailsListener = listener;
    }

    /**
     * Open pannel smoothly.
     *
     * @param smooth true, smoothly. false otherwise.
     */
    public void smoothOpen(boolean smooth) {
        if (mStatus != Status.OPEN) {
            mStatus = Status.OPEN;
            final float height = -getMeasuredHeight();
            animatorSwitch(0, height, true, smooth ? mDuration : 0);
        }
    }

    /**
     * Close pannel smoothly.
     *
     * @param smooth true, smoothly. false otherwise.
     */
    public void smoothClose(boolean smooth) {
        if (mStatus != Status.CLOSE) {
            mStatus = Status.CLOSE;
            final float height = -getMeasuredHeight();
            animatorSwitch(height, 0, true, smooth ? mDuration : 0);
        }
    }

    /**
     * Set the float value for indicate the moment of switch panel
     *
     * @param percent (0.0, 1.0)
     */
    public void setPercent(float percent) {
        this.mPercent = percent;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final int childCount = getChildCount();
        if (1 >= childCount) {
            throw new RuntimeException("SlideDetailsLayout only accept childs more than 1!!");
        }

        mFrontView = getChildAt(0);

        int scrollRange =
                computeVerticalScrollRange((NestedScrollView) ((ViewGroup) mFrontView).getChildAt(1));
        int barOffset = 400;
        maxRange = scrollRange - mFrontView.getHeight() + barOffset;
        mBehindView = getChildAt(1);

        // set behindview's visibility to GONE before show.
        //mBehindView.setVisibility(GONE);
        if (mDefaultPanel == 1) {
            post(new Runnable() {
                @Override
                public void run() {
                    smoothOpen(false);
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int pWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int pHeight = MeasureSpec.getSize(heightMeasureSpec);

        final int childWidthMeasureSpec =
                MeasureSpec.makeMeasureSpec(pWidth, MeasureSpec.EXACTLY);
        final int childHeightMeasureSpec =
                MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.EXACTLY);

        View child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            // skip measure if gone
            if (child.getVisibility() == GONE) {
                continue;
            }

            measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec);
        }

        setMeasuredDimension(pWidth, pHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("Sola_Silde", "onLayout() called with: off[" + mSlideOffset + "] = [" + changed + "], l = [" + l + "], t = [" + t + "], r = [" + r + "], b = [" + b + "]");
        int top;
        int bottom;
//        if (mSlideOffset == 0 && mStatus == Status.CLOSE)
//            return;
        final int offset = (int) mSlideOffset;
        View child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);

            // skip layout
            if (child.getVisibility() == GONE) {
                continue;
            }

            if (child == mBehindView) {
                top = b + offset;
                bottom = top + b - t;
            } else {
                top = t + offset;
                bottom = b + offset;
            }

            child.layout(l, top, r, bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float mInitMotionX = ev.getX();
                mInitMotionY = ev.getY();
            }
            case MotionEvent.ACTION_MOVE: {
                int yDiff = (int) (ev.getY() - mInitMotionY);
                return mStatus == Status.CLOSE && ((yDiff < 0 && Math.abs(yDiff) >= maxRange) || (yDiff < 0 && cacheY < 0 && Math.abs(cacheY) >= maxRange));
//                int xDiff = (int) (ev.getX() - mInitMotionX);
//                return Math.abs(yDiff) > mTouchSlop && Math.abs(yDiff) >= Math.abs(xDiff) && !(mStatus == Status.CLOSE && yDiff > 0 || mStatus == Status.OPEN && yDiff < 0);
            }

        }

        return super.onInterceptTouchEvent(ev);
//        ensureTarget();
//        if (null == mTarget) {
//            return false;
//        }
//
//        if (!isEnabled()) {
//            return false;
//        }
//
//        final int aciton = MotionEventCompat.getActionMasked(ev);
//
//        boolean shouldIntercept = false;
//        switch (aciton) {
//            case MotionEvent.ACTION_DOWN: {
//                mInitMotionX = ev.getX();
//                mInitMotionY = ev.getY();
//                return super.onInterceptTouchEvent(ev);
//            }
//            case MotionEvent.ACTION_MOVE: {
//                final float x = ev.getX();
//                final float y = ev.getY();
//
//                final float xDiff = x - mInitMotionX;
//                final float yDiff = y - mInitMotionY;
//
//                if (canChildScrollVertically((int) yDiff)) {
//                    return super.onInterceptTouchEvent(ev);
//                } else {
//                    final float xDiffabs = Math.abs(xDiff);
//                    final float yDiffabs = Math.abs(yDiff);
//
//                    // intercept rules：
//                    // 1. The vertical displacement is larger than the horizontal displacement;
//                    // 2. Panel stauts is CLOSE：slide up
//                    // 3. Panel status is OPEN：slide down
//                    if (yDiffabs > mTouchSlop && yDiffabs >= xDiffabs
//                            && !(mStatus == Status.CLOSE && yDiff > 0
//                            || mStatus == Status.OPEN && yDiff < 0)) {
//                        return true;
//                    }
//                }
//                return super.onInterceptTouchEvent(ev);
//            }
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL: {
//                return super.onInterceptTouchEvent(ev);
//            }
//
//        }
//        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        ensureTarget();
        if (null == mTarget) {
            return false;
        }

        if (!isEnabled()) {
            return false;
        }

        boolean wantTouch = true;
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                // if target is a view, we want the DOWN action.
                if (mTarget instanceof View) {
                    wantTouch = true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                final float y = ev.getY();
                final float yDiff = y - mInitMotionY;

                if (canChildScrollVertically(((int) yDiff))) {
                    wantTouch = false;
                } else {
                    processTouchEvent(yDiff);
                    wantTouch = true;
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mStatus == Status.CLOSE) {
                    final float y = ev.getY();
                    final float yDiff = y - mInitMotionY;
                    cacheY += yDiff;
                } else cacheY = 0;
                // 当第一个页面呈现的时候，并且第一个页面可以滑动
                finishTouchEvent();
                wantTouch = false;
                break;
            }
        }
        return wantTouch;
    }

    private int cacheY;

    public int computeVerticalScrollRange(NestedScrollView scrollview) {
        final int count = scrollview.getChildCount();
        final int contentHeight = scrollview.getHeight() - scrollview.getPaddingBottom() - scrollview.getPaddingTop();
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = scrollview.getChildAt(0).getBottom();
        final int scrollY = scrollview.getScrollY();
        final int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }

        return scrollRange;
    }

    /**
     * @param offset Displacement in vertically.
     */
    private void processTouchEvent(final float offset) {
        if (Math.abs(offset) < mTouchSlop) {
            return;
        }

        final float oldOffset = mSlideOffset;
        // pull up to open
        if (mStatus == Status.CLOSE) {
            // reset if pull down
            if (offset >= 0) {
                mSlideOffset = 0;
            } else {
// 两种情况下，第一次 单次滑动的距离大于Range极限的时候
                if (cacheY < 0 && Math.abs(cacheY) >= maxRange) {
                    mSlideOffset = offset;
                } else {
//                    cacheY -= offset;
                    mSlideOffset = 0;
                }
            }
            Log.d("Sola_Slide", "cacheY[" + cacheY + "]offset[" + offset + "]");

            if (mSlideOffset == oldOffset) {
                return;
            }

            // pull down to close
        } else if (mStatus == Status.OPEN) {
            cacheY = 0;
            final float pHeight = -getMeasuredHeight();
            // reset if pull up
            if (offset <= 0) {
                mSlideOffset = pHeight;
            } else {
                mSlideOffset = pHeight + offset;
            }

            if (mSlideOffset == oldOffset) {
                return;
            }
        }
        // relayout
        requestLayout();
    }

    /**
     * Called after gesture is ending.
     */
    private void finishTouchEvent() {
        final int pHeight = getMeasuredHeight();
        final int percent = (int) (pHeight * mPercent);
        final float offset = mSlideOffset;

        boolean changed = false;

        if (Status.CLOSE == mStatus) {
            if (offset <= -percent) {
                mSlideOffset = -pHeight;
                mStatus = Status.OPEN;
                changed = true;
            } else {
                // keep panel closed
                mSlideOffset = 0;
            }
        } else if (Status.OPEN == mStatus) {
            if ((offset + pHeight) >= percent) {
                mSlideOffset = 0;
                mStatus = Status.CLOSE;
                changed = true;
            } else {
                // keep panel opened
                mSlideOffset = -pHeight;
            }
        }

        animatorSwitch(offset, mSlideOffset, changed);
    }

    private void animatorSwitch(final float start, final float end) {
        animatorSwitch(start, end, true, mDuration);
    }

    private void animatorSwitch(final float start, final float end, final long duration) {
        animatorSwitch(start, end, true, duration);
    }

    private void animatorSwitch(final float start, final float end, final boolean changed) {
        animatorSwitch(start, end, changed, mDuration);
    }

    private void animatorSwitch(final float start,
                                final float end,
                                final boolean changed,
                                final long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSlideOffset = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (changed) {
                    if (mStatus == Status.OPEN) {
                        checkAndFirstOpenPanel();
                    }

                    if (null != mOnSlideDetailsListener) {
                        mOnSlideDetailsListener.onStatucChanged(mStatus);
                    }
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    /**
     * Whether the closed pannel is opened at first time.
     * If open first, we should set the behind view's visibility as VISIBLE.
     */
    private void checkAndFirstOpenPanel() {
        if (isFirstShowBehindView) {
            isFirstShowBehindView = false;
            mBehindView.setVisibility(VISIBLE);
        }
    }

    /**
     * When pulling, target view changed by the panel status. If panel opened, the target is behind view.
     * Front view is for otherwise.
     */
    private void ensureTarget() {
        if (mStatus == Status.CLOSE) {
            mTarget = mFrontView;
        } else {
            mTarget = mBehindView;
        }
    }

    /**
     * Check child view can srcollable in vertical direction.
     *
     * @param direction Negative to check scrolling up, positive to check scrolling down.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    protected boolean canChildScrollVertically(int direction) {
        if (mTarget instanceof AbsListView) {
            return canListViewSroll((AbsListView) mTarget);
        } else if (mTarget instanceof FrameLayout ||
                mTarget instanceof RelativeLayout ||
                mTarget instanceof LinearLayout) {
            View child;
            for (int i = 0; i < ((ViewGroup) mTarget).getChildCount(); i++) {
                child = ((ViewGroup) mTarget).getChildAt(i);
                if (child instanceof AbsListView) {
                    return canListViewSroll((AbsListView) child);
                }
            }
        }
//        return ViewCompat.dispatchNestedScroll(mTarget, -direction, 0, 0, 0, new int[]{0, 628});
        return ViewCompat.canScrollVertically(mTarget, -direction);
    }

    protected boolean canListViewSroll(AbsListView absListView) {
        if (mStatus == Status.OPEN) {
            return absListView.getChildCount() > 0
                    && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                    .getTop() <
                    absListView.getPaddingTop());
        } else {
            final int count = absListView.getChildCount();
            return count > 0
                    && (absListView.getLastVisiblePosition() < count - 1
                    || absListView.getChildAt(count - 1)
                    .getBottom() > absListView.getMeasuredHeight());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.offset = mSlideOffset;
        ss.status = mStatus.ordinal();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mSlideOffset = ss.offset;
        mStatus = Status.valueOf(ss.status);

        if (mStatus == Status.OPEN) {
            mBehindView.setVisibility(VISIBLE);
        }

        requestLayout();
    }

    private static class SavedState extends BaseSavedState {

        private float offset;
        private int status;

        /**
         * Constructor used when reading from a parcel. Reads the state of the superclass.
         */
        SavedState(Parcel source) {
            super(source);
            offset = source.readFloat();
            status = source.readInt();
        }

        /**
         * Constructor called by derived classes when creating their SavedState objects
         *
         * @param superState The state of the superclass of this view
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(offset);
            out.writeInt(status);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
