package com.redbooth.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RectangleWithCapCircleView extends View {
    private Paint paint;
    private RectF rectCap;
    private RectF rectBottom;

    public RectangleWithCapCircleView(Context context) {
        super(context);
    }

    public RectangleWithCapCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleWithCapCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rectCap = null;
        super.onMeasure(widthMeasureSpec, (int) Math.ceil(heightMeasureSpec + widthMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectCap == null) {
            rectCap = new RectF(0f, 0f, getWidth(), getWidth() + 1);
            rectBottom = new RectF(0.0f, (getWidth()/2) - 1, getWidth(), getHeight());
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
        }
        canvas.drawRect(rectBottom, paint);
        canvas.drawArc(rectCap, 0f, -180f, true, paint);
    }
}
