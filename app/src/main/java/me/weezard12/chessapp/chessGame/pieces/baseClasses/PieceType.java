package me.weezard12.chessapp.chessGame.pieces.baseClasses;

public enum PieceType {
    EMPTY(0,0,0),
    PAWN(1,1,1),
    KNIGHT(3,3,3.05f),
    BISHOP(3,5,3.33f),
    ROOK(5,8,5.63f),
    QUEEN(9,13,9.5f),
    KING(0,3.75f,0);
    public final int materialValue;
    public final float movementValue;
    public final float realMaterialValue;

    PieceType(int materialValue,float movementValue,float realMaterialValue) {
        this.materialValue = materialValue;
        this.movementValue = movementValue;
        this.realMaterialValue = realMaterialValue;
    }
}
