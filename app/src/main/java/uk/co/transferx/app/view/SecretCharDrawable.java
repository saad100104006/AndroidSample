package uk.co.transferx.app.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by sergey on 19.11.17.
 */

public class SecretCharDrawable extends Drawable {

    private static final float RADIUS = 0.18f;

    private final Paint paint;

    public SecretCharDrawable(int color) {
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), getBounds().width() * RADIUS, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }
}
