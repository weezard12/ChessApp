package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class SpriteRenderer extends RenderableComponent {

    private final Sprite sprite;
    private final Paint paint;
    private final RectF destinationRect = new RectF();
    private float localScale = 1.0f; // New property for local scale

    public Sprite getSprite() {
        return sprite;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.paint = new Paint();
        setupPaint();
    }

    public SpriteRenderer(Bitmap texture) {
        this.sprite = new Sprite(texture);
        this.paint = new Paint();
        setupPaint();
    }

    private void setupPaint() {
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(false); // Ensure no anti-aliasing for sharp pixels
        paint.setFilterBitmap(false); // Disable bilinear filtering for sharper images
        paint.setDither(false); // Disable dithering for sharp pixel edges
    }

    // New setter for local scale
    public void setScale(float scale) {
        this.localScale = scale;
    }

    public float getScale() {
        return localScale;
    }

    @Override
    public void render(float delta, Canvas canvas) {
        super.render(delta, canvas);

        float entityX = entity.getTransform().position.x;
        float entityY = entity.getTransform().position.y;

        Bitmap texture = sprite.texture;

        // Combine entity's transform scale, sprite's internal scale, and local scale
        float scaleX = (sprite.getScale().x + entity.getTransform().scale - 1) * localScale;
        float scaleY = (sprite.getScale().y + entity.getTransform().scale - 1) * localScale;

        destinationRect.set(
                entityX - (texture.getWidth() * scaleX / 2),
                entityY - (texture.getHeight() * scaleY / 2),
                entityX + (texture.getWidth() * scaleX / 2),
                entityY + (texture.getHeight() * scaleY / 2)
        );

        canvas.drawBitmap(texture, null, destinationRect, paint);
    }
}
