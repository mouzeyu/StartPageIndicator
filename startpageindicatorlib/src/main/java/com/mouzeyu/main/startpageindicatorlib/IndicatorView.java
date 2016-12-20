package com.mouzeyu.main.startpageindicatorlib;

/**
 * Created by mouzeyu on 16/12/20.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * A startpage indicator which used in ViewPager
 * Created by mouzeyu on 16/11/21.
 */

public class IndicatorView extends View {

    private static final String TAG = "IndicatorView";

    private static final int DEF_NUM = 3;

    private float mMaxWidth;

    private float mMinWidth;

    private float mDivider;

    private int mCurrentPageColor;

    private int mDefColor;

    private int mCurrentPosition;

    private float mPageOffset;

    private float mHeight;

    private Paint mPaint;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        mMaxWidth = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_max_width,29);
        mMinWidth = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_min_width,8);
        mDivider = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_divide,28);
        mCurrentPageColor = typedArray.getColor(R.styleable.IndicatorView_indicator_current_page_color, Color.BLACK);
        mDefColor = typedArray.getColor(R.styleable.IndicatorView_indicator_def_color, Color.GRAY);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setUpWithViewPager(ViewPager viewPager){
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCurrentPosition = position;
                mPageOffset = positionOffset;
                invalidate();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (mMaxWidth + (mMinWidth + mDivider)* (DEF_NUM - 1));
        setMeasuredDimension(width, (int) mMinWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startX = mMaxWidth/2;
        float circleRadius = mHeight / 2;
        float rectLengthMax = mMaxWidth - mHeight;
        for(int i = 0; i < DEF_NUM ; i++){
            if(i == mCurrentPosition){
                int color = getColor(mPageOffset);
                mPaint.setColor(color);
                float length = rectLengthMax *  (1 - mPageOffset);
                float circleX = startX - length/2;
                RectF rectF = new RectF(circleX - circleRadius,0,circleX + circleRadius,mHeight);
                canvas.drawArc(rectF,-270,180,true,mPaint);

                canvas.drawRect(circleX - 1,0,circleX + length + 1, mHeight,mPaint);
                RectF rect2 = new RectF(circleX + length- circleRadius,0,circleX + length + circleRadius,mHeight);
                canvas.drawArc(rect2,-90,180,true,mPaint);
                startX += circleRadius * 2 + mDivider;
            }else if(i == mCurrentPosition + 1){
                int color = getColor(1 - mPageOffset);
                mPaint.setColor(color);
                float length = rectLengthMax * mPageOffset;

                float circleX = startX - length/2;
                RectF rectF = new RectF(circleX - circleRadius,0,circleX + circleRadius,mHeight);
                canvas.drawArc(rectF,-270,180,true,mPaint);

                canvas.drawRect(circleX - 1,0,circleX + length + 1, mHeight,mPaint);
                RectF rect2 = new RectF(circleX + length- circleRadius,0,circleX + length + circleRadius,mHeight);
                canvas.drawArc(rect2,-90,180,true,mPaint);

                startX += circleRadius * 2 + mDivider;
            }else {
                mPaint.setColor(mDefColor);
                canvas.drawCircle(startX,circleRadius,circleRadius,mPaint);
                startX += circleRadius * 2 + mDivider;
            }
        }
    }

    public int getColor(float radio) {
        int redStart = Color.red(mCurrentPageColor);
        int blueStart = Color.blue(mCurrentPageColor);
        int greenStart = Color.green(mCurrentPageColor);
        int redEnd = Color.red(mDefColor);
        int blueEnd = Color.blue(mDefColor);
        int greenEnd = Color.green(mDefColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255,red, greed, blue);
    }
}

