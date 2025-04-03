package me.weezard12.chessapp.gameLogic.myPhysics.shapes;

import android.graphics.PointF;

import com.example.android2dtest.gameLogic.myPhysics.ShapeCollisions;

public class Box extends Shape{
    public float width;
    public float height;

    public Box(float width, float height){
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean collidesWith(Shape other) {
        if(other instanceof Circle)
            return ShapeCollisions.circleToBox((Circle) other,this);
        if(other instanceof Box)
            return ShapeCollisions.boxToBox((Box) other,this);

        return false;
    }

    @Override
    public boolean collidesWithPoint(PointF point) {
    // Calculate half-width and half-height for boundary calculation
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        // Box edges based on the center point and offset
        float left = center.x + offset.x - halfWidth;
        float right = center.x + offset.x + halfWidth;
        float top = center.y + offset.y - halfHeight;
        float bottom = center.y + offset.y + halfHeight;

        // Check if the point is within the box boundaries
        return (point.x >= left && point.x <= right && point.y >= top && point.y <= bottom);
    }
}
