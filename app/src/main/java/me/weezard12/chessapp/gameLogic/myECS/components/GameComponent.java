package me.weezard12.chessapp.gameLogic.myECS.components;

import com.example.android2dtest.gameLogic.myECS.entities.GameEntity;


public abstract class GameComponent {
    public GameEntity entity;
    private boolean enabled = true;
    public void update(float delta) {

    }
    public void attachToEntity(GameEntity entity){
        this.entity = entity;
    }

    public void detachFromEntity() {
        this.entity = null;
    }
    public boolean getEnabled(){
        return enabled;
    }
    public void setEnabled(){
        enabled = true;
    }
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

}
