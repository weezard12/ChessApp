package me.weezard12.chessapp.chessGame.scenes.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.android2dtest.R;

import java.util.Random;

public class PatternView extends View {
    private Paint paintCurrent, paintNext;
    private BitmapShader shaderCurrent, shaderNext;
    private Matrix matrix = new Matrix();
    private float xOffset = 0;
    private float yOffset = 0;
    private float scrollSpeedX = 0.2f;
    private float scrollSpeedY = 0.2f;


    private Bitmap[] bitmaps;
    private Random random = new Random();
    private int currentImageIndex = 0;
    private int nextImageIndex = 0;
    private long lastChangeTime = 0;
    private long transitionStartTime = 0;
    private static final long IMAGE_CHANGE_INTERVAL = 5000; // 5 seconds
    private static final long TRANSITION_DURATION = 2000;  // 2 seconds fade effect

    private float transitionProgress = 1.0f; // 1.0 = fully current image, 0.0 = fully next image

    public PatternView(Context context) {
        super(context);
        init();
    }

    public PatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PatternView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Load multiple images into an array
        bitmaps = new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.raw.pawn0),
                BitmapFactory.decodeResource(getResources(), R.raw.pawn1),
                BitmapFactory.decodeResource(getResources(), R.raw.bishop0),
                BitmapFactory.decodeResource(getResources(), R.raw.bishop1),
                BitmapFactory.decodeResource(getResources(), R.raw.knight0),
                BitmapFactory.decodeResource(getResources(), R.raw.knight1),
                BitmapFactory.decodeResource(getResources(), R.raw.queen0),
                BitmapFactory.decodeResource(getResources(), R.raw.queen1),
                BitmapFactory.decodeResource(getResources(), R.raw.king0),
                BitmapFactory.decodeResource(getResources(), R.raw.king1),
                BitmapFactory.decodeResource(getResources(), R.raw.rook0),
                BitmapFactory.decodeResource(getResources(), R.raw.rook1)
        };

        // Randomly select the first image
        currentImageIndex = random.nextInt(bitmaps.length);
        setShaders(currentImageIndex);

        // Start the animation loop
        postInvalidateOnAnimation();
    }

    private void setShaders(int imageIndex) {
        if (bitmaps[imageIndex] != null) {
            shaderCurrent = new BitmapShader(bitmaps[imageIndex], Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            paintCurrent = new Paint();
            paintCurrent.setShader(shaderCurrent);
        }
    }

    private void setNextShader(int imageIndex) {
        if (bitmaps[imageIndex] != null) {
            shaderNext = new BitmapShader(bitmaps[imageIndex], Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            paintNext = new Paint();
            paintNext.setShader(shaderNext);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long currentTime = System.currentTimeMillis();

        // Check if it's time to start transitioning to a new image
        if (currentTime - lastChangeTime >= IMAGE_CHANGE_INTERVAL && transitionProgress >= 1.0f) {
            lastChangeTime = currentTime;
            transitionStartTime = currentTime;
            nextImageIndex = random.nextInt(bitmaps.length);
            setNextShader(nextImageIndex);
            transitionProgress = 0.0f; // Start transition
        }

        // Smoothly blend images during transition
        if (transitionProgress < 1.0f) {
            float elapsedTime = currentTime - transitionStartTime;
            transitionProgress = Math.min(elapsedTime / TRANSITION_DURATION, 1.0f);
        }

        // Apply scrolling effect
        xOffset += scrollSpeedX;
        yOffset += scrollSpeedY;
        matrix.setTranslate(xOffset, yOffset);

        if (shaderCurrent != null) shaderCurrent.setLocalMatrix(matrix);
        if (shaderNext != null) shaderNext.setLocalMatrix(matrix);

        // Draw the current image with fading effect
        if (shaderCurrent != null) {
            paintCurrent.setAlpha((int) ((1.0f - transitionProgress) * 255)); // Fade out
            canvas.drawRect(0, 0, getWidth(), getHeight(), paintCurrent);
        }

        // Draw the next image with increasing opacity
        if (shaderNext != null) {
            paintNext.setAlpha((int) (transitionProgress * 255)); // Fade in
            canvas.drawRect(0, 0, getWidth(), getHeight(), paintNext);
        }

        // When transition completes, set next image as current
        if (transitionProgress >= 1.0f) {
            currentImageIndex = nextImageIndex;
            setShaders(currentImageIndex);
        }

        // Keep updating for smooth animation
        postInvalidateOnAnimation();
    }
}
