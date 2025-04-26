package me.weezard12.chessapp.chessGame.ai;

import android.graphics.Point;

import me.weezard12.chessapp.gameLogic.MyDebug;
import me.weezard12.chessapp.chessGame.board.GameBoard;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.BasePiece;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Shtokfish {

    public static ShtokfishThread thread;
    public static BoardEval currentBoardEval = new BoardEval(new PositionEval(), new PositionEval());

    public static GameStage stage;

    private static ArrayList<Opening> openings = new ArrayList<>();


    private static int maxThreads = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);

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

        MyDebug.log("shtokfish", "thinking for " + (forBlack ? "black" : "white") + " using " + maxThreads + " threads");


        currentBoardEval = getBestPosition(board, forBlack, 1, bestEval, bestEvalForEnemy);

        boolean isFoundOpening = false;
        if (stage == GameStage.OPENING) {
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
            if (!isFoundOpening)
                stage = GameStage.START_GAME;
        } else {
            if (isInMiddleGame(board))
                stage = GameStage.MIDDLE_GAME;
        }

        MyDebug.log("shtokfish", "game stage: " + stage + "\n" + currentBoardEval.toString());
    }

    private static BoardEval getBestPosition(BasePiece[][] board, boolean forBlack, int steps,
                                             PositionEval bestEval, PositionEval bestEvalForEnemy) {
        if (Thread.interrupted())
            return currentBoardEval;

        // Store all potential moves
        List<MoveTask> allMoveTasks = new ArrayList<>();
        int movesCount = 0;

        // Generate all possible moves for the current player
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[y][x] != null && board[y][x].isEnemy == forBlack) {
                    ArrayList<BasePiece[][]> piecePositions = new ArrayList<>();
                    board[y][x].getAllPossibleMoves(x, y, piecePositions);

                    for (BasePiece[][] position : piecePositions) {
                        if (position != null) {
                            allMoveTasks.add(new MoveTask(position, forBlack, steps));
                            movesCount++;
                        }
                    }
                }
            }
        }

        // Handle checkmate condition
        if (movesCount == 0) {
            if (GameBoard.isColorInCheck(board, forBlack))
                bestEval.kingMoves = -100;
            return new BoardEval(forBlack ? bestEvalForEnemy : bestEval, forBlack ? bestEval : bestEvalForEnemy);
        }

        // Process moves in parallel if enough moves
        if (movesCount > 1 && steps > 0) {
            try {
                // Execute move evaluation in parallel
                List<Future<MoveResult>> futures = executorService.invokeAll(allMoveTasks);

                // Process results
                for (Future<MoveResult> future : futures) {
                    MoveResult result = future.get();
                    PositionEval currentEval = result.eval;
                    PositionEval currentEvalForEnemy = result.enemyEval;

                    if (PositionEval.isLeftBiggerThanRight(currentEval, currentEvalForEnemy, bestEval, bestEvalForEnemy)) {
                        bestEval = currentEval;
                        bestEvalForEnemy = currentEvalForEnemy;
                    }
                }
            } catch (Exception e) {
                MyDebug.log("shtokfish", "Parallel execution error: " + e.getMessage());
                // Fall back to sequential processing if parallel fails
                return processMovesSequentially(allMoveTasks, forBlack, bestEval, bestEvalForEnemy);
            }
        } else {
            // Process moves sequentially for small number of moves or at leaf nodes
            return processMovesSequentially(allMoveTasks, forBlack, bestEval, bestEvalForEnemy);
        }

        return new BoardEval(forBlack ? bestEvalForEnemy : bestEval, forBlack ? bestEval : bestEvalForEnemy);
    }

    private static BoardEval processMovesSequentially(List<MoveTask> moveTasks, boolean forBlack,
                                                      PositionEval bestEval, PositionEval bestEvalForEnemy) {
        for (MoveTask task : moveTasks) {
            MoveResult result = task.call();
            PositionEval currentEval = result.eval;
            PositionEval currentEvalForEnemy = result.enemyEval;

            if (PositionEval.isLeftBiggerThanRight(currentEval, currentEvalForEnemy, bestEval, bestEvalForEnemy)) {
                bestEval = currentEval;
                bestEvalForEnemy = currentEvalForEnemy;
            }
        }

        return new BoardEval(forBlack ? bestEvalForEnemy : bestEval, forBlack ? bestEval : bestEvalForEnemy);
    }

    // Class to evaluate a single move
    private static class MoveTask implements Callable<MoveResult> {
        private final BasePiece[][] position;
        private final boolean forBlack;
        private final int steps;

        public MoveTask(BasePiece[][] position, boolean forBlack, int steps) {
            this.position = GameBoard.cloneBoard(position);
            this.forBlack = forBlack;
            this.steps = steps;
        }

        @Override
        public MoveResult call() {
            PositionEval currentEval = new PositionEval();
            PositionEval currentEvalForEnemy = new PositionEval();

            if (steps > 0) {
                // Moves the other color to get the real position
                BoardEval boardEval = getBestPosition(
                        GameBoard.cloneBoard(position),
                        !forBlack,
                        steps - 1,
                        new PositionEval(),
                        new PositionEval()
                );

                // Sets the current eval to the eval after other color moved
                currentEval = forBlack ? boardEval.blackEval : boardEval.whiteEval;
                currentEval.position = position;

                currentEvalForEnemy = forBlack ? boardEval.whiteEval : boardEval.blackEval;
                currentEvalForEnemy.position = position;
            } else {
                // Gets the eval in the position (for black and white)
                calculateEvalForPosition(position, currentEval, forBlack);
                calculateEvalForPosition(position, currentEvalForEnemy, !forBlack);
            }

            return new MoveResult(currentEval, currentEvalForEnemy);
        }
    }

    private static class MoveResult {
        public final PositionEval eval;
        public final PositionEval enemyEval;

        public MoveResult(PositionEval eval, PositionEval enemyEval) {
            this.eval = eval;
            this.enemyEval = enemyEval;
        }
    }

    public static void calculateEvalForPosition(BasePiece[][] position, PositionEval eval, boolean forBlack) {
        eval.position = position;

        eval.materialValue = 0;
        eval.piecesActivity = 0;
        eval.kingMoves = 0;
        eval.pawnStructure = 0;

        ArrayList<BasePiece[][]> moves = new ArrayList<>();

        // region king safety
        Point p = GameBoard.finedKingInBoard(position, forBlack);
        BasePiece king;

        if (p.x != -10) {
            king = position[p.y][p.x];
        } else {
            eval.kingMoves = -100;
            return;
        }

        eval.kingMoves = king.getKingSafety(p.x, p.y, position) * 0.004f;
        // endregion

        // for checkmate
        int movesCount = 0;
        boolean canEnemyMove = false;
        boolean isEnemyChecked = false;
        Point ep = GameBoard.finedKingInBoard(position, !forBlack);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (position[y][x] != null)
                    if (forBlack == position[y][x].isEnemy) {

                        // for enemy check
                        if (!isEnemyChecked)
                            if (position[y][x].doesCheck(x, y, ep.x, ep.y)) {
                                isEnemyChecked = true;
                            }

                        // clears the moves every time
                        moves.clear();

                        // material
                        eval.materialValue += position[y][x].type.realMaterialValue;

                        // piece activity
                        position[y][x].getAllPossibleMoves(x, y, moves);
                        eval.piecesActivity += moves.size() * position[y][x].type.movementValue * 0.002f;

                        movesCount += moves.size();

                        // pawn structure
                        if (position[y][x].type == PieceType.PAWN)
                            eval.pawnStructure += getPawnValue(position, x, y, forBlack);

                        // center control (after the opening)
                        if (position[y][x].doesCheck(x, y, 3, 3))
                            eval.pawnStructure += stage.centerControlValue;
                        if (position[y][x].doesCheck(x, y, 3, 4))
                            eval.pawnStructure += stage.centerControlValue;
                        if (position[y][x].doesCheck(x, y, 4, 3))
                            eval.pawnStructure += stage.centerControlValue;
                        if (position[y][x].doesCheck(x, y, 4, 4))
                            eval.pawnStructure += stage.centerControlValue;

                    } else if (!canEnemyMove) {
                        // clears the moves every time
                        moves.clear();

                        // piece activity
                        position[y][x].getAllPossibleMoves(x, y, moves);
                        if (moves.size() > 0)
                            canEnemyMove = true;
                    }
            }
        }

        if (!canEnemyMove && isEnemyChecked)
            eval.isCheckMated = true;
    }

    private static float getPawnValue(BasePiece[][] board, int pX, int pY, boolean isEnemy) {
        float value = 0;

        float clearPathValue = (isEnemy ? 7 - pY : pY) * 0.01f;
        float advanceValue = (isEnemy ? 7 - pY : pY) * (isEnemy ? 7 - pY : pY) * 0.0003f;
        float protectedMultiplayer = 1.1f;

        // edge pawn
        if (pX == 0 || pX == 7)
            value -= 0.0001f;

        // how much the pawn is close to promotion
        value += advanceValue;

        // if the pawn path is clear (original, left, right)
        Point p = new Point(-1, -1);
        Point pl = new Point(-1, -1);
        Point pr = new Point(-1, -1);

        p = board[pY][pX].moveInLineUntilHitEnemy(pX, pY, 0, isEnemy ? -1 : 1, board, isEnemy, p);
        pl = board[pY][pX].moveInLineUntilHitEnemy(pX - 1, pY, 0, isEnemy ? -1 : 1, board, isEnemy, pl);
        pr = board[pY][pX].moveInLineUntilHitEnemy(pX + 1, pY, 0, isEnemy ? -1 : 1, board, isEnemy, pr);

        if (p != null) {
            if (board[p.y][p.x].type != PieceType.PAWN) {
                value += clearPathValue;
                protectedMultiplayer = 4f;

                if (pl != null)
                    if (board[pl.y][pl.x].type != PieceType.PAWN) {
                        protectedMultiplayer = 7f;

                        if (pr != null)
                            if (board[pr.y][pr.x].type != PieceType.PAWN)
                                protectedMultiplayer = 8f;
                    }
            }
        } else {
            value += clearPathValue;
            protectedMultiplayer = 8.4f;
        }

        // if the pawn is protected
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[y][x] != null)
                    if (board[y][x].isEnemy == isEnemy) {
                        if (board[y][x].doesCheck(x, y, pX, pY)) {
                            value += advanceValue * protectedMultiplayer;
                            return value;
                        }
                    }
            }
        }

        return value;
    }

    private static boolean isInMiddleGame(BasePiece[][] position) {
        if (position[3][3] != null)
            if (position[3][3].type == PieceType.PAWN)
                return false;
        if (position[3][4] != null)
            if (position[3][4].type == PieceType.PAWN)
                return false;
        if (position[4][3] != null)
            if (position[4][3].type == PieceType.PAWN)
                return false;
        if (position[4][4] != null)
            if (position[4][4].type == PieceType.PAWN)
                return false;

        return true;
    }

    // Method to clean up thread pool when the game ends
    public static void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    // Method to reinitialize thread pool if needed (e.g., for a new game)
    public static void reinitializeThreadPool() {
        shutdown();
        executorService = Executors.newFixedThreadPool(maxThreads);
    }

    // Method to set the number of threads to use
    public static void setThreadCount(int threads) {
        maxThreads = threads > 0 ? threads : Runtime.getRuntime().availableProcessors();
        reinitializeThreadPool();
    }
}

