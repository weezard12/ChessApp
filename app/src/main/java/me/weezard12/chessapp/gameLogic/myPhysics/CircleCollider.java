package me.weezard12.chessapp.gameLogic.myPhysics;

import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Circle;

import java.util.List;

public class CircleCollider extends Collider{

    public CircleCollider(float radius){
        collisionShape = new Circle(radius);
    }
    @Override
    public void onCollide(List<Collider> collidesWith) {

    }
}
