package me.weezard12.chessapp.gameLogic.myECS.components.touchable;

import android.view.MotionEvent;

import me.weezard12.chessapp.gameLogic.myPhysics.Collider;
import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Shape;

public class DraggableComponent extends TouchBase {
    private boolean isDragging = false;    // To check if we are currently dragging
    private float lastTouchX;              // To track the last X touch position
    private float lastTouchY;

    private OnStopDraggingListener onStopDraggingListener;// To track the last Y touch position
    private ClickableComponent.OnClickListener onStartDraggingListener;// To track the last Y touch position

    public DraggableComponent(Collider collider) {
        super(collider);
    }
    public DraggableComponent(Shape collider) {
        super(collider);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Handle dragging logic here (based on input)
        if (isDragging) {
            // Move the entity according to input delta
            // Assuming that entity has a position that can be updated
            entity.setPosition(entity.getX() + getTouchDeltaX(),entity.getY() + getTouchDeltaY());

            // Update the last touch coordinates
        }
    }

    // This method will be called whenever a touch event happens
    public void onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // If the touch is inside the shape's boundary, start dragging
                if (shape.collidesWithPoint(touchX, touchY)) {
                    isDragging = true;
                    lastTouchX = touchX;
                    lastTouchY = touchY;

                    if(onStartDraggingListener!=null)
                        onStartDraggingListener.onClick(touchX,touchY);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // If already dragging, update the touch position
                if (isDragging) {
                    float deltaX = touchX - lastTouchX;
                    float deltaY = touchY - lastTouchY;

                    // Update entity position
                    entity.setPosition(entity.getX()+deltaX,entity.getY()+deltaY);

                    // Update last touch coordinates
                    lastTouchX = touchX;
                    lastTouchY = touchY;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(!isDragging)
                    return;
                // Stop dragging when touch is released or cancelled
                isDragging = false;

                if (onStopDraggingListener != null) {
                    onStopDraggingListener.onStopDragging();
                }
                break;
        }
    }

    // Helper functions to get the current touch position (depends on platform)
    private float getCurrentTouchX() {
        // This method should return the current X coordinate of the touch
        // You can adapt it to your input system
        return lastTouchX;
    }

    private float getCurrentTouchY() {
        // This method should return the current Y coordinate of the touch
        // You can adapt it to your input system
        return lastTouchY;
    }

    private float getTouchDeltaX() {
        // Calculate how much the X position has changed
        return getCurrentTouchX() - lastTouchX;
    }

    private float getTouchDeltaY() {
        // Calculate how much the Y position has changed
        return getCurrentTouchY() - lastTouchY;
    }

    public void setOnStopDraggingListener(OnStopDraggingListener listener) {
        this.onStopDraggingListener = listener;
    }
    public void setOnStartDraggingListener(ClickableComponent.OnClickListener listener) {
        this.onStartDraggingListener = listener;
    }
    public interface OnStopDraggingListener{
        public void onStopDragging();
    }
}
