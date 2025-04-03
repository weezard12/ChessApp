package me.weezard12.chessapp.chessGame.board;

import android.graphics.PointF;
import android.graphics.RectF;

import com.example.android2dtest.gameLogic.MyDebug;
import com.example.android2dtest.gameLogic.myPhysics.shapes.Box;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.baseClasses.BasePiece;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.scenes.base.ChessSceneBase;

import java.util.ArrayList;

public class Tile {
    GameBoard gameBoard;
    public TileHighlightType highlightType = TileHighlightType.NONE;

    // From 0 - 7 (tiles on the board)
    public int posX;
    public int posY;

    public PointF positionOnScreen;
    Box bounds;
    RectF collisionBounds;

    public int getTileBoundsYAsPos() {
        return (int) positionOnScreen.y;
    }

    public Tile(int posX, int posY, int boundsX, int boundsY, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.posX = posX;
        this.posY = posY;

        // Calculate tile position on screen
        float tileSize = ChessSceneBase.tileSize;
        float tileX = boundsX * tileSize + GameBoard.offsetToRight;
        float tileY = boundsY * tileSize ;

        this.positionOnScreen = new PointF(tileX + (tileSize/2), tileY + (tileSize/2));
        this.bounds = new Box(tileSize, tileSize);

        // Set up collision bounds
        this.collisionBounds = new RectF(
                tileX,
                tileY,
                tileX + tileSize,
                tileY + tileSize
        );
    }

    // Alternative constructor using float-based bounds
    public Tile(int posX, int posY, float boundsX, float boundsY, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.posX = posX;
        this.posY = posY;

        float tileSize = ChessSceneBase.tileSize;
        this.bounds = new Box(tileSize, tileSize);
        this.positionOnScreen = new PointF(boundsX + (tileSize/2), boundsY + (tileSize/2));

        // Set up collision bounds
        this.collisionBounds = new RectF(
                boundsX,
                boundsY,
                boundsX + tileSize,
                boundsY + tileSize
        );
    }

    @Override
    public String toString() {
        return String.format("x: %s, y: %s, piece on the tile: %s ", posX, posY,
                (gameBoard.board[posY][posX] == null) ? "empty"
                        : gameBoard.board[posY][posX].type + " " + gameBoard.board[posY][posX].isEnemy);
    }

    public static void setTileHighlight(ArrayList<BasePiece[][]> moves, BasePiece piece, Tile[][] tiles) {
        int bCount = 0;
        for (BasePiece[][] boards : moves) {
            for (BasePiece[] row : boards) {
                for (BasePiece p : row) {
                    if (p != null && p.isJustMoved) {
                        tiles[p.getPosY()][p.getPosX()].highlightType = TileHighlightType.CAN_MOVE_TO;
                        MyDebug.log("highlight", p.getPosX() + " " + p.getPosY() + " " + p.type + " " + bCount);
                    }
                }
            }
            bCount++;
        }
    }
}
