package me.weezard12.chessapp.chessGame.ai;

/**
 * Class for storing bord Eval.
 * Stores two PositionEval.
 */
public class BoardEval {
    public PositionEval whiteEval;
    public PositionEval blackEval;

    public BoardEval(PositionEval whiteEval,PositionEval blackEval){
        this.whiteEval = whiteEval;
        this.blackEval = blackEval;
    }

    public float getWhiteEvalAsPresent(){
        return whiteEval.getSumEval() / (whiteEval.getSumEval() + blackEval.getSumEval());
    }
    public int getWhiteEvalSubtractByBlack(){
        return (int)(whiteEval.getSumEval() - blackEval.getSumEval());
    }

    @Override
    public String toString() {
        return String.format("White: %s \n Black: %s",whiteEval.toString(),blackEval.toString());
    }
}
