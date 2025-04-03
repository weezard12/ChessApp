package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.android2dtest.gameLogic.myECS.components.GameComponent;

public class RenderableComponent extends GameComponent {
    public PointF getOffset() {
        return offset;
    }

    public void setOffset(float x, float y) {
        this.offset.x = x;
        this.offset.y = y;
    }
    public void setOffset(PointF offset) {
        this.offset = offset;
    }

    protected PointF offset = new PointF();

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint paint = new Paint();
    public RenderableComponent(){
        paint.setColor(Color.WHITE);
    }

    public void render(float delta ,Canvas canvas){

    }
}
