package me.weezard12.chessapp.gameLogic.myPhysics.shapes;

import android.graphics.PointF;

public abstract class Shape {

    public PointF center = new PointF();
    public PointF offset = new PointF();
    public abstract boolean collidesWith(Shape other);

    public abstract boolean collidesWithPoint(PointF point);
    public boolean collidesWithPoint(float x, float y){
        return collidesWithPoint(new PointF(x,y));
    }
}
