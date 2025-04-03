package me.weezard12.chessapp.gameLogic.math;

import android.graphics.PointF;

public class Transform {
    public PointF position;
    public float getX(){return position.x;}
    public float getY(){return position.y;}
    public float rotation;
    public float scale;
    public Transform(){
        position = new PointF();
        rotation = 0;
        scale = 1;
    }
}
