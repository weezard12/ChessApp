package me.weezard12.chessapp.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages user session information (login state, current user ID)
 */
public class UserSessionManager {
    private static final String PREF_NAME = "ChessAppUserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_GUEST = "isGuest";
    
    private static UserSessionManager instance;
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    private UserSessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    
    public static synchronized UserSessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserSessionManager(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * Create login session
     */
    public void createLoginSession(int userId, String username, boolean isGuest) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_GUEST, isGuest);
        editor.commit();
    }
    
    /**
     * Check login status
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * Get stored session data
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }
    
    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }
    
    public boolean isGuest() {
        return pref.getBoolean(KEY_IS_GUEST, true);
    }
    
    /**
     * Clear session details
     */
    public void logout() {
        editor.clear();
        editor.commit();
    }
}
