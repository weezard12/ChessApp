package me.weezard12.chessapp.chessGame.ai;

public enum GameStage {
    OPENING(0.45f),
    START_GAME(0.45f),
    MIDDLE_GAME(0.01f),
    END_GAME(0f);

    public final float centerControlValue;

    GameStage(float centerControlValue) {
        this.centerControlValue = centerControlValue;
    }
}
