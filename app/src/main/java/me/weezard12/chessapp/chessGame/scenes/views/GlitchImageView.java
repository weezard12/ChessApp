package me.weezard12.chessapp.chessGame.scenes.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.Random;

public class GlitchImageView extends AppCompatImageView {

    private Paint paint;
    private Bitmap bitmap;
    private BitmapShader bitmapShader;
    private Matrix shaderMatrix;
    private Random random;
    private boolean glitchEnabled = true;

    public GlitchImageView(Context context) {
        super(context);
        init();
    }

    public GlitchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlitchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shaderMatrix = new Matrix();
        random = new Random();
        setLayerType(LAYER_TYPE_HARDWARE, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null || !glitchEnabled) {
            super.onDraw(canvas);
            return;
        }

        if (bitmap == null || bitmap.getWidth() != getWidth() || bitmap.getHeight() != getHeight()) {
            bitmap = Bitmap.createBitmap(getWidth()*2, getHeight()*2, Bitmap.Config.ARGB_8888);
        }

        Canvas tempCanvas = new Canvas(bitmap);
        super.onDraw(tempCanvas);

        if (bitmapShader == null) {
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }

        applyGlitchEffect();

        paint.setShader(bitmapShader);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    private void applyGlitchEffect() {
        if (bitmap == null) return;

        shaderMatrix.reset();

        for (int i = 0; i < bitmap.getHeight(); i += random.nextInt(15) + 5) {
            int shift = random.nextInt(20) - 10; // Shift pixels randomly left or right
            shaderMatrix.postTranslate(shift, 0);
            bitmapShader.setLocalMatrix(shaderMatrix);
        }
    }

    public void setGlitchEnabled(boolean enabled) {
        glitchEnabled = enabled;
        invalidate();
    }

    public static Bitmap addTransparentBorder(Bitmap original) {
        int width = original.getWidth();
        int height = original.getHeight();

        // New size: doubling the dimensions
        int newWidth = width * 2;
        int newHeight = height * 2;

        // Create a new bitmap with ARGB_8888 format to support transparency
        Bitmap expandedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        // Create a canvas to draw on the new bitmap
        Canvas canvas = new Canvas(expandedBitmap);

        // Calculate the position to center the original bitmap
        int left = (newWidth - width) / 2;
        int top = (newHeight - height) / 2;

        // Draw the original bitmap in the center of the new bitmap
        canvas.drawBitmap(original, left, top, null);

        return expandedBitmap;
    }
}