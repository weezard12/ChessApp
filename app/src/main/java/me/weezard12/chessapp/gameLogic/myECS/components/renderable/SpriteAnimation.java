package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Bitmap;

public class SpriteAnimation {
    public enum LoopMode {
        LOOP,       // Animation loops continuously
        ONCE,       // Animation plays once and stops on last frame
        PING_PONG   // Animation plays forward, then backward
    }

    private Bitmap[] frames;          // Array of animation frames
    private float fps;                // Frames per second
    private float frameTime;          // Time per frame (1/fps)
    private LoopMode loopMode;        // Animation loop behavior
    private boolean isPlaying;        // Whether animation is currently playing
    private boolean isReversed;       // For ping-pong mode
    private float currentTime;        // Time accumulated in current frame
    private int currentFrameIndex;    // Current frame being displayed

    /**
     * Creates a new sprite animation
     * @param frames Array of bitmap frames
     * @param fps Frames per second
     * @param loopMode How the animation should loop
     */
    public SpriteAnimation(Bitmap[] frames, float fps, LoopMode loopMode) {
        this.frames = frames;
        this.fps = fps;
        this.frameTime = 1.0f / fps;
        this.loopMode = loopMode;
        this.isPlaying = false;
        this.isReversed = false;
        this.currentTime = 0;
        this.currentFrameIndex = 0;
    }

    /**
     * Updates the animation state based on elapsed time
     * @param deltaTime Time elapsed since last update in seconds
     * @return The current frame bitmap
     */
    public Bitmap update(float deltaTime) {
        if (!isPlaying || frames.length == 0) {
            return getCurrentFrame();
        }

        currentTime += deltaTime;

        // Time to advance frame
        if (currentTime >= frameTime) {
            currentTime -= frameTime;

            // Handle frame advancement based on loop mode
            if (!isReversed) {
                currentFrameIndex++;

                if (currentFrameIndex >= frames.length) {
                    switch (loopMode) {
                        case LOOP:
                            currentFrameIndex = 0;
                            break;
                        case ONCE:
                            currentFrameIndex = frames.length - 1;
                            isPlaying = false;
                            break;
                        case PING_PONG:
                            if (frames.length > 1) {
                                currentFrameIndex = frames.length - 2;
                                isReversed = true;
                            } else {
                                currentFrameIndex = 0;
                            }
                            break;
                    }
                }
            } else {
                // When in reversed mode (for ping-pong)
                currentFrameIndex--;

                if (currentFrameIndex < 0) {
                    currentFrameIndex = 1;
                    isReversed = false;
                }
            }
        }

        return getCurrentFrame();
    }

    /**
     * Gets the current frame without advancing the animation
     * @return The current frame bitmap
     */
    public Bitmap getCurrentFrame() {
        if (frames.length == 0) {
            return null;
        }

        return frames[currentFrameIndex];
    }

    /**
     * Starts playing the animation
     */
    public void play() {
        isPlaying = true;
    }

    /**
     * Pauses the animation at the current frame
     */
    public void pause() {
        isPlaying = false;
    }

    /**
     * Stops the animation and resets to the first frame
     */
    public void stop() {
        isPlaying = false;
        currentFrameIndex = 0;
        currentTime = 0;
        isReversed = false;
    }

    /**
     * Sets the animation to a specific frame
     * @param frameIndex The frame index to set
     */
    public void setFrame(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < frames.length) {
            currentFrameIndex = frameIndex;
            currentTime = 0;
        }
    }

    /**
     * Changes the frame rate of the animation
     * @param fps New frames per second
     */
    public void setFPS(float fps) {
        this.fps = fps;
        this.frameTime = 1.0f / fps;
    }

    /**
     * @return The number of frames in the animation
     */
    public int getFrameCount() {
        return frames.length;
    }

    /**
     * @return Whether the animation is currently playing
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * @return The current loop mode
     */
    public LoopMode getLoopMode() {
        return loopMode;
    }

    /**
     * @param loopMode The new loop mode
     */
    public void setLoopMode(LoopMode loopMode) {
        this.loopMode = loopMode;
    }
}