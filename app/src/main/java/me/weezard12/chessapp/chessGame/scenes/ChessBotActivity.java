package me.weezard12.chessapp.chessGame.scenes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.chessGame.ai.Shtokfish;
import me.weezard12.chessapp.chessGame.ai.ShtokfishThread;
import me.weezard12.chessapp.chessGame.board.GameBoard;
import me.weezard12.chessapp.chessGame.scenes.views.GlitchedKnightView;

public class ChessBotActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton whitePieces;
    ImageButton blackPieces;

    Button startButton;

    private View glitchedKnightView;
    private TextView chessTitle;

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
        chessTitle = findViewById(R.id.chessTitle);
        
        // Find the GlitchedKnightView using the correct ID
        glitchedKnightView = findViewById(R.id.glitchedKnight);
        if (glitchedKnightView == null) {
            glitchedKnightView = GlitchedKnightView.instance;
        }

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

            // Player selected white, so bot plays as black
            if (glitchedKnightView != null) {
                glitchedKnightView.setBackgroundColor(Color.BLACK);
            }
        }
        else if(v.equals(blackPieces)){
            GameBoard.currentActiveBoard.isBlackTurn = true;

            whitePieces.setBackgroundColor(Color.TRANSPARENT);
            blackPieces.setBackgroundColor(Color.CYAN);

            // Player selected black, so bot plays as white
            if (glitchedKnightView != null) {
                glitchedKnightView.setBackgroundColor(Color.WHITE);
            }
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
                
            // Move the GlitchedKnightView to the bottom of the "Player VS Bot" label
            if (glitchedKnightView != null && chessTitle != null) {
                // Get the current layout parameters
                android.view.ViewGroup.MarginLayoutParams params = 
                        (android.view.ViewGroup.MarginLayoutParams) glitchedKnightView.getLayoutParams();
                
                // Get the position of the chessTitle
                int[] titlePos = new int[2];
                chessTitle.getLocationInWindow(titlePos);
                
                // Calculate the new position
                params.topMargin = titlePos[1] + chessTitle.getHeight() + 10; // 10dp margin below the title
                params.leftMargin = titlePos[0];
                params.rightMargin = 10;
                
                // Apply the new layout parameters
                glitchedKnightView.setLayoutParams(params);
            }

            whitePieces.setVisibility(View.INVISIBLE);
            blackPieces.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.INVISIBLE);
        }
    }
}