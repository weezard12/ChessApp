package me.weezard12.chessapp.gameLogic.myECS;

import static com.example.android2dtest.gameLogic.MyDebug.log;

import android.graphics.PointF;

import com.example.android2dtest.gameLogic.myECS.entities.GameEntity;
import com.example.android2dtest.gameLogic.myPhysics.BoxCollider;
import com.example.android2dtest.gameLogic.myPhysics.Collider;
import com.example.android2dtest.gameLogic.myPhysics.shapes.Box;

import java.util.ArrayList;

public class Panel {
    ArrayList<GameEntity> entities;
    PointF startPos;

    public Panel(float x, float y){
        this(new PointF(x,y));
    }
    public Panel(PointF startPos){
        this.startPos = startPos;
        this.entities = new ArrayList<>();
    }
    public void add(GameEntity entity){
        if(entities.contains(entity))
            return;
        entities.add(entity);
        arrangeEntitiesVertically();
    }
    public void remove(GameEntity entity){
        if(!entities.contains(entity))
            return;
        entities.remove(entity);
        arrangeEntitiesVertically();
    }
    private void arrangeEntitiesVertically(){
        float currentY = startPos.y;
        for (GameEntity entity : entities) {
            if (entity != null) {
                Collider collider = entity.getComponent(BoxCollider.class);
                if (collider != null) {
                    currentY += (((Box) collider.getCollisionShape()).height) / 2;
                    log(""+((Box) collider.getCollisionShape()).height);
                }
                entity.setPosition(startPos.x, currentY);
                if (collider != null) {
                    currentY += (((Box) collider.getCollisionShape()).height) / 2;
                }
            }
        }
    }
}