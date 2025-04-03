package me.weezard12.chessapp.gameLogic.myPhysics.shapes;

import static me.weezard12.chessapp.gameLogic.math.MathUtils.distance;


import android.graphics.PointF;

import me.weezard12.chessapp.gameLogic.myPhysics.ShapeCollisions;

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
