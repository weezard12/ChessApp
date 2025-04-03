package me.weezard12.chessapp.chessGame.scenes.base;

import android.content.Context;
import android.util.AttributeSet;

public class ChessHotSit extends ChessSceneBase{
    /**
     * WARNING when inheriting the class do not use the constructor scene init logic!
     * override the start method so you cant get null reference when creating entities at the scene start.
     *
     * @param context
     */
    public ChessHotSit(Context context) {
        super(context);
        gameBoard.isUpdatingInput = true;
    }
    public ChessHotSit(Context context, AttributeSet attrs) {
        this(context);
    }
}
