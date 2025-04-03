package me.weezard12.chessapp.chessGame.board;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import me.weezard12.chessapp.gameLogic.MyDebug;
import me.weezard12.chessapp.gameLogic.math.Interpolation;
import me.weezard12.chessapp.gameLogic.myECS.components.renderable.Batch;
import me.weezard12.chessapp.gameLogic.myECS.components.renderable.RenderableComponent;
import me.weezard12.chessapp.chessGame.ai.Shtokfish;
import me.weezard12.chessapp.chessGame.ai.ShtokfishThread;
import me.weezard12.chessapp.chessGame.pieces.BishopPiece;
import me.weezard12.chessapp.chessGame.pieces.KingPiece;
import me.weezard12.chessapp.chessGame.pieces.KnightPiece;
import me.weezard12.chessapp.chessGame.pieces.PawnPiece;
import me.weezard12.chessapp.chessGame.pieces.QueenPiece;
import me.weezard12.chessapp.chessGame.pieces.RookPiece;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.BasePiece;
import me.weezard12.chessapp.chessGame.pieces.baseClasses.PieceType;
import me.weezard12.chessapp.chessGame.scenes.base.ChessSceneBase;

import java.util.ArrayList;

public class GameBoard extends RenderableComponent {
    public boolean isBlackTurn = false;
    public boolean isBlackRotationBoard = true;

    public boolean isUpdatingInput = false;

    public boolean moveTheBot = false;

    //region Scale and UI
    public static final int offsetToRight = ((int)(ChessSceneBase.boardSize * 0.08f));
    private float elapsedTime = 1;
    Point interpolation = new Point(0,0);
    //endregion


    //Tiles
    public static Tile[][] tiles;
    Tile selectedTile;
    private Tile movedFromTile;
    private Tile movedToTile;
    public void setMovedTiles(Point movedFrom, Point movedTo){
        if(movedFrom == null || movedTo == null)
            return;
        movedFromTile = tiles[movedFrom.y][movedFrom.x];
        movedToTile = tiles[movedTo.y][movedTo.x];
        elapsedTime = 0;
    }

    public BasePiece[][] board;
    public static BoardColors boardColors = new BoardColors(Color.WHITE,Color.BLACK,Color.BLUE,Color.CYAN);

    ArrayList<BasePiece[][]> possibleMoves = new ArrayList<>();

    //Debug
    public boolean isFreeMove = true;

    public static GameBoard currentActiveBoard;

    public GameBoard(){
        //sets up an instance
        currentActiveBoard = this;

        board = new BasePiece[8][8];

        tiles = new Tile[8][8];
        createTiles(isBlackRotationBoard);

    }

    @Override
    public void render(float delta, Canvas canvas) {
        renderBoard(canvas);
    }


    //check for input
    public void checkForInput(float inputX, float inputY){

        if(!isUpdatingInput)
            return;

        //if moving the bot, and he is thinking cancel the input
        if(moveTheBot)
            if(Shtokfish.thread.isCalculating)
                return;

        //if promotion is in process
        if(!PromotionSelection.isPromoting)
            for (Tile[] row: tiles) {
                for (Tile tile: row) {

                    if (tile.collisionBounds.contains(inputX, inputY)) {
                        MyDebug.log("click on tile", tile.toString());
                        if (selectedTile == null) {

                            clearMoveHighLight();

                            selectedTile = tile;
                            selectedTile.highlightType = TileHighlightType.SELECTED;
                            if (board[selectedTile.posY][selectedTile.posX] != null) {
                                if (isBlackTurn == board[selectedTile.posY][selectedTile.posX].isEnemy) {
                                    possibleMoves.clear();
                                    board[selectedTile.posY][selectedTile.posX].getAllPossibleMoves(selectedTile.posX, selectedTile.posY, possibleMoves);
                                    Tile.setTileHighlight(possibleMoves, board[selectedTile.posY][selectedTile.posX], tiles);
                                }

                            }

                            //MyDebug.log("check check",""+board[selectedTile.posY][selectedTile.posX].doesCheck(finedKingInBoard(board,board[selectedTile.posY][selectedTile.posX].isEnemy?false:true)));
                        }
                        else if (tile == selectedTile) {
                            clearMoveHighLight();
                            selectedTile.highlightType = TileHighlightType.NONE;
                            selectedTile = null;
                        }
                        else {
                            if(tile.highlightType==TileHighlightType.CAN_MOVE_TO)
                            {
                                if (board[selectedTile.posY][selectedTile.posX] != null) {
                                    movePiece(tile, board[selectedTile.posY][selectedTile.posX]);
                                }
                                clearMoveHighLight();
                                selectedTile.highlightType = TileHighlightType.NONE;
                                selectedTile = null;
                            }



                        }

                    }
                }




            }

    }

    public void movePiece(Tile tile,BasePiece selectedPiece){

        if (!isFreeMove)
            if(tile.highlightType != TileHighlightType.CAN_MOVE_TO)
                return;

        //for interpolation
        movedToTile = tile;
        movedFromTile = selectedTile;
        elapsedTime = 0;

        clearMoveHighLight();

            if(board[selectedTile.posY][selectedTile.posX].type==PieceType.ROOK)
                ((RookPiece)board[selectedTile.posY][selectedTile.posX]).isEverMoved = true;

            //check for queen spawn
            if(board[selectedTile.posY][selectedTile.posX].type==PieceType.PAWN){

                //if en passant left
                if(tile.posX > 0)
                    if(tile.posY == 5 - 3 * (selectedPiece.isEnemy ? 1 : 0))
                        if(selectedPiece.getPosY() == 4 - (selectedPiece.isEnemy ? 1 : 0))
                            if(board[selectedPiece.getPosY()][selectedPiece.getPosX()-1] != null)
                                if(board[selectedPiece.getPosY()][selectedPiece.getPosX()-1] instanceof PawnPiece)
                                    if(((PawnPiece)board[selectedPiece.getPosY()][selectedPiece.getPosX()-1]).isMovedTwo)
                                        board[selectedPiece.getPosY()][selectedPiece.getPosX()-1] = null;

                //if en passant right
                if(tile.posX < 7)
                    if(tile.posY == 5 - 3 * (selectedPiece.isEnemy ? 1 : 0))
                        if(selectedPiece.getPosY() == 4 - (selectedPiece.isEnemy ? 1 : 0))
                            if(board[selectedPiece.getPosY()][selectedPiece.getPosX()+1] != null)
                                if(board[selectedPiece.getPosY()][selectedPiece.getPosX()+1] instanceof PawnPiece)
                                    if(((PawnPiece)board[selectedPiece.getPosY()][selectedPiece.getPosX()+1]).isMovedTwo)
                                        board[selectedPiece.getPosY()][selectedPiece.getPosX()+1] = null;

                //if first move
                if(selectedPiece.getPosY() == 6 -5 * (selectedPiece.isEnemy ? 0 : 1))
                    if(tile.posY == (4 - (board[selectedTile.posY][selectedTile.posX].isEnemy ? 0 : 1))){
                        ((PawnPiece)selectedPiece).isMovedTwo = true;
                    }


                //promote
                if(tile.posY== 7 * (board[selectedTile.posY][selectedTile.posX].isEnemy? 0 : 1))
                    PromotionSelection.startPromotion(board[selectedTile.posY][selectedTile.posX],this,tile);
                else
                    board[tile.posY][tile.posX] = board[selectedTile.posY][selectedTile.posX];
            }
            else if(board[selectedTile.posY][selectedTile.posX].type==PieceType.KING){
                boolean isCastled = false;
                if(selectedPiece.getPosX() == 4)
                {


                    if(!((KingPiece)selectedPiece).isEverMoved)
                        if(tile.posX==2){
                            if(board[selectedPiece.getPosY()][0] != null)
                                if(board[selectedPiece.getPosY()][0].type == PieceType.ROOK)
                                    if(!((RookPiece)board[selectedPiece.getPosY()][0]).isEverMoved){
                                        board[tile.posY][2] = selectedPiece;
                                        board[tile.posY][3] = board[tile.posY][0];
                                        board[tile.posY][0] = null;
                                        isCastled = true;
                                    }
                        }
                        else if(tile.posX==6){
                            if(board[selectedPiece.getPosY()][7] != null)
                                if(board[selectedPiece.getPosY()][7].type == PieceType.ROOK)
                                    if(!((RookPiece)board[selectedPiece.getPosY()][7]).isEverMoved){
                                        board[tile.posY][6] = selectedPiece;
                                        board[tile.posY][5] = board[tile.posY][7];
                                        board[tile.posY][7] = null;
                                        isCastled = true;
                                    }
                        }
                }
                if(!isCastled)
                    board[tile.posY][tile.posX] = board[selectedTile.posY][selectedTile.posX];

            }
            else
                board[tile.posY][tile.posX] = board[selectedTile.posY][selectedTile.posX];
            if(board[tile.posY][tile.posX] instanceof KingPiece)
                ((KingPiece)board[tile.posY][tile.posX]).isEverMoved = true;

            clearEnPassantOptionFromPawns(selectedPiece.isEnemy);
            if (!PromotionSelection.isPromoting)
                board[selectedTile.posY][selectedTile.posX] = null;

            isBlackTurn = !isBlackTurn;
            elapsedTime = 0;

            if(!PromotionSelection.isPromoting){
                Shtokfish.thread.interrupt();
                Shtokfish.thread = new ShtokfishThread(this);
                Shtokfish.thread.start();
            }


    }

    //region Render
    public void renderBoard(Canvas canvas){

        drawBoard(canvas);
        drawPieces(canvas);

    }

    protected void drawBoard(Canvas canvas){
        for (int y = 0; y<8;y++){
            for (int x = 0; x<8;x++){
                Color color = null;
                switch (tiles[y][x].highlightType){
                    case NONE:
                        color = ((x+y) % 2 == 0) ? boardColors.white : boardColors.black;
                        break;
                    case SELECTED:
                        color = boardColors.selectedTile;
                        break;
                    case CAN_MOVE_TO:
                        color = boardColors.movesHighlightColor;
                        break;
                }

                //shapeDrawer.filledRectangle(new Rectangle(x*ChessSceneBase.tileSIze + offsetToRight,y*ChessSceneBase.tileSIze,ChessSceneBase.tileSIze,ChessSceneBase.tileSIze));
                Batch.drawBox(canvas, tiles[y][x].positionOnScreen.x, tiles[y][x].positionOnScreen.y  ,tiles[y][x].bounds, color);
            }

        }
    }
    protected void drawPieces(Canvas canvas){
        for (int y = 0; y < 8;y++){
            for (int x = 0; x < 8;x++){
                if(board[y][x]!=null){
                    //set piece texture if null
                    if(board[y][x].texture == null)
                        board[y][x].texture = ChessSceneBase.piecesTextures.get(String.format("%s%s",board[y][x].type.toString().toLowerCase(),board[y][x].isEnemy ? 1 : 0 ));



                    if(elapsedTime < 1)
                    {
                        if(movedToTile.posX == x && movedToTile.posY == y){
                            getPieceInterpolation(movedFromTile.positionOnScreen.x,movedFromTile.positionOnScreen.y  - 8,tiles[y][x].positionOnScreen.x,tiles[y][x].positionOnScreen.y   - 8);
                            Batch.drawSprite(canvas,board[y][x].texture,interpolation.x,interpolation.y,ChessSceneBase.tileSize,ChessSceneBase.tileSize);
                        }
                        else
                            Batch.drawSprite(canvas,board[y][x].texture,tiles[y][x].positionOnScreen.x ,tiles[y][x].positionOnScreen.y - 8,ChessSceneBase.tileSize,ChessSceneBase.tileSize);
                    }
                    else{
                        Batch.drawSprite(canvas,board[y][x].texture,tiles[y][x].positionOnScreen.x,tiles[y][x].positionOnScreen.y - 8,ChessSceneBase.tileSize,ChessSceneBase.tileSize)                       ;
                    }

                }
            }

        }
    }

    //this method works on the GameBoard interpolationPoint, elapsedTime  vars
    protected void getPieceInterpolation(float startX, float startY, float endX, float endY){
        interpolation.x = (int)Interpolation.pow2.apply(startX, endX, elapsedTime);
        interpolation.y = (int)Interpolation.pow2.apply(startY, endY, elapsedTime);
        elapsedTime += entity.scene.deltaTime * 5;
    }
    //endregion
    //region Setup Board
    public static void setBoardByString(BasePiece[][] board,String s){
        int idx = 0;
        StringBuilder piece = new StringBuilder();
        for (int y = 7; y>-1;y--){
            for (int x = 7; x>-1;x--){

                while (s.charAt(idx)!=','){
                    piece.append(s.charAt(idx));
                    idx++;
                }
                boolean color = piece.charAt(0)=='B';
                if(color)
                    piece = new StringBuilder(piece.substring(1));
                switch (piece.toString()){
                    case "p":
                       board[y][x]= new PawnPiece(color,board);
                       break;
                    case "r":
                        board[y][x]= new RookPiece(color,board);
                        break;
                    case "k":
                        board[y][x]= new KnightPiece(color,board);
                        break;
                    case "b":
                        board[y][x]= new BishopPiece(color,board);
                        break;
                    case "q":
                        board[y][x]= new QueenPiece(color,board);
                        break;
                    case "K":
                        board[y][x]= new KingPiece(color,board);
                        break;
                }
                piece = new StringBuilder();
                idx++;
            }

        }
    }
    public void createTiles(boolean isBlackTurn){

        int rotated = isBlackTurn?7:0;

        for (int y = 0; y<8;y++){
            for (int x = 0; x<8;x++){
                tiles[y][x] = new Tile( x, y,rotated + (x*(isBlackTurn?-1 : 1)),rotated + (y*(isBlackTurn?-1 : 1)),this);
            }

        }
    }
    //endregion
    public static BasePiece[][] cloneBoard(BasePiece[][] board){
        BasePiece[][] rBoard = new BasePiece[8][8];

        for (int y = 0; y<8;y++){
            for (int x = 0; x<8;x++){
                if(board[y][x] != null)
                    switch (board[y][x].type){
                        case KING:
                            rBoard[y][x] = new KingPiece(board[y][x].isEnemy,rBoard);
                            ((KingPiece)rBoard[y][x]).isEverMoved = ((KingPiece)board[y][x]).isEverMoved;
                            break;
                        case KNIGHT:
                            rBoard[y][x] = new KnightPiece(board[y][x].isEnemy,rBoard);
                            break;
                        case ROOK:
                            rBoard[y][x] = new RookPiece(board[y][x].isEnemy,rBoard);
                            ((RookPiece)rBoard[y][x]).isEverMoved = ((RookPiece)board[y][x]).isEverMoved;
                            break;
                        case BISHOP:
                            rBoard[y][x] = new BishopPiece(board[y][x].isEnemy,rBoard);
                            break;
                        case QUEEN:
                            rBoard[y][x] = new QueenPiece(board[y][x].isEnemy,rBoard);
                            break;
                        case PAWN:
                            rBoard[y][x] = new PawnPiece(board[y][x].isEnemy,rBoard);
                            ((PawnPiece)rBoard[y][x]).isMovedTwo = ((PawnPiece)board[y][x]).isMovedTwo;
                            break;
                    }

            }

        }

        return rBoard;
    }

    public static Point finedKingInBoard(BasePiece[][] board, boolean isEnemy){
        for (int y = 0; y<8;y++){
            for (int x = 0; x<8;x++){
                if(board[y][x] != null)
                    if (board[y][x].type == PieceType.KING && board[y][x].isEnemy == isEnemy)
                        return new Point(x,y);
            }
        }
        return new Point(-10,-1);
    }

    public static boolean isColorInCheck(BasePiece[][] board, boolean isEnemy){
        Point p = finedKingInBoard(board,isEnemy);
        if(p.x==-10)
            return true;
        for (int y = 0; y<8;y++){
            for (int x = 0; x<8;x++){
                if(board[y][x] != null)
                    if (board[y][x].isEnemy == !isEnemy){
                        MyDebug.log("check all",board[y][x].toString());
                        if(board[y][x].doesCheck(x,y, p.x, p.y)){
                            MyDebug.log("check all","True");
                            return true;
                        }

                    }

            }
        }
        return false;

    }

    public void clearMoveHighLight(){
        for (Tile[] row : tiles) {
            for (Tile tile : row) {

                if(tile.highlightType == TileHighlightType.CAN_MOVE_TO)
                    tile.highlightType = TileHighlightType.NONE;

            }
        }
    }
    private void clearEnPassantOptionFromPawns(boolean isEnemy){
        for (BasePiece[] row : board) {
            for (BasePiece piece : row) {
                if (piece != null)
                    if(piece.isEnemy==isEnemy)
                        if(piece instanceof PawnPiece)
                            ((PawnPiece)piece).isMovedTwo = false;
            }
        }
    }
    public static String toStringBoardArrayList(BasePiece[][] board){
        StringBuilder s = new StringBuilder();
        for (int y = 0; y<8;y++) {
            for (int x = 0; x < 8; x++) {

                if (board[y][x] != null) {
                    s.append(board[y][x].isJustMoved ? "m" : "");
                    s.append(board[y][x].type).append("-");
                    s.append(board[y][x].isEnemy ? "black " : "white ");

                }
                else s.append("empty ");

            }
            s.append("\n");
        }
        return s.toString();
    }

}
