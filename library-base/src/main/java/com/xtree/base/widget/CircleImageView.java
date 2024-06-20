package com.xtree.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class CircleImageView extends AppCompatImageView {

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取ImageView的Bitmap
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();

        if(bitmap != null) {
            // 将Bitmap裁剪为圆形
            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas circleCanvas = new Canvas(circleBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            circleCanvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

            // 将裁剪后的Bitmap绘制到ImageView中
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            circleCanvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.drawBitmap(circleBitmap, 0, 0, null);
        }
    }
}
