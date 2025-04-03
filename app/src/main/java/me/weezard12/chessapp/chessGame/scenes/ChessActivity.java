package me.weezard12.chessapp.chessGame.scenes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android2dtest.R;
import com.example.android2dtest.gameLogic.MusicManager;

public class ChessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chess);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.getInstance(this).onAppMinimized();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.getInstance(this).onAppResumed();
    }
}