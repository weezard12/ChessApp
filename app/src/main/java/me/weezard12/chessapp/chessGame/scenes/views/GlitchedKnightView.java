package me.weezard12.chessapp.chessGame.scenes.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.gameLogic.ContentManager;
import me.weezard12.chessapp.gameLogic.GameLoop;
import me.weezard12.chessapp.gameLogic.myECS.GameScene;

import me.weezard12.chessapp.gameLogic.myECS.components.renderable.SpriteAnimation;
import me.weezard12.chessapp.gameLogic.myECS.components.renderable.SpriteAnimationRenderer;
import me.weezard12.chessapp.gameLogic.myECS.entities.GameEntity;

public class GlitchedKnightView extends GameScene {
    public GlitchedKnightView(Context context) {
        super(context);
        GameLoop gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    public GlitchedKnightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        GameLoop gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    @Override
    public void start() {
        super.start();

        setBackgroundColor(Color.TRANSPARENT);

        GameEntity knight = new GameEntity("Knight Icon");
        SpriteAnimationRenderer animationRenderer = new SpriteAnimationRenderer();
        animationRenderer.addAnimation("glitch",new SpriteAnimation(ContentManager.loadSpriteSheetFrames(getContext(),R.drawable.gliched_knight_icon,1,24,24),12, SpriteAnimation.LoopMode.LOOP));
        animationRenderer.setScale(0.4f);

        knight.addComponent(animationRenderer);
        animationRenderer.playAnimation("glitch");

        knight.setPosition(getSurfaceCenter());
        addEntity(knight);

    }
}
