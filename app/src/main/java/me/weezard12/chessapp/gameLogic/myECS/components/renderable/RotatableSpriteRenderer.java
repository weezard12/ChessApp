package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

public class RotatableSpriteRenderer extends SpriteRenderer {

    private final Matrix matrix = new Matrix(); // Matrix for rotation

    public RotatableSpriteRenderer(Sprite sprite) {
        super(sprite);
    }

    public RotatableSpriteRenderer(Bitmap texture) {
        super(texture);
    }

    @Override
    public void render(float delta, Canvas canvas) {
        // Clear the matrix
        matrix.reset();

        // Get the entity's transform values
        float entityX = entity.getTransform().position.x + offset.x;
        float entityY = entity.getTransform().position.y + offset.y;
        float entityAngle = entity.getTransform().rotation;

        Bitmap texture = getSprite().texture;

        // Handle scaling: if sprite has scale set, apply it
        float scaleX = getSprite().getScale().x + entity.getTransform().scale -1;
        float scaleY = getSprite().getScale().y + entity.getTransform().scale -1;

        // Set up the source rectangle and destination rectangle for scaling
        RectF destinationRect = new RectF(
                entityX - (texture.getWidth() * scaleX / 2),
                entityY - (texture.getHeight() * scaleY / 2),
                entityX + (texture.getWidth() * scaleX / 2),
                entityY + (texture.getHeight() * scaleY / 2)
        );

        // Apply translation, rotation, and scaling to the matrix
        matrix.postTranslate(-texture.getWidth() / 2f, -texture.getHeight() / 2f); // Move to center for rotation
        matrix.postRotate(entityAngle); // Apply rotation
        matrix.postScale(scaleX, scaleY); // Apply scaling
        matrix.postTranslate(entityX, entityY); // Move back to the entity's position

        // Draw the bitmap using the matrix for rotation and scaling
        canvas.drawBitmap(texture, matrix, getPaint());
    }
}