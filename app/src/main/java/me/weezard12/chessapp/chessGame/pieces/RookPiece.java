package me.weezard12.chessapp.chessGame.pieces;

import android.graphics.Point;

import me.weezard12.chessapp.chessGame.board.GameBoard;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.BasePiece;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.PieceType;

import java.util.ArrayList;

public class RookPiece extends BasePiece {
    public boolean isEverMoved = false;
    private final Point point1 = new Point(-1,-1);
    private final Point point2 = new Point(-1,-1);

    public RookPiece(boolean isEnemy, BasePiece[][] board) {
        super(isEnemy, board);
        type = PieceType.ROOK;
    }

    @Override
    public void getAllPossibleMoves(int pX, int pY, ArrayList<BasePiece[][]> r) {

        movePieceInRow(pX,pY,isEnemy,1,0,GameBoard.cloneBoard(board),r);
        movePieceInRow(pX,pY,isEnemy,-1,0,GameBoard.cloneBoard(board),r);
        movePieceInRow(pX,pY,isEnemy,0,1,GameBoard.cloneBoard(board),r);
        movePieceInRow(pX,pY,isEnemy,0,-1,GameBoard.cloneBoard(board),r);


        //Tile.setTileHighlight(r,this, GameBoard.tiles);

    }


    @Override
    public boolean doesCheck(int mX,int mY,int kX,int kY) {
        point1.x = -1;
        point1.y = -1;

        point2.x = kX;
        point2.y = kY;

        //left
        if(moveInLineUntilHit(mX,mY,1,0,board,kX,kY,point1).equals(point2))
            return true;
        //right
        if(moveInLineUntilHit(mX,mY,-1,0,board,kX,kY,point1).equals(point2))
            return true;
        //up
        if(moveInLineUntilHit(mX,mY,0,1,board,kX,kY,point1).equals(point2))
            return true;
        //down
        if(moveInLineUntilHit(mX,mY,0,-1,board,kX,kY,point1).equals(point2))
            return true;
        return false;
    }
}
