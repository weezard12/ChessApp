package me.weezard12.chessapp.chessGame.pieces;

import com.example.android2dtest.scenes.exampleScenes.ChessTest.board.GameBoard;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.baseClasses.BasePiece;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.baseClasses.PieceType;

import java.util.ArrayList;

public class KingPiece extends BasePiece {
    public boolean isEverMoved = false;
    public KingPiece(boolean isEnemy, BasePiece[][] board) {
        super(isEnemy, board);
        type = PieceType.KING;
    }

    @Override
    public void getAllPossibleMoves(int pX, int pY, ArrayList<BasePiece[][]> r) {
        posX = pX;
        posY = pY;


        if(!this.isEverMoved)
            if(posX == 4){

                if(!isKingInCheck(posX, posY, isEnemy, board)){

                    //castle O-O
                    if (board[posY][posX + 3] != null)
                        if (board[posY][posX + 3].type == PieceType.ROOK && board[posY][posX + 3].isEnemy == this.isEnemy)
                            if (!((RookPiece) board[posY][posX + 3]).isEverMoved) {

                                //check if there is a check when moved
                                if (canKingMove(posX, posY, isEnemy, posX + 1, posY, board)) {
                                    if (canKingMove(posX, posY, isEnemy, posX + 2, posY, board)) {
                                        BasePiece[][] shortCastle = GameBoard.cloneBoard(board);
                                        shortCastle[posY][posX + 2] = shortCastle[posY][posX];
                                        ((KingPiece) shortCastle[posY][posX + 2]).isEverMoved = true;

                                        shortCastle[posY][posX] = null;
                                        shortCastle[posY][posX + 2].isJustMoved = true;
                                        shortCastle[posY][posX + 1] = shortCastle[posY][posX + 3];
                                        shortCastle[posY][posX + 3] = null;

                                        r.add(shortCastle);
                                    }
                                }
                            }


                    //castle O-O-O
                    if (board[posY][posX - 4] != null)
                        if (board[posY][posX - 4].type == PieceType.ROOK && board[posY][posX - 4].isEnemy == this.isEnemy)
                            if (!((RookPiece) board[posY][posX - 4]).isEverMoved) {

                                //check if there is a check when moved
                                if (canKingMove(posX, posY, isEnemy, posX - 1, posY, board)) {
                                    if (canKingMove(posX, posY, isEnemy, posX - 2, posY, board)) {

                                        if (canKingMove(posX, posY, isEnemy, posX - 3, posY, board)) {
                                            BasePiece[][] longCastle = GameBoard.cloneBoard(board);
                                            longCastle[posY][posX - 2] = longCastle[posY][posX];
                                            ((KingPiece) longCastle[posY][posX - 2]).isEverMoved = true;

                                            longCastle[posY][posX] = null;
                                            longCastle[posY][posX - 2].isJustMoved = true;
                                            longCastle[posY][posX - 1] = longCastle[posY][posX - 4];
                                            longCastle[posY][posX - 4] = null;

                                            r.add(longCastle);
                                        }
                                    }
                                }
                            }

                }



            }

        boolean m = isEverMoved;
        if(!m)
            isEverMoved = true;
        BasePiece[][] option1 = GameBoard.cloneBoard(board);
        BasePiece[][] option2 = GameBoard.cloneBoard(board);
        BasePiece[][] option3 = GameBoard.cloneBoard(board);
        BasePiece[][] option4 = GameBoard.cloneBoard(board);
        BasePiece[][] option5 = GameBoard.cloneBoard(board);
        BasePiece[][] option6 = GameBoard.cloneBoard(board);
        BasePiece[][] option7 = GameBoard.cloneBoard(board);
        BasePiece[][] option8 = GameBoard.cloneBoard(board);
        if(!m)
            isEverMoved = false;

        movePiece(posX,posY,isEnemy,posX+1,posY +1 ,option1,r);
        movePiece(posX,posY,isEnemy,posX+1,posY,option2,r);
        movePiece(posX,posY,isEnemy,posX+1,posY-1,option3,r);

        movePiece(posX,posY,isEnemy,posX,posY+1,option4,r);
        movePiece(posX,posY,isEnemy,posX,posY-1,option5,r);

        movePiece(posX,posY,isEnemy,posX-1,posY+1,option6,r);
        movePiece(posX,posY,isEnemy,posX-1,posY,option7,r);
        movePiece(posX,posY,isEnemy,posX-1,posY-1,option8,r);



        //Tile.setTileHighlight(r,this, GameBoard.tiles);

    }

    @Override
    public boolean doesCheck(int mX,int mY,int kX, int kY) {
        if(kX == mX+1 || kX == mX-1 || kX == mX)
            if(kY == mY+1 || kY == mY-1 || kY == mY)
                return true;


        return false;
    }


}
