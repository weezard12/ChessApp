package me.weezard12.chessapp.chessGame.scenes.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.android2dtest.R;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.board.GameBoard;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.scenes.MenuActivity;

/**
 * TODO: document your custom view class.
 */
public class ChessSideView extends LinearLayout implements View.OnClickListener {

    Button backButton;
    Button rotateButton;

    Context context;

    public ChessSideView(Context context) {
        super(context);

        init(context);
    }

    public ChessSideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public ChessSideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    public ChessSideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context);
    }

    private void init(Context context) {

        this.context = context;

        // Inflate custom layout
        LayoutInflater.from(context).inflate(R.layout.sample_chess_side_layout, this, true);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        rotateButton = findViewById(R.id.rotateBoardButton);
        rotateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(backButton)){
            Intent intent = new Intent(context, MenuActivity.class);
            context.startActivity(intent);
        }
        else if(v.equals(rotateButton)){
            GameBoard.currentActiveBoard.isBlackRotationBoard = !GameBoard.currentActiveBoard.isBlackRotationBoard;
            GameBoard.currentActiveBoard.createTiles(GameBoard.currentActiveBoard.isBlackRotationBoard);
        }
    }

}