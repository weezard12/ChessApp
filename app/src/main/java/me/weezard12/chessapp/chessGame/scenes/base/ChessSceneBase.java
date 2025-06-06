package me.weezard12.chessapp.chessGame.scenes.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.gameLogic.GameLoop;
import me.weezard12.chessapp.gameLogic.MyDebug;
import me.weezard12.chessapp.gameLogic.myECS.GameScene;
import me.weezard12.chessapp.gameLogic.myECS.components.renderable.Sprite;
import me.weezard12.chessapp.gameLogic.myECS.entities.GameEntity;
import me.weezard12.chessapp.chessGame.ai.Shtokfish;
import me.weezard12.chessapp.chessGame.board.GameBoard;
import me.weezard12.chessapp.chessGame.scenes.views.EvalBar;

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
                R.drawable.bishop0,
                R.drawable.bishop1,
                R.drawable.king0,
                R.drawable.king1,
                R.drawable.knight0,
                R.drawable.knight1,
                R.drawable.pawn0,
                R.drawable.pawn1,
                R.drawable.queen0,
                R.drawable.queen1,
                R.drawable.rook0,
                R.drawable.rook1,
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
