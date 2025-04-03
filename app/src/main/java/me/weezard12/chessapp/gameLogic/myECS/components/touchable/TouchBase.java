package me.weezard12.chessapp.gameLogic.myECS.components.touchable;

import android.view.MotionEvent;

import me.weezard12.chessapp.gameLogic.myECS.components.GameComponent;
import me.weezard12.chessapp.gameLogic.myECS.entities.GameEntity;
import me.weezard12.chessapp.gameLogic.myPhysics.Collider;
import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Shape;

public abstract class TouchBase extends GameComponent {
    protected final Shape shape;
    public TouchBase(Collider collider) {
        this.shape = collider.getCollisionShape();
    }
    public TouchBase(Shape collider) {
        this.shape = collider;
    }
    public abstract void onTouchEvent(MotionEvent event);

    @Override
    public void attachToEntity(GameEntity entity) {
        super.attachToEntity(entity);
        entity.scene.addTouchable(this);
    }

    @Override
    public void detachFromEntity() {
        entity.scene.removeTouchable(this);
        super.detachFromEntity();
    }
}
