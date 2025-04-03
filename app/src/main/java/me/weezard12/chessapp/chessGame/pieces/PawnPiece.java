package me.weezard12.chessapp.chessGame.pieces;

import me.weezard12.chessapp.chessGame.board.GameBoard;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.BasePiece;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.PieceType;

import java.util.ArrayList;

public class PawnPiece extends BasePiece {

    public boolean isMovedTwo = false;

    public PawnPiece(boolean isEnemy, BasePiece[][] board) {
        super(isEnemy, board);
        type = PieceType.PAWN;
    }

    @Override
    public void getAllPossibleMoves(int pX, int pY, ArrayList<BasePiece[][]> r) {
        posX = pX;
        posY = pY;

        int isEnemyInt = isEnemy ? 1 : 0;


        //one move
        if(board[posY+1 - (2 * isEnemyInt)][posX] == null){

            BasePiece[][] oneMove = GameBoard.cloneBoard(board);

            //double move
            if (posY == 1 + (5 * isEnemyInt)){
                if(board[posY + 2 * (isEnemy?- 1 : 1)][posX] == null){
                    isMovedTwo = true;
                    BasePiece[][] twoMove = GameBoard.cloneBoard(board);
                    movePiece(posX,posY,isEnemy,posX,posY+2 - (4 * isEnemyInt),twoMove,r);
                    isMovedTwo = false;
                }
            }

            boolean moved = movePiece(posX, posY, isEnemy, posX,posY+1 - (2 * isEnemyInt),oneMove);

            //makes a queen
            if((posY + 1 - (2 * isEnemyInt)) == (7 * (isEnemy? 0 : 1))){
                oneMove[posY+1 - (2 * isEnemyInt)][posX] = new QueenPiece(this.isEnemy,oneMove);
                oneMove[posY+1 - (2 * isEnemyInt)][posX].isJustMoved = true;
            }
            if(moved)
                r.add(oneMove);
        }

        //eat left
        if(posX>0)
            if(board[posY+1 - (2 * isEnemyInt)][posX-1] != null){
                BasePiece[][] eatLeft = GameBoard.cloneBoard(board);
                movePiece(posX,posY,isEnemy,posX-1,posY+1 - (2 * isEnemyInt),eatLeft,r);

                //makes a queen
                if((posY + 1 - (2 * isEnemyInt)) == (7 * (isEnemy? 0 : 1))){
                    eatLeft[posY+1 - (2 * isEnemyInt)][posX-1] = new QueenPiece(this.isEnemy,eatLeft);
                    eatLeft[posY+1 - (2 * isEnemyInt)][posX-1].isJustMoved = true;
                }

            }
            //en passant left
            else {
                if (posY == 4 - isEnemyInt)
                    if(board[posY][posX-1] != null)
                    {
                        if(board[posY][posX-1] instanceof PawnPiece)
                            if(((PawnPiece)board[posY][posX-1]).isMovedTwo){
                                BasePiece[][] enPassant = GameBoard.cloneBoard(board);
                                enPassant[posY][posX-1] = null;
                                movePiece(posX,posY,isEnemy,posX-1,posY+1 -2 *(this.isEnemy?1:0),enPassant,r);

                            }
                    }


            }
        //eat right
        if(posX<7)
            if(board[posY+1 - (2 * isEnemyInt)][posX+1] != null){
                BasePiece[][] eatRight = GameBoard.cloneBoard(board);
                movePiece(posX,posY,isEnemy,posX+1,posY+1 - (2 * isEnemyInt),eatRight,r);

                //makes a queen
                if((posY + 1 - (2 * isEnemyInt)) == (7 * (isEnemy? 0 : 1))){
                    eatRight[posY+1 - (2 * isEnemyInt)][posX+1] = new QueenPiece(this.isEnemy,eatRight);
                    eatRight[posY+1 - (2 * isEnemyInt)][posX+1].isJustMoved = true;
                }

            }
            //en passant right
            else {
                if (posY == 4 - isEnemyInt)
                    if(board[posY][posX+1]!=null)
                        if(board[posY][posX+1] instanceof PawnPiece)
                            if(((PawnPiece)board[posY][posX+1]).isMovedTwo){
                                BasePiece[][] enPassant = GameBoard.cloneBoard(board);
                                enPassant[posY][posX+1] = null;
                                movePiece(posX,posY,isEnemy,posX+1,posY+1 -2 *(this.isEnemy?1:0),enPassant,r);

                            }
            }

/*        //promote
        if(posY == 7 * isEnemyInt)
            board[posY][posX] = new QueenPiece(PieceType.QUEEN,this.isEnemy,board);*/

        //Tile.setTileHighlight(r,this, GameBoard.tiles);
        //MyDebug.log("pawn move",GameBoard.toStringBoardArrayList(oneMove));
    }

    @Override
    public boolean doesCheck(int mX, int mY, int kX, int kY) {
        return (kX == mX - 1 || kX == mX + 1) && kY == mY + 1 - 2 * (isEnemy ? 1 : 0);
    }
}
