package me.weezard12.chessapp.chessGame.scenes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android2dtest.R;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.ai.Shtokfish;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.ai.ShtokfishThread;
import com.example.android2dtest.scenes.exampleScenes.ChessTest.board.GameBoard;

public class ChessBotActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton whitePieces;
    ImageButton blackPieces;

    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chess_bot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        whitePieces = findViewById(R.id.whitePieces);
        whitePieces.setBackgroundColor(Color.CYAN);
        blackPieces = findViewById(R.id.blackPieces);
        startButton = findViewById(R.id.startButton);

        whitePieces.setOnClickListener(this);
        blackPieces.setOnClickListener(this);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(whitePieces)){
            GameBoard.currentActiveBoard.isBlackTurn = false;

            whitePieces.setBackgroundColor(Color.CYAN);
            blackPieces.setBackgroundColor(Color.TRANSPARENT);
        }
        else if(v.equals(blackPieces)){
            GameBoard.currentActiveBoard.isBlackTurn = true;

            whitePieces.setBackgroundColor(Color.TRANSPARENT);
            blackPieces.setBackgroundColor(Color.CYAN);
        }
        else if (v.equals(startButton)){
            GameBoard gameBoard = GameBoard.currentActiveBoard;
            gameBoard.currentActiveBoard.isUpdatingInput = true;

            if(gameBoard.isBlackTurn)
                if(gameBoard.moveTheBot){
                    gameBoard.isBlackTurn = !gameBoard.isBlackTurn;
                    Shtokfish.thread.interrupt();
                    Shtokfish.thread = new ShtokfishThread(gameBoard);
                    Shtokfish.thread.start();
                }

            whitePieces.setVisibility(View.INVISIBLE);
            blackPieces.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.INVISIBLE);
        }
    }
}