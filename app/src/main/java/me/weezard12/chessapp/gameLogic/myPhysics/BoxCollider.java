package me.weezard12.chessapp.gameLogic.myPhysics;

import com.example.android2dtest.gameLogic.myPhysics.shapes.Box;

import java.util.List;

public class BoxCollider extends Collider {

    public BoxCollider(float width, float height){
        this.collisionShape = new Box(width, height);
    }
    @Override
    public void onCollide(List<Collider> collidesWith) {

    }
}
