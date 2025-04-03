package me.weezard12.chessapp.chessGame.pieces.baseClasses;


import android.graphics.Point;

import me.weezard12.chessapp.gameLogic.MyDebug;
import me.weezard12.chessapp.gameLogic.myECS.components.renderable.Sprite;
import me.weezard12.chessapp.chessGame.board.GameBoard;

import java.util.ArrayList;

public abstract class BasePiece {
    protected BasePiece[][] board;
    public PieceType type;
    public boolean isEnemy;

    //forHighlights
    public boolean isJustMoved;


    //from 0 - 8 (tiles on the board)
    public int posX;
    public int getPosX() {
        if(isEnemy){
            for (int y = board.length-1; y > -1; y--) {
                for (int x = 0; x < board[y].length; x++) {
                    if (board[x][y] == this) {
                        return y;
                    }
                }
            }
        }
        else for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[x][y] == this) {
                    return y;
                }
            }
        }
        MyDebug.log("BasePiece","cant fined piece X");
        return 0;
    }

    public int posY;
    public int getPosY() {
        if(isEnemy){
            for (int y = board.length-1; y > -1; y--) {
                for (int x = 0; x < board[y].length; x++) {
                    if (board[x][y] == this) {
                        return x;
                    }
                }
            }
        }
        else for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[x][y] == this) {
                    return x;
                }
            }
        }
        MyDebug.log("BasePiece","cant fined piece X");
        return 0;
    }

    public void updatePos(){
        posX = getPosX();
        posY = getPosY();
    }

    public Sprite texture;
    public BasePiece(boolean isEnemy,BasePiece[][] board){
        this.board = board;

        this.isEnemy=isEnemy;

    }

    public void removeFromBoard(){
        board[posY][posX] = null;
    }

    public void getAllPossibleMoves(int pX, int pY, ArrayList<BasePiece[][]> r){
    }
    public boolean doesCheck(int mX,int mY, int kX,int kY){
        //MyDebug.log("doesCheck func",String.format("mX: %s, mY: %s, kX: %s, kY: %s",mX,mY,kX,kY));
        return false;
    }


    //simple move (just changing pos ones)
    public boolean movePiece(int cX, int cY,boolean isEnemy, int mX,int mY,BasePiece[][] board){
        MyDebug.log("movePiece func",String.format("mX: %s, mY: %s, posX: %s, posY: %s",mX,mY,cX,cY));

        //don't move a null piece
        if(board[cY][cX]==null)
            return false;

        //outside the board
        if(mY < 0 || mY > 7 ||mX < 0 || mX > 7)
            return false;

        //don't move to the same color (if not null)
        if(board[mY][mX] != null)
            if (isEnemy == board[mY][mX].isEnemy)
                return false;

        board[mY][mX] = board[cY][cX];
        board[mY][mX].isJustMoved = true;
        board[cY][cX] = null;

        //don't move if there is check (might be a pin)
        if(GameBoard.isColorInCheck(board,isEnemy))
            return false;

        MyDebug.log("movePiece func","moved");
        return true;
    }
    public void movePiece(int cX, int cY, boolean isEnemy, int mX, int mY, BasePiece[][] board, ArrayList<BasePiece[][]> moves){
         if(movePiece(cX,cY,isEnemy,mX,mY,board))
            moves.add(board);

    }

    //like the move method but without moving ( ONLY FOR KING)
    public boolean canKingMove(int cX, int cY,boolean isEnemy, int mX,int mY,BasePiece[][] board){
        MyDebug.log("movePiece func",String.format("mX: %s, mY: %s, posX: %s, posY: %s",mX,mY,cX,cY));

        //don't move a null piece
        if(board[cY][cX]==null)
            return false;

        //outside the board
        if(mY < 0 || mY > 7 ||mX < 0 || mX > 7)
            return false;

        //don't move to the same color (if not null)
        if(board[mY][mX] != null)
            if (isEnemy == board[mY][mX].isEnemy)
                return false;

        for (int y = 0; y<8;y++) {
            for (int x = 0; x < 8; x++) {
                if (board[y][x] != null)
                    if (board[y][x].isEnemy == !isEnemy) {
                        if(board[y][x].doesCheck(x,y,mX,mY))
                            return false;
                    }
            }
        }

        MyDebug.log("movePiece func","moved");
        return true;
    }

    public boolean isKingInCheck(int mX, int mY,boolean isEnemy,BasePiece[][] board){

        for (int y = 0; y<8;y++) {
            for (int x = 0; x < 8; x++) {
                if (board[y][x] != null)
                    if (board[y][x].isEnemy == !isEnemy) {
                        if(board[y][x].doesCheck(x,y,mX,mY))
                            return true;
                    }
            }
        }

        return false;
    }

    public int getKingSafety(int kX, int kY,BasePiece[][] board){
        int safty = 0;

        if(isEmptyAt(kX + 1,kY,board))
            safty--;
        if(isEmptyAt(kX + 1,kY + 1,board))
            safty--;
        if(isEmptyAt(kX + 1,kY - 1,board))
            safty--;


        if(isEmptyAt(kX,kY + 1,board))
            safty--;
        if(isEmptyAt(kX,kY - 1,board))
            safty--;

        if(isEmptyAt(kX - 1,kY,board)) //
            safty--;
        if(isEmptyAt(kX - 1,kY + 1,board))
            safty--;
        if(isEmptyAt(kX - 1,kY - 1,board))
            safty--;


        return safty;
    }


    //move the piece in a line
    public void movePieceInRow(int sX, int sY,boolean isEnemy, int mX,int mY,BasePiece[][] board,ArrayList<BasePiece[][]> moves){

        BasePiece[][] cBoard = GameBoard.cloneBoard(board);
        int cX = sX;
        int cY = sY;

        BasePiece hit = moveInLineUntilHit(sX, sY, mX,mY,cBoard);
        int hitX = 0;
        int hitY = 0;
        if (hit!=null){
            hit.updatePos();
            hitX = hit.posX;
            hitY = hit.posY;
            if(movePiece(sX,sY,isEnemy, hit.posX, hit.posY, cBoard)){
                moves.add(cBoard);
            }

        }

        while (true){

            cBoard = GameBoard.cloneBoard(board);
            cX += mX;
            cY += mY;

            if(cY < 0 || cY > 7 || cX < 0 || cX > 7)
                    return;

            if (hit != null)
                if (cX==hitX && cY==hitY)
                    return;

            movePiece(sX,sY,isEnemy, cX, cY, cBoard, moves);

                //MyDebug.log("Moved in row","");



        }


    }

    //check for piece in line without moving
    public BasePiece moveInLineUntilHit(int cX, int cY, int mX,int mY,BasePiece[][] board){
        cX += mX;
        cY += mY;
        while (true){
            if(cX > 7 || cX < 0 || cY>7 || cY<0)
                return null;

            if(board[cY][cX] != null)
                return board[cY][cX];
            cX +=mX;
            cY +=mY;

        }

    }
    public Point moveInLineUntilHit(int cX, int cY, int mX, int mY, BasePiece[][] board, int kX, int kY , Point point){
        cX += mX;
        cY += mY;
        while (true){
            if(cX > 7 || cX < 0 || cY>7 || cY<0){
                return point;
            }


            if(board[cY][cX] != null)
            {
                point.x = cX;
                point.y = cY;
                return point;
            }
            else if(cX==kX && cY == kY){
                point.x = cX;
                point.y = cY;
                return point;
            }

            cX +=mX;
            cY +=mY;

        }
    }

    public Point moveInLineUntilHitEnemy(int cX, int cY, int mX,int mY,BasePiece[][] board,boolean isEnemy, Point point){
        cX += mX;
        cY += mY;
        while (true){
            if(cX > 7 || cX < 0 || cY>7 || cY<0){
                return null;
            }

            if(board[cY][cX] != null)
            {
                if(board[cY][cX].isEnemy != isEnemy){
                    point.x = cX;
                    point.y = cY;
                    return point;
                }

            }

            cX +=mX;
            cY +=mY;

        }
    }

    public boolean isEmptyAt(int cX,int cY, BasePiece[][] board){

        if(cY < 0 || cY > 7 ||cX < 0 || cX > 7)
            return false;

        if (board[cY][cX] == null)
            return true;

        return false;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj==null)
            return false;
        return ((BasePiece)obj).type == type;
    }

    @Override
    public String toString() {
        return String.format("pX: %s, pY: %s, type: %s",getPosX(),getPosY(),isEnemy? "Black " + type : type);
    }
}
