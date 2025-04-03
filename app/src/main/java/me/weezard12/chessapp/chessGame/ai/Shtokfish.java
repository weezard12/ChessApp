package me.weezard12.chessapp.chessGame.ai;

import android.graphics.Point;

import com.example.android2dtest.gameLogic.MyDebug;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.board.GameBoard;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.baseClasses.BasePiece;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.baseClasses.PieceType;

import java.util.ArrayList;

public class Shtokfish {

    public static ShtokfishThread thread;
    public static BoardEval currentBoardEval = new BoardEval(new PositionEval(), new PositionEval());

    public static GameStage stage;

    private static ArrayList<Opening> openings = new ArrayList<>();

    public static void init(GameBoard gameBoard) {
        thread = new ShtokfishThread(gameBoard);

        setupOpenings();

        stage = GameStage.OPENING;
    }

    private static void setupOpenings() {
        openings.clear();
        //region d4 d5
        openings.add(new Opening("London System",

            "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //start
                "Bp,Bp,Bp,Bp,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,p,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //d4
                "Bp,Bp,Bp,Bp,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,p,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //d5
                "Bp,Bp,Bp,e,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,Bp,e,e,e,e," +
                "e,e,e,p,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //bf4
                "Bp,Bp,Bp,e,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,Bp,e,e,e,e," +
                "e,e,e,p,e,b,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,e,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,e,Br," + //Nf6
                "Bp,Bp,Bp,e,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,Bk,e,e," +
                "e,e,e,Bp,e,e,e,e," +
                "e,e,e,p,e,b,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,e,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,e,Br," + //e3
                "Bp,Bp,Bp,e,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,Bk,e,e," +
                "e,e,e,Bp,e,e,e,e," +
                "e,e,e,p,e,b,e,e," +
                "e,e,e,e,p,e,e,e," +
                "p,p,p,e,e,p,p,p," +
                "r,k,e,q,K,b,k,r,"

        ));
        //endregion

        //region d4 e5
        openings.add(new Opening("Englund Gambit",
            "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //d4
                "Bp,Bp,Bp,Bp,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,p,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //e5
                "Bp,Bp,Bp,Bp,e,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,Bp,e,e,e," +
                "e,e,e,p,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //dxe5
                "Bp,Bp,Bp,Bp,e,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,p,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,e,Bb,Bq,BK,Bb,Bk,Br," + //Nc6
                "Bp,Bp,Bp,Bp,e,Bp,Bp,Bp," +
                "e,e,Bk,e,e,e,e,e," +
                "e,e,e,e,p,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,e,Bb,Bq,BK,Bb,Bk,Br," + //Nf3
                "Bp,Bp,Bp,Bp,e,Bp,Bp,Bp," +
                "e,e,Bk,e,e,e,e,e," +
                "e,e,e,e,p,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,k,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,e,r," +

                "Br,e,Bb,e,BK,Bb,Bk,Br," + //Qe7
                "Bp,Bp,Bp,Bp,Bq,Bp,Bp,Bp," +
                "e,e,Bk,e,e,e,e,e," +
                "e,e,e,e,p,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,e,k,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,e,r," +

                "Br,e,Bb,e,BK,Bb,Bk,Br," + //Nc3
                "Bp,Bp,Bp,Bp,Bq,Bp,Bp,Bp," +
                "e,e,Bk,e,e,e,e,e," +
                "e,e,e,e,p,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,k,e,e,k,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,e,b,q,K,b,e,r," +

                "Br,e,Bb,e,BK,Bb,Bk,Br," + //Nxe5
                "Bp,Bp,Bp,Bp,Bq,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,Bk,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,k,e,e,k,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,e,b,q,K,b,e,r," +

                "Br,e,Bb,e,BK,Bb,Bk,Br," + //e4
                "Bp,Bp,Bp,Bp,Bq,Bp,Bp,Bp," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,e,Bk,e,e,e," +
                "e,e,e,e,p,e,e,e," +
                "e,e,k,e,e,k,e,e," +
                "p,p,p,e,e,p,p,p," +
                "r,e,b,q,K,b,e,r,"

        ));
        //endregion

        //region d4
        openings.add(new Opening("Queen's Pawn Opening: Horwitz Defense",
            "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //e5
                "Bp,Bp,Bp,Bp,e,Bp,Bp,Bp," +
                "e,e,e,e,Bp,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,p,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,Bk,Br," + //e5
                "Bp,Bp,Bp,Bp,e,Bp,Bp,Bp," +
                "e,e,e,e,Bp,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,p,e,b,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,e,q,K,b,k,r,"


        ));
        //endregion

        //region d4 Nf6
        openings.add(new Opening("King’s Indian Defense",
            "Br,Bk,Bb,Bq,BK,Bb,e,Br," + //Nf6
                "Bp,Bp,Bp,Bp,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,Bk,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,e,p,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,p,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,e,Br," + //c4
                "Bp,Bp,Bp,Bp,Bp,Bp,Bp,Bp," +
                "e,e,e,e,e,Bk,e,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,p,p,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,e,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,e,Br," + //g6
                "Bp,Bp,Bp,Bp,Bp,Bp,e,Bp," +
                "e,e,e,e,e,Bk,Bp,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,p,p,e,e,e,e," +
                "e,e,e,e,e,e,e,e," +
                "p,p,e,e,p,p,p,p," +
                "r,k,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,Bb,e,Br," + //Nc3
                "Bp,Bp,Bp,Bp,Bp,Bp,e,Bp," +
                "e,e,e,e,e,Bk,Bp,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,p,p,e,e,e,e," +
                "e,e,k,e,e,e,e,e," +
                "p,p,e,e,p,p,p,p," +
                "r,e,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,e,e,Br," + //Bg7
                "Bp,Bp,Bp,Bp,Bp,Bp,Bb,Bp," +
                "e,e,e,e,e,Bk,Bp,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,p,p,e,e,e,e," +
                "e,e,k,e,e,e,e,e," +
                "p,p,e,e,p,p,p,p," +
                "r,e,b,q,K,b,k,r," +

                "Br,Bk,Bb,Bq,BK,e,e,Br," + //e4
                "Bp,Bp,Bp,Bp,Bp,Bp,Bb,Bp," +
                "e,e,e,e,e,Bk,Bp,e," +
                "e,e,e,e,e,e,e,e," +
                "e,e,p,p,p,e,e,e," +
                "e,e,k,e,e,e,e,e," +
                "p,p,e,e,e,p,p,p," +
                "r,e,b,q,K,b,k,r,"


        ));
        //endregion
    }

    public static void getBestPosition(BasePiece[][] board, boolean forBlack) {

        PositionEval bestEval = new PositionEval(0);
        PositionEval bestEvalForEnemy = new PositionEval(0);

        MyDebug.log("shtokfish", "thinking for "+(forBlack?"black":"white"));


        currentBoardEval = getBestPosition(board, forBlack, 1, bestEval, bestEvalForEnemy);

        boolean isFoundOpening = false;
        if (stage == GameStage.OPENING){
            for (Opening opening : openings) {
                BasePiece[][] p = opening.tryGetPositionIdx(board);
                if (p != null) {
                    currentBoardEval.whiteEval.position = p;
                    currentBoardEval.blackEval.position = p;
                    isFoundOpening = true;
                    MyDebug.log("shtokfish", "opening:" + opening.name);
                    break;
                }

            }
            if(!isFoundOpening)
                stage = GameStage.START_GAME;
        }

        else {
            if(isInMiddleGame(board))
                stage = GameStage.MIDDLE_GAME;
        }


        MyDebug.log("shtokfish", "game stage: "+ stage +"\n" + currentBoardEval.toString());
    }

    private static BoardEval getBestPosition(BasePiece[][] board, boolean forBlack, int steps, PositionEval bestEval, PositionEval bestEvalForEnemy) {

        if (Thread.interrupted())
            return currentBoardEval;

        ArrayList<BasePiece[][]> allPositions = new ArrayList<>();
        int movesCount = 0;

        PositionEval currentEval = new PositionEval();
        PositionEval currentEvalForEnemy = new PositionEval();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {

                if (board[y][x] != null)
                    //getPosition only for one color
                    if (board[y][x].isEnemy == forBlack) {

                        allPositions.clear();
                        board[y][x].getAllPossibleMoves(x, y, allPositions);
                        for (BasePiece[][] position : allPositions) {
                            if (position != null) {
                                if (steps > 0) {

                                    //moves the other color to get the real position
                                    BoardEval boardEval = getBestPosition(GameBoard.cloneBoard(position), !forBlack, steps - 1, new PositionEval(), new PositionEval());

                                    //sets the current eval to the eval after other color moved
                                    currentEval = forBlack ? boardEval.blackEval : boardEval.whiteEval;

                                    //sets the position after the other color moved to the current position (overriding the other color move)
                                    currentEval.position = position;

                                    currentEvalForEnemy = forBlack ? boardEval.whiteEval : boardEval.blackEval;

                                    currentEvalForEnemy.position = position;

                                } else {
                                    //gets the eval in the position (foe black and white)
                                    calculateEvalForPosition(position, currentEval, forBlack);
                                    calculateEvalForPosition(position, currentEvalForEnemy, !forBlack);
                                    //MyDebug.log("shtokfish","pos"+currentEvalForEnemy.getSumEval());
                                }

                                if (PositionEval.isLeftBiggerThanRight(currentEval, currentEvalForEnemy, bestEval, bestEvalForEnemy)) {
                                    bestEval = currentEval;
                                    bestEvalForEnemy = currentEvalForEnemy;
                                    currentEval = new PositionEval();
                                    currentEvalForEnemy = new PositionEval();
                                    //MyDebug.log("shtokfish","found pos for "+(forBlack?"black":"white"));
                                }
                                movesCount++;
                            }

                        }

                    }
            }
        }

        //is checkmate
        if(movesCount==0){
            if(GameBoard.isColorInCheck(board,forBlack))
                bestEval.kingMoves = -100;
            //MyDebug.log("shtokfish","moves possible: " + forBlack);
        }


        return new BoardEval(forBlack ? bestEvalForEnemy : bestEval, forBlack ? bestEval : bestEvalForEnemy);
    }

    public static void calculateEvalForPosition(BasePiece[][] position, PositionEval eval, boolean forBlack) {

        eval.position = position;

        eval.materialValue = 0;
        eval.piecesActivity = 0;
        eval.kingMoves = 0;
        eval.pawnStructure = 0;

        ArrayList<BasePiece[][]> moves = new ArrayList<>();

        //region king safety
        Point p = GameBoard.finedKingInBoard(position, forBlack);
        BasePiece king;

        if (p.x != -10) {
            king = position[p.y][p.x];

        } else {
            eval.kingMoves = -100;
            return;
        }

        eval.kingMoves = king.getKingSafety(p.x, p.y, position) * 0.004f;


        //endregion


        //for checkmate
        int movesCount = 0;
        boolean canEnemyMove = false;
        boolean isEnemyChecked = false;
        Point ep = GameBoard.finedKingInBoard(position, !forBlack);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (position[y][x] != null)
                    if (forBlack == position[y][x].isEnemy) {

                        //for enemy check
                        if(!isEnemyChecked)
                            if(position[y][x].doesCheck(x,y,ep.x, ep.y)){
                                isEnemyChecked = true;
                            }


                        //clears the moves every time
                        moves.clear();

                        //material
                        eval.materialValue += position[y][x].type.realMaterialValue;

                        //piece activity
                        position[y][x].getAllPossibleMoves(x, y, moves);
                        eval.piecesActivity += moves.size() * position[y][x].type.movementValue * 0.002f;

                        movesCount += moves.size();

                        //pawn structure
                        if(position[y][x].type == PieceType.PAWN)
                            eval.pawnStructure += getPawnValue(position,x,y,forBlack);


                        //center control (after the opening)
                        if(position[y][x].doesCheck(x,y,3,3))
                            eval.pawnStructure += stage.centerControlValue;
                        if(position[y][x].doesCheck(x,y,3,4))
                            eval.pawnStructure += stage.centerControlValue;
                        if(position[y][x].doesCheck(x,y,4,3))
                            eval.pawnStructure += stage.centerControlValue;
                        if(position[y][x].doesCheck(x,y,4,4))
                            eval.pawnStructure += stage.centerControlValue;



                        //MyDebug.log("shtokfish move count","count: "+moves.size);
                    }
                    else if(!canEnemyMove){

                        //clears the moves every time
                        moves.clear();

                        //piece activity
                        position[y][x].getAllPossibleMoves(x, y, moves);
                        if(moves.size() > 0)
                            canEnemyMove = true;

                    }

            }
        }
        //MyDebug.log("shtokfish enemy","count: " + isEnemyChecked);
        if(!canEnemyMove && isEnemyChecked)
            eval.isCheckMated = true;

        //eval.piecesActivity = 0;
        //eval.kingMoves = 0;
        //eval.pawnStructure = 0;

    }
    private static float getPawnValue(BasePiece[][] board,int pX, int pY,boolean isEnemy){
        float value = 0;

        float clearPathValue = (isEnemy ?  7 - pY : pY) * 0.01f;
        float advanceValue = (isEnemy? 7 - pY : pY) * (isEnemy? 7 - pY : pY) * 0.0003f;
        float protectedMultiplayer = 1.1f;


        //edge pawn
        if(pX == 0 || pX == 7)
            value -= 0.0001f;


        //how much the pawn is close to promotion
        value += advanceValue;


        //if the pawn path is clear (original, left, right)
        Point p = new Point(-1,-1);
        Point pl = new Point(-1,-1);
        Point pr = new Point(-1,-1);

        p = board[pY][pX].moveInLineUntilHitEnemy(pX,pY,0,isEnemy?-1:1,board,isEnemy,p);
        pl = board[pY][pX].moveInLineUntilHitEnemy(pX-1,pY,0,isEnemy?-1:1,board,isEnemy,pl);
        pr = board[pY][pX].moveInLineUntilHitEnemy(pX+1,pY,0,isEnemy?-1:1,board,isEnemy,pr);


        if(p != null){
            if(board[p.y][p.x].type != PieceType.PAWN) {
                value += clearPathValue;
                protectedMultiplayer = 4f;

                if(pl != null)
                    if(board[pl.y][pl.x].type != PieceType.PAWN){
                        protectedMultiplayer = 7f;

                        if(pr != null)
                            if(board[pr.y][pr.x].type != PieceType.PAWN)
                                protectedMultiplayer = 8f;
                    }

            }
        }
        else{
            value += clearPathValue;
            protectedMultiplayer = 8.4f;
        }



        //if the pawn is protected
        for (int y = 0; y<8;y++) {
            for (int x = 0; x < 8; x++) {
                if (board[y][x] != null)
                    if (board[y][x].isEnemy == isEnemy) {
                        if(board[y][x].doesCheck(x,y,pX,pY)){
                            value += advanceValue * protectedMultiplayer;
                            return value;
                        }

                    }
            }
        }


        return value;
    }

    private static boolean isInMiddleGame(BasePiece[][] position){
        if(position[3][3] != null)
            if(position[3][3].type == PieceType.PAWN)
                return false;
        if(position[3][4] != null)
            if(position[3][4].type == PieceType.PAWN)
                return false;
        if(position[4][3] != null)
            if(position[4][3].type == PieceType.PAWN)
                return false;
        if(position[4][4] != null)
            if(position[4][4].type == PieceType.PAWN)
                return false;

        return true;
    }
}
