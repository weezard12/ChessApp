package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.util.HashMap;
import java.util.Map;

public class SpriteAnimationRenderer extends RenderableComponent {
    private final Map<String, SpriteAnimation> animations;
    private String currentAnimation;
    private final Matrix matrix = new Matrix();
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;

    /**
     * Creates a new SpriteAnimationRenderer without any animations
     */
    public SpriteAnimationRenderer() {
        super();
        animations = new HashMap<>();
        currentAnimation = null;
    }

    /**
     * Creates a new SpriteAnimationRenderer with a default animation
     * @param name The name to assign to the animation
     * @param animation The default animation
     */
    public SpriteAnimationRenderer(String name, SpriteAnimation animation) {
        super();
        animations = new HashMap<>();
        addAnimation(name, animation);
        currentAnimation = name;
    }

    /**
     * Adds a new animation to this renderer
     * @param name Identifier for this animation
     * @param animation The animation to add
     */
    public void addAnimation(String name, SpriteAnimation animation) {
        animations.put(name, animation);
        // If this is the first animation, set it as current
        if (currentAnimation == null) {
            currentAnimation = name;
        }
    }

    /**
     * Changes to a different animation
     * @param name The name of the animation to play
     * @param resetIfSame Whether to restart the animation if it's already playing
     * @return true if the animation was found and set, false otherwise
     */
    public boolean playAnimation(String name, boolean resetIfSame) {
        if (!animations.containsKey(name)) {
            return false;
        }

        if (resetIfSame || !name.equals(currentAnimation)) {
            if (currentAnimation != null) {
                SpriteAnimation current = animations.get(currentAnimation);
                if (current != null) {
                    current.stop();
                }
            }

            currentAnimation = name;
            SpriteAnimation anim = animations.get(name);
            if (anim != null) {
                anim.play();
            }
        }

        return true;
    }

    /**
     * Plays an animation from the beginning
     * @param name The name of the animation to play
     * @return true if the animation was found and set, false otherwise
     */
    public boolean playAnimation(String name) {
        return playAnimation(name, true);
    }

    /**
     * Stops the current animation
     */
    public void stopAnimation() {
        SpriteAnimation current = getCurrentAnimation();
        if (current != null) {
            current.stop();
        }
    }

    /**
     * Pauses the current animation
     */
    public void pauseAnimation() {
        SpriteAnimation current = getCurrentAnimation();
        if (current != null) {
            current.pause();
        }
    }

    /**
     * Resumes the current animation
     */
    public void resumeAnimation() {
        SpriteAnimation current = getCurrentAnimation();
        if (current != null) {
            current.play();
        }
    }

    /**
     * @return The currently active animation or null if none
     */
    public SpriteAnimation getCurrentAnimation() {
        if (currentAnimation == null) {
            return null;
        }
        return animations.get(currentAnimation);
    }

    /**
     * Gets a named animation
     * @param name The animation identifier
     * @return The animation or null if not found
     */
    public SpriteAnimation getAnimation(String name) {
        return animations.get(name);
    }

    /**
     * Sets the scale for rendering
     * @param scaleX X-axis scale
     * @param scaleY Y-axis scale
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * Sets uniform scale for rendering
     * @param scale Scale factor for both axes
     */
    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
    }

    @Override
    public void render(float delta, Canvas canvas) {
        SpriteAnimation animation = getCurrentAnimation();
        if (animation == null) {
            return;
        }

        // Update animation and get current frame
        Bitmap texture = animation.update(delta);
        if (texture == null) {
            return;
        }

        // Clear the matrix
        matrix.reset();

        // Get the entity's transform values
        float entityX = entity.getTransform().position.x + offset.x;
        float entityY = entity.getTransform().position.y + offset.y;
        float entityAngle = entity.getTransform().rotation;

        // Calculate final scale including entity transform scale
        float finalScaleX = scaleX + entity.getTransform().scale - 1;
        float finalScaleY = scaleY + entity.getTransform().scale - 1;

        // Set up the destination rectangle for scaling
        RectF destinationRect = new RectF(
                entityX - (texture.getWidth() * finalScaleX / 2),
                entityY - (texture.getHeight() * finalScaleY / 2),
                entityX + (texture.getWidth() * finalScaleX / 2),
                entityY + (texture.getHeight() * finalScaleY / 2)
        );

        // Apply transformations to matrix
        matrix.postTranslate(-texture.getWidth() / 2f, -texture.getHeight() / 2f); // Move to center for rotation
        matrix.postRotate(entityAngle); // Apply rotation
        matrix.postScale(finalScaleX, finalScaleY); // Apply scaling
        matrix.postTranslate(entityX, entityY); // Move back to the entity's position

        // Draw the bitmap using the matrix for rotation and scaling
        canvas.drawBitmap(texture, matrix, getPaint());
    }

    /**
     * @return The name of the current animation
     */
    public String getCurrentAnimationName() {
        return currentAnimation;
    }

    /**
     * @return True if an animation is currently playing
     */
    public boolean isAnimationPlaying() {
        SpriteAnimation current = getCurrentAnimation();
        return current != null && current.isPlaying();
    }
}