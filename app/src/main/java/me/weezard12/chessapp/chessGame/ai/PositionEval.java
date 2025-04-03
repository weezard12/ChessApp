package me.weezard12.chessapp.chessGame.ai;

import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.baseClasses.BasePiece;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.pieces.baseClasses.TurnType;

public class PositionEval {
    public BasePiece[][] position;
    public TurnType evalForColor;

    public float materialValue;
    public float piecesActivity;

    public float pawnStructure;

    public float kingMoves;

    public boolean isCheckMated = false;

    public float getSumEval() {
        return materialValue + piecesActivity + kingMoves + pawnStructure + (isCheckMated ? 10000 : 0);
    }

    public PositionEval(){

    }

    public PositionEval(float materialValue){
        this.materialValue = materialValue;
    }

    public boolean isBiggerThan(PositionEval otherEval){
        return (materialValue > otherEval.materialValue);
    }

    public static boolean isLeftBiggerThanRight(PositionEval firstEval,PositionEval firstEnemyEval,PositionEval secondEval,PositionEval secondEnemyEval){
        if(firstEval.position == null || firstEnemyEval.position == null)
            return false;
        if(secondEval.position == null || secondEnemyEval.position == null)
            return true;
        return ((firstEval.getSumEval() - firstEnemyEval.getSumEval()) > (secondEval.getSumEval() - secondEnemyEval.getSumEval()));
    }

    @Override
    public String toString() {
        return String.format("material: %s, pieces activity: %s king moves: %s, pawn structure: %s, sum: %s",materialValue,piecesActivity,kingMoves,pawnStructure,getSumEval()/*position==null?"empty position": GameBoard.toStringBoardArrayList(position)*/);
    }
}
