package me.weezard12.chessapp.chessGame.scenes;

import android.content.Intent;
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

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.database.UserSessionManager;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton logoButton;
    Button hotSitButton;
    Button botButton;
    Button settingsButton;
    Button switchAccountButton;
    Button quitButton;

    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize session manager
        sessionManager = UserSessionManager.getInstance(this);

        logoButton = (ImageButton) findViewById(R.id.logo_image_button);
        hotSitButton = (Button) findViewById(R.id.btn_play_hot_seat);
        botButton = (Button) findViewById(R.id.btn_play_vs_bot);
        settingsButton = (Button) findViewById(R.id.btn_settings);
        switchAccountButton = (Button) findViewById(R.id.btn_switch_account);
        quitButton = (Button) findViewById(R.id.btn_quit);

        logoButton.setOnClickListener(this);
        hotSitButton.setOnClickListener(this);
        botButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        switchAccountButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);

        // Update the Switch Account button text based on login status
        updateSwitchAccountButtonText();

    }

    /**
     * Update the Switch Account button text based on login status
     */
    private void updateSwitchAccountButtonText() {
        if (sessionManager.isLoggedIn()) {
            if (sessionManager.isGuest()) {
                switchAccountButton.setText("Sign In");
            } else {
                switchAccountButton.setText("Switch Account ("+sessionManager.getUsername()+")");
            }
        } else {
            switchAccountButton.setText("Sign In");
        }
    }

    @Override
    public void onClick(View v) {
        if(logoButton.equals(v)){

        }
        else if(hotSitButton.equals(v)){
            // Hide the status bar
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Intent intent = new Intent(this, ChessHotSitActivity.class);
            startActivity(intent);
        }
        else if(botButton.equals(v)){

            Intent intent = new Intent(this, ChessBotActivity.class);
            startActivity(intent);
        }
        else if(settingsButton.equals(v)){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(switchAccountButton.equals(v)){
            // Log out the current user
            sessionManager.logout();

            // Navigate to the login screen
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close this activity so user can't go back to it
        }
        else if(quitButton.equals(v)){
            finishAffinity(); // Closes all activities in the task
            System.exit(0);   // Optional, forces the app to exit
        }
    }
}