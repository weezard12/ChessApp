package me.weezard12.chessapp.chessGame.scenes.views;

import android.graphics.Color;

import com.example.android2dtest.gameLogic.myECS.GameScene;
import com.example.android2dtest.gameLogic.myECS.components.renderable.BoxRenderer;
import com.example.android2dtest.gameLogic.myECS.entities.GameEntity;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.ai.Shtokfish;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.board.GameBoard;

public class EvalBar extends GameEntity {

    BoxRenderer dynamicEval;
    BoxRenderer staticEval;

    public EvalBar(String name) {
        super(name);
    }

    @Override
    public void attachToScene(GameScene scene) {
        super.attachToScene(scene);

        staticEval = new BoxRenderer(GameBoard.offsetToRight,scene.getHeight());
        staticEval.paint.setColor(Color.WHITE);
        addComponent(staticEval);

        dynamicEval = new BoxRenderer(GameBoard.offsetToRight,0);
        dynamicEval.paint.setColor(Color.BLACK);
        addComponent(dynamicEval);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        dynamicEval.height = Shtokfish.currentBoardEval.getWhiteEvalAsPresent()*scene.getHeight();
        //setPosition(GameBoard.offsetToRight/2,Shtokfish.currentBoardEval.getWhiteEvalAsPresent()*scene.getHeight()/2);
        dynamicEval.setOffset(0, (scene.getHeight()/2) + Shtokfish.currentBoardEval.getWhiteEvalAsPresent() * scene.getHeight()/-2);
        setPosition(GameBoard.offsetToRight/2,scene.getHeight()/2);

        staticEval.height = scene.getHeight();
    }
}
