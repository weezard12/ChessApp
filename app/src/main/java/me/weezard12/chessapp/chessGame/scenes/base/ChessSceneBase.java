package me.weezard12.chessapp.chessGame.scenes.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import com.example.android2dtest.R;
import com.example.android2dtest.gameLogic.GameLoop;
import com.example.android2dtest.gameLogic.MyDebug;
import com.example.android2dtest.gameLogic.myECS.GameScene;
import com.example.android2dtest.gameLogic.myECS.components.renderable.Sprite;
import com.example.android2dtest.gameLogic.myECS.entities.GameEntity;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.ai.Shtokfish;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.board.GameBoard;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.scenes.views.EvalBar;

import java.util.HashMap;

public class ChessSceneBase extends GameScene {
    public static int tileSize = 128;
    public static int boardSize = 1024;

    public GameBoard gameBoard;

    public static HashMap<String, Sprite> piecesTextures;

    public ChessSceneBase(Context context) {
        super(context);

        setTransparentBackground();

        if(piecesTextures == null){
            loadPiecesTextures(context);
        }

        //debugRenderPhysics = true;

        gameBoard = new GameBoard();
        Shtokfish.init(gameBoard);



        GameBoard.setBoardByString(gameBoard.board,
            "Br,Bk,Bb,Bq,BK,Bb,Bk,Br,"+
                "Bp,Bp,Bp,Bp,Bp,Bp,Bp,Bp,"+
                "e,e,e,e,e,e,e,e,"+
                "e,e,e,e,e,e,e,e,"+
                "e,e,e,e,e,e,e,e,"+
                "e,e,e,e,e,e,e,e,"+
                "p,p,p,p,p,p,p,p,"+
                "r,k,b,q,K,b,k,r,"
        );

        // starts the game loop
        GameLoop gameLoop = new GameLoop(this);
        gameLoop.start();


        GameEntity boardEntity = new GameEntity("Board");
        boardEntity.addComponent(gameBoard);

        addEntity(boardEntity);

        EvalBar evalBar = new EvalBar("Eval Bar");
        addEntity(evalBar);
    }

    public ChessSceneBase(Context context, AttributeSet attrs) {
        this(context);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected void handleClick(float x, float y) {
        super.handleClick(x, y);
        gameBoard.checkForInput(x,y);
    }

    private void loadPiecesTextures(Context context) {
        piecesTextures = new HashMap<>();

        // Manually maintain a list of resource IDs
        int[] textureIds = {
                R.raw.bishop0,
                R.raw.bishop1,
                R.raw.king0,
                R.raw.king1,
                R.raw.knight0,
                R.raw.knight1,
                R.raw.pawn0,
                R.raw.pawn1,
                R.raw.queen0,
                R.raw.queen1,
                R.raw.rook0,
                R.raw.rook1,
        };

        // Load each texture and store in HashMap
        for (int resId : textureIds) {
            String resourceName = context.getResources().getResourceEntryName(resId);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            Sprite sprite = new Sprite(bitmap);
            piecesTextures.put(resourceName, sprite);
            MyDebug.log(resourceName);
        }
    }
}
