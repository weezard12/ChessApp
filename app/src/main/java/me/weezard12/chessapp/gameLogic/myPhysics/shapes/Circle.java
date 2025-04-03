package me.weezard12.chessapp.gameLogic.myPhysics.shapes;

import static com.example.android2dtest.main.MyUtils.distance;

import android.graphics.PointF;

import com.example.android2dtest.gameLogic.myPhysics.ShapeCollisions;

public class Circle extends Shape{
    public float radius;
    public Circle(float radius){
        this.radius = radius;
    }

    @Override
    public boolean collidesWith(Shape other) {
        if (other instanceof Circle)
            return ShapeCollisions.circleToCircle(this,(Circle) other);
        if (other instanceof Box)
            return ShapeCollisions.circleToBox(this,(Box) other);

        return false;
    }

    @Override
    public boolean collidesWithPoint(PointF point) {
        return radius > distance(center.x + offset.x,center.y + offset.y,point.x, point.y);
    }
}
