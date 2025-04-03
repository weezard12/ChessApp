package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import me.weezard12.chessapp.gameLogic.myECS.entities.GameEntity;
import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Box;

public final class Batch extends GameEntity {


    public Batch(String name) {
        super(name);

    }

    public static Paint getDefaultPaint(){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        return paint;
    }

    public static void drawBox(Canvas canvas, float x, float y, Box box){
        drawBox(canvas,x,y,box.width,box.height, getDefaultPaint());
    }
    public static void drawBox(Canvas canvas, float x, float y, Box box, Color color){
        Paint paint = new Paint();
        paint.setColor(color.toArgb());
        drawBox(canvas,x,y,box.width,box.height, paint);
    }
    public static void drawBox(Canvas canvas, float x, float y, Box box, Paint paint){
        drawBox(canvas,x,y,box.width,box.height, paint);
    }
    public static void drawBox(Canvas canvas, float x, float y, float width, float height, Paint paint){
        canvas.drawRect(x - (width / 2),y + (height/2),x+(width/2),y-(height/2), paint);
    }

    private static final Paint SPRITE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static void drawSprite(Canvas canvas, Sprite sprite, float x, float y, float width, float height) {
        if (sprite == null || sprite.texture == null || canvas == null) return;

        Bitmap texture = sprite.texture;

        // Compute destination rectangle correctly centered at (x, y)
        float halfWidth = width * 0.5f;  // Adjusted scale factor
        float halfHeight = height * 0.5f;

        RectF destinationRect = new RectF(
                x - halfWidth,
                y - halfHeight,
                x + halfWidth,
                y + halfHeight
        );

        // Draw the bitmap on the canvas
        canvas.drawBitmap(texture, null, destinationRect, SPRITE_PAINT);
    }
}
