package me.weezard12.chessapp.gameLogic.myECS.components.renderable;

import android.graphics.Canvas;
import android.graphics.Paint;

public class TextRenderer extends RenderableComponent {
    public String getDrawText() {
        return drawText;
    }

    public void setDrawText(String drawText) {
        this.drawText = drawText;
    }

    String drawText;
    public TextRenderer(String drawText){
        super();
        this.drawText = drawText;
        paint.setTextSize(100);        // Default text size
        paint.setTextAlign(Paint.Align.CENTER); // Text alignment
    }

    @Override
    public void render(float delta, Canvas canvas) {
        super.render(delta, canvas);
        canvas.drawText(drawText,entity.getX() + offset.x,entity.getY() + offset.y,paint);
    }
}
