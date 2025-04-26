package me.weezard12.chessapp.chessGame.scenes.views;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.gameLogic.ContentManager;
import me.weezard12.chessapp.gameLogic.GameLoop;
import me.weezard12.chessapp.gameLogic.myECS.GameScene;

import me.weezard12.chessapp.gameLogic.myECS.components.renderable.SpriteAnimation;
import me.weezard12.chessapp.gameLogic.myECS.components.renderable.SpriteAnimationRenderer;
import me.weezard12.chessapp.gameLogic.myECS.components.renderable.SpriteRenderer;
import me.weezard12.chessapp.gameLogic.myECS.entities.GameEntity;

public class GlitchedKnightView extends GameScene {
    private GameEntity knight;
    public static GlitchedKnightView instance;
    
    public GlitchedKnightView(Context context) {
        super(context);
        init();
    }

    public GlitchedKnightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        GameLoop gameLoop = new GameLoop(this);
        gameLoop.start();
        instance = this;
    }

    @Override
    public void start() {
        super.start();

        setBackgroundColor(Color.TRANSPARENT);

        knight = new GameEntity("Knight Icon");

        // animation
        SpriteAnimationRenderer animationRenderer = new SpriteAnimationRenderer();
        animationRenderer.addAnimation("glitch",new SpriteAnimation(ContentManager.loadSpriteSheetFrames(getContext(),R.drawable.gliched_knight_icon,1,24,24),12, SpriteAnimation.LoopMode.LOOP));
        animationRenderer.setScale(0.4f);

        // static image
        SpriteRenderer spriteRenderer = new SpriteRenderer(BitmapFactory.decodeResource(getResources(),R.drawable.knight_icon));
        spriteRenderer.setScale(0.4f);
        knight.addComponent(spriteRenderer);

        knight.addComponent(animationRenderer);
        animationRenderer.playAnimation("glitch");
        animationRenderer.setEnabled(false);

        knight.setPosition(getSurfaceCenter());
        addEntity(knight);
    }
    public void setThinking(boolean isThinking){
        try {
            knight.getComponent(SpriteRenderer.class).setEnabled(!isThinking);
            knight.getComponent(SpriteAnimationRenderer.class).setEnabled(isThinking);
        }
        catch (Exception ex){

        }

    }
}
