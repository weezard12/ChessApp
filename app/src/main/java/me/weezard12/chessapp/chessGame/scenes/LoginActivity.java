package me.weezard12.chessapp.chessGame.scenes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.database.DatabaseHelp;
import me.weezard12.chessapp.database.UserSessionManager;
import me.weezard12.chessapp.database.UserSettings;
import me.weezard12.chessapp.gameLogic.MusicManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEditText, passwordEditText, emailEditText;
    private Button loginButton, signupButton, guestButton;
    private TextView toggleModeTextView;
    
    private boolean isLoginMode = true;
    private DatabaseHelp databaseHelp;
    private UserSessionManager sessionManager;
    private MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Force the orientation to Portrait mode (height-wise)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Initialize database helper, session manager, and music manager
        databaseHelp = new DatabaseHelp(this);
        sessionManager = UserSessionManager.getInstance(this);
        musicManager = MusicManager.getInstance(this);
        
        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            // User is already logged in, go to menu activity
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        }
        
        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        guestButton = findViewById(R.id.guestButton);
        toggleModeTextView = findViewById(R.id.toggleModeTextView);
        
        // Set click listeners
        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        guestButton.setOnClickListener(this);
        toggleModeTextView.setOnClickListener(this);
        
        // Set initial UI state
        updateUIForMode();
    }
    
    private void updateUIForMode() {
        if (isLoginMode) {
            // Login mode
            loginButton.setVisibility(View.VISIBLE);
            signupButton.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            toggleModeTextView.setText("Don't have an account? Switch to Signup");
        } else {
            // Signup mode
            loginButton.setVisibility(View.GONE);
            signupButton.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            toggleModeTextView.setText("Already have an account? Switch to Login");
        }
    }
    
    private void toggleMode() {
        isLoginMode = !isLoginMode;
        updateUIForMode();
    }
    
    private void handleLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Validate inputs
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        
        // Check if user exists
        int userId = databaseHelp.checkUser(username, password);
        if (userId != -1) {
            // Login successful
            sessionManager.createLoginSession(userId, username, false);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            
            // Load user settings and apply volume
            UserSettings userSettings = databaseHelp.getUserSettings(userId);
            musicManager.setMusicVolume(userSettings.getVolume());
            
            // Go to menu activity
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        } else {
            // Login failed
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void handleSignup() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        
        // Validate inputs
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        
        // Check if username already exists
        if (databaseHelp.usernameExists(username)) {
            usernameEditText.setError("Username already exists");
            return;
        }
        
        // Insert new user
        long userId = databaseHelp.insertUser(username, email, password);
        if (userId != -1) {
            // Signup successful
            sessionManager.createLoginSession((int)userId, username, false);
            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
            
            // Load user settings and apply volume (default settings were created during registration)
            UserSettings userSettings = databaseHelp.getUserSettings((int)userId);
            musicManager.setMusicVolume(userSettings.getVolume());
            
            // Go to menu activity
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        } else {
            // Signup failed
            Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void handleGuestLogin() {
        // Create guest session
        sessionManager.createLoginSession(-1, "Guest", true);
        Toast.makeText(this, "Logged in as guest", Toast.LENGTH_SHORT).show();
        
        // Use default volume for guest
        UserSettings defaultSettings = new UserSettings();
        musicManager.setMusicVolume(defaultSettings.getVolume());
        
        // Go to menu activity
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(loginButton)) {
            handleLogin();
        } else if (v.equals(signupButton)) {
            handleSignup();
        } else if (v.equals(guestButton)) {
            handleGuestLogin();
        } else if (v.equals(toggleModeTextView)) {
            toggleMode();
        }
    }
}
