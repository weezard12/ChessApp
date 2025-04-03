package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class SpriteRenderer extends RenderableComponent {

    public Sprite getSprite() {
        return sprite;
    }

    private final Sprite sprite;
    private final Paint paint;
    private final RectF destinationRect = new RectF();

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.paint = new Paint();
        paint.setColor(Color.WHITE);

        paint.setAntiAlias(false); // Ensure no anti-aliasing for sharp pixels
        paint.setFilterBitmap(false); // Disable bilinear filtering for sharper images
        paint.setDither(false); // Disable dithering for sharp pixel edges

    }
    public SpriteRenderer(Bitmap texture) {
        this.sprite = new Sprite(texture);
        this.paint = new Paint();
        paint.setColor(Color.WHITE);

        paint.setAntiAlias(false); // Ensure no anti-aliasing for sharp pixels
        paint.setFilterBitmap(false); // Disable bilinear filtering for sharper images
        paint.setDither(false); // Disable dithering for sharp pixel edges
    }

    @Override
    public void render(float delta, Canvas canvas) {
        super.render(delta, canvas);

        float entityX = entity.getTransform().position.x;
        float entityY = entity.getTransform().position.y;

        Bitmap texture = sprite.texture;

        // Handle scaling: if sprite has scale set, apply it, otherwise default to 1.0
        float scaleX = sprite.getScale().x + entity.getTransform().scale -1;
        float scaleY = sprite.getScale().y + entity.getTransform().scale -1;

        // Calculate the source and destination rectangles
        RectF destinationRect = new RectF(
                entityX - (texture.getWidth() * scaleX / 2),
                entityY - (texture.getHeight() * scaleY / 2),
                entityX + (texture.getWidth() * scaleX / 2),
                entityY + (texture.getHeight() * scaleY / 2)
        );
        // Draw the bitmap on the canvas, applying the scaling and positioning
        canvas.drawBitmap(texture, null, destinationRect, paint);
    }
}
