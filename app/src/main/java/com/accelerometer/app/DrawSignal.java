package com.accelerometer.app;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by hesanche on 23/03/2014.
 */
public class DrawSignal extends Drawable {
    private SensorData sensorData;

    public DrawSignal(SensorData sensorData)  {
        this.sensorData = sensorData;
    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint;

        // draw lines to split
        Rect rect = this.getBounds();
        int H=rect.height();
        int W=rect.width();
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1.5f);
        canvas.drawLine(0, H/3, W, H/3, paint);
        canvas.drawLine(0, 2*(H/3), W, 2*(H/3), paint);

        // draw X

        if(sensorData.getSize() >2 ) {
            int max_n = sensorData.getMaxSize();
            paint.setColor(Color.RED);
            drawVariable(canvas, sensorData.getX(), max_n, H / 3, W, 0, 0, paint);
            paint.setColor(Color.BLUE);
            drawVariable(canvas, sensorData.getY(), max_n, H / 3, W, 0, H/3, paint);
            paint.setColor(Color.GREEN);
            drawVariable(canvas, sensorData.getZ(), max_n, H / 3, W, 0, 2 * (H / 3), paint);
        }

    }

    private void drawVariable(Canvas canvas, Float[] data, int max_n, int H, int W,int x_init, int y_init, Paint paint){
        int i, xi,yi,xf,yf;
        float min_h, max_h;

        int n = data.length;

        min_h=max_h=data[0];
        for(i=1;i<n;i++){
            if(min_h>data[i])   min_h=data[i];
            if(max_h<data[i])   max_h=data[i];
        }

        xi= 0;
        yi= (int) (H - ((data[0]-min_h)*H)/(max_h-min_h));

        for( i=0;i < n;i++)
        {
            xf= i*W/max_n;
            yf= (int) (H - Math.round(((data[i] - min_h) * H * 1.0) / (max_h - min_h)));
            canvas.drawLine(x_init + xi,y_init + yi,x_init + xf,y_init + yf, paint);

            xi=xf;
            yi=yf;
        }
    }


    public void setData(SensorData sensorData)  {
        this.sensorData = sensorData;
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
