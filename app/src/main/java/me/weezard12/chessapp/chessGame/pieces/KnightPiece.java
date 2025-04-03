package me.weezard12.chessapp.chessGame.pieces;

import me.weezard12.chessapp.chessGame.board.GameBoard;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.BasePiece;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.PieceType;

import java.util.ArrayList;

public class KnightPiece extends BasePiece {
    public KnightPiece(boolean isEnemy, BasePiece[][] board) {
        super(isEnemy, board);
        type = PieceType.KNIGHT;
    }
    @Override
    public void getAllPossibleMoves(int pX, int pY, ArrayList<BasePiece[][]> r) {


        BasePiece[][] option1 = GameBoard.cloneBoard(board);
        BasePiece[][] option2 = GameBoard.cloneBoard(board);
        BasePiece[][] option3 = GameBoard.cloneBoard(board);
        BasePiece[][] option4 = GameBoard.cloneBoard(board);
        BasePiece[][] option5 = GameBoard.cloneBoard(board);
        BasePiece[][] option6 = GameBoard.cloneBoard(board);
        BasePiece[][] option7 = GameBoard.cloneBoard(board);
        BasePiece[][] option8 = GameBoard.cloneBoard(board);

        movePiece(pX,pY,isEnemy,getPosX()+1,getPosY() +2 ,option1,r);
        movePiece(pX,pY,isEnemy,getPosX()-1,getPosY()+2,option2,r);

        movePiece(pX,pY,isEnemy,getPosX()+2,getPosY()+1,option3,r);
        movePiece(pX,pY,isEnemy,getPosX()+2,getPosY()-1,option4,r);

        movePiece(pX,pY,isEnemy,getPosX()-2,getPosY()+1,option5,r);
        movePiece(pX,pY,isEnemy,getPosX()-2,getPosY()-1,option6,r);

        movePiece(pX,pY,isEnemy,getPosX()-1,getPosY()-2,option7,r);
        movePiece(pX,pY,isEnemy,getPosX()+1,getPosY()-2,option8,r);


        //Tile.setTileHighlight(r,this, GameBoard.tiles);

    }

    @Override
    public boolean doesCheck(int mX, int mY, int kX, int kY) {
        //MyDebug.log("doesCheck func",String.format("mX: %s, mY: %s, kX: %s, kY: %s",mX,mY,kX,kY));
        if(kX == mX+1 && kY==mY-2)
            return true;
        if(kX == mX+1 && kY==mY+2)
            return true;
        if(kX == mX+2 && kY==mY+1)
            return true;
        if(kX == mX-2 && kY==mY+1)
            return true;
        if(kX == mX+2 && kY==mY-1)
            return true;
        if(kX == mX-2 && kY==mY-1)
            return true;
        if(kX == mX-1 && kY==mY+2)
            return true;
        if(kX == mX-1 && kY==mY-2)
            return true;

        return false;
    }
}
