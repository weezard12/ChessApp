package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleRenderer extends RenderableComponent {

    public int radius;

    public CircleRenderer(int radius){
        this.radius = radius;
    }
    public CircleRenderer(int radius, Paint color){
        this.radius = radius;
        this.paint = color;
    }


    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(float delta, Canvas canvas) {
        super.render(delta, canvas);
        canvas.drawCircle(entity.getTransform().getX(), entity.getTransform().getY(),radius,paint);
    }
}
