package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Sprite {
    public Bitmap getTexture() {
        return texture;
    }

    public void setTexture(Bitmap texture) {
        this.texture = texture;
    }

    Bitmap texture;

    public PointF getScale() {
        return scale;
    }

    public void setScale(PointF scale) {
        this.scale = scale;
    }
    public void setScale(float scaleX, float scaleY) {
        this.scale = new PointF(scaleX, scaleY);
    }

    private PointF scale;
    public Sprite(Bitmap texture){
        this.texture = texture;
        this.scale = new PointF(1,1);
    }
    public Sprite(Bitmap texture, float scaleX, float scaleY){
        this.texture = texture;
        this.scale = new PointF(scaleX,scaleY);
    }


}
