package me.weezard12.chessapp.gameLogic.myECS.components.touchable;

import android.view.MotionEvent;

import me.weezard12.chessapp.gameLogic.myPhysics.Collider;
import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Shape;

public class ClickableComponent extends TouchBase {
    private OnClickListener onClickListener; // Callback for click events
    private final int HOLD_THRESHOLD; // Duration to differentiate between click and hold (in ms)

    private long touchDownTime; // To store the time when touch started
    private boolean isHold; // To flag if the action is considered a hold

    public ClickableComponent(Collider collider) {
        super(collider);
        HOLD_THRESHOLD = 500;
    }
    public ClickableComponent(Collider collider, int holdThreshold) {
        super(collider);
        HOLD_THRESHOLD = holdThreshold;
    }

    public ClickableComponent(Shape collider) {
        super(collider);
        HOLD_THRESHOLD = 500;
    }
    public ClickableComponent(Shape collider, int holdThreshold) {
        super(collider);
        HOLD_THRESHOLD = holdThreshold;
    }

    // This method will be called whenever a touch event happens
    public void onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Detect if the click (touch) was inside the shape
                if (shape.collidesWithPoint(touchX, touchY)) {
                    // Record the time of the touch start
                    touchDownTime = System.currentTimeMillis();
                    isHold = false; // Reset hold flag
                }
                break;

            case MotionEvent.ACTION_UP:
                // Calculate touch duration
                long touchDuration = System.currentTimeMillis() - touchDownTime;

                // Check if the touch was inside the shape and not considered a hold
                if (shape.collidesWithPoint(touchX, touchY) && touchDuration < HOLD_THRESHOLD && !isHold) {
                    if (onClickListener != null) {
                        // Notify that the shape was clicked
                        onClickListener.onClick(touchX, touchY);
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // Optionally, you can detect if the user moved significantly during the touch
                break;

            case MotionEvent.ACTION_CANCEL:
                // Handle touch cancel event
                break;
        }
    }

    // Set the click listener (callback)
    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    // Callback interface for click events
    public interface OnClickListener {
        void onClick(float x, float y);
    }
}
