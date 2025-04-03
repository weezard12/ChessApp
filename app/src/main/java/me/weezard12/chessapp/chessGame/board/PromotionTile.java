package me.weezard12.chessapp.chessGame.board;

import android.graphics.Color;

import com.example.android2dtest.gameLogic.myECS.GameScene;
import com.example.android2dtest.gameLogic.myECS.components.renderable.BoxRenderer;
import com.example.android2dtest.gameLogic.myECS.components.renderable.SpriteRenderer;
import com.example.android2dtest.gameLogic.myECS.components.touchable.ClickableComponent;
import com.example.android2dtest.gameLogic.myECS.entities.GameEntity;
import com.example.android2dtest.gameLogic.myPhysics.BoxCollider;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.*;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.baseClasses.PieceType;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.scenes.base.ChessSceneBase;

import java.util.List;

public class PromotionTile extends GameEntity {

    private final PieceType type;
    private final boolean isEnemy;
    GameBoard gameBoard;

    Tile promotionTile;

    BoxCollider boxCollider;
    SpriteRenderer spriteRenderer;
    BoxRenderer boxRenderer;
    ClickableComponent clickableComponent;

    public PromotionTile(Tile movedToTile, int posY, GameBoard gameBoard, PieceType type, boolean isEnemy){
        super("Promotion Tile" + posY);
        this.type = type;
        this.isEnemy = isEnemy;
        this.gameBoard = gameBoard;
        this.promotionTile = movedToTile;
        setPosition(movedToTile.positionOnScreen.x,posY * ChessSceneBase.tileSize +ChessSceneBase.tileSize*0.5f);
    }


    @Override
    public void attachToScene(GameScene scene) {
        super.attachToScene(scene);
        boxCollider = new BoxCollider(ChessSceneBase.tileSize, ChessSceneBase.tileSize);
        addComponent(boxCollider);

        boxRenderer = new BoxRenderer(ChessSceneBase.tileSize);
        boxRenderer.paint.setColor(Color.CYAN);
        addComponent(boxRenderer);

        spriteRenderer = new SpriteRenderer(ChessSceneBase.piecesTextures.get(String.format("%s%s",type.toString().toLowerCase(), isEnemy ? 1 : 0 )));
        float scaleFactor = ((float) ChessSceneBase.tileSize) / spriteRenderer.getSprite().getTexture().getWidth();
        spriteRenderer.getSprite().setScale(scaleFactor, scaleFactor);
        addComponent(spriteRenderer);

        clickableComponent = new ClickableComponent(boxCollider.getCollisionShape());
        clickableComponent.setOnClickListener(new ClickableComponent.OnClickListener() {
            @Override
            public void onClick(float x, float y) {
                setNewPieceAt(promotionTile.posX, promotionTile.posY);

                PromotionSelection.isPromoting = false;
                List<GameEntity> entitiesToRemove = scene.getEntitiesByPattern("Promotion Tile");
                for (GameEntity entity : entitiesToRemove) {
                    entity.destroy();
                }

            }
        });
        addComponent(clickableComponent);

    }

    public void setNewPieceAt(int x, int y){
        switch (type){
            case KING:
            gameBoard.board[y][x] = new KingPiece(isEnemy,gameBoard.board);
            break;
            case KNIGHT:
                gameBoard.board[y][x] = new KnightPiece(isEnemy,gameBoard.board);
            break;
            case ROOK:
                gameBoard.board[y][x] = new RookPiece(isEnemy,gameBoard.board);
            break;
            case BISHOP:
                gameBoard.board[y][x] = new BishopPiece(isEnemy,gameBoard.board);
            break;
            case QUEEN:
                gameBoard.board[y][x] = new QueenPiece(isEnemy,gameBoard.board);
            break;
            case PAWN:
                gameBoard.board[y][x] = new PawnPiece(isEnemy,gameBoard.board);
            break;

        }

    }
}
