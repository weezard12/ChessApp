package me.weezard12.chessapp.gameLogic.myPhysics;

import me.weezard12.chessapp.gameLogic.myECS.components.GameComponent;
import me.weezard12.chessapp.gameLogic.myECS.entities.GameEntity;
import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Shape;

import java.util.List;

public abstract class Collider extends GameComponent {
    public Shape getCollisionShape() {
        return collisionShape;
    }

    protected Shape collisionShape;
    public abstract void onCollide(List<Collider> collidesWith);


    public boolean checkCollision(Collider colliderB) {
        return this.collisionShape.collidesWith(colliderB.collisionShape);
    }

    @Override
    public void attachToEntity(GameEntity entity) {
        super.attachToEntity(entity);
        PhysicsSystem.addCollider(this);
        collisionShape.center = entity.getTransform().position;
    }

    @Override
    public void detachFromEntity() {
        PhysicsSystem.removeCollider(this);
        super.detachFromEntity();
    }
}
