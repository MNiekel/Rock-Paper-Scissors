package net.niekel.rockpaperscissors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import java.lang.Math;

public class TouchView extends ImageView
{
    private Bitmap mask;
    private int maskWidth, maskHeight;
    private int viewSize;

    TouchView(Context context)
    {
        super(context);
        init();
    }

    public TouchView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public TouchView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        viewSize = Math.min(w, h);
        int size = MeasureSpec.makeMeasureSpec(viewSize, MeasureSpec.EXACTLY);
        setMeasuredDimension(size, size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            float x = event.getX();
            float y = event.getY();
            int touch = checkTouchPosition(x, y);
            //((ViewActivity) getContext()).debug("" +x+", "+ y);
            ((ViewActivity) getContext()).handleTouch(x, y, touch);
        }
        return true;
    }

    private void init()
    {
        mask = BitmapFactory.decodeResource(getResources(),
                                                R.drawable.rpsls_mask);
        maskWidth = mask.getWidth();
        maskHeight = mask.getHeight();
    }

    private int checkTouchPosition(float x, float y)
    {
        float size = (float) viewSize;
        int maskx = (int) ((x / size) * (float) maskWidth);
        int masky = (int) ((y / size) * (float) maskHeight);
        if ((maskx >= maskWidth) || (masky >= maskHeight)) { return -1; }
        switch (mask.getPixel(maskx, masky))
        {
            case Color.RED: return 0;
            case Color.BLUE: return 3;
            case Color.GREEN: return 4;
            case Color.MAGENTA: return 1;
            case Color.YELLOW: return 2;
        }
        return -1;
    }
}
