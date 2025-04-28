package me.weezard12.chessapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelp extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chessApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_SETTINGS = "user_settings";

    // User table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Settings table columns
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_THEME_NAME = "theme_name";
    private static final String COLUMN_THEME_INDEX = "theme_index";
    private static final String COLUMN_VOLUME = "volume";

    // Create users table query
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL" +
                    ");";

    public DatabaseHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create settings table query
    private static final String CREATE_TABLE_SETTINGS =
            "CREATE TABLE " + TABLE_SETTINGS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_THEME_NAME + " TEXT, " +
                    COLUMN_THEME_INDEX + " INTEGER, " +
                    COLUMN_VOLUME + " REAL, " +
                    "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" +
                    ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add settings table in version 2
            db.execSQL(CREATE_TABLE_SETTINGS);
        }
        if (oldVersion < 3) {
            // Add theme_index column in version 3
            db.execSQL("ALTER TABLE " + TABLE_SETTINGS + " ADD COLUMN " + COLUMN_THEME_INDEX + " INTEGER DEFAULT 0");
        }
    }

    /**
     * Add a new user to the database
     * @return user ID if successful, -1 if failed
     */
    public long insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long userId = db.insert(TABLE_USERS, null, values);

        if (userId != -1) {
            // Create default settings for the new user
            createDefaultSettings(userId);
        }

        db.close();
        return userId;
    }

    /**
     * Create default settings for a new user
     */
    private void createDefaultSettings(long userId) {
        UserSettings defaultSettings = new UserSettings();
        defaultSettings.setUserId((int)userId);
        saveUserSettings(defaultSettings);
    }

    /**
     * Check if user exists and return user ID if found
     * @return user ID if found, -1 if not found
     */
    public int checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        }

        cursor.close();
        db.close();
        return userId;
    }

    /**
     * Check if username already exists
     */
    public boolean usernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    /**
     * Save user settings to database
     */
    public boolean saveUserSettings(UserSettings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, settings.getUserId());
        values.put(COLUMN_THEME_NAME, settings.getThemeName());
        values.put(COLUMN_THEME_INDEX, settings.getThemeIndex());
        values.put(COLUMN_VOLUME, settings.getVolume());

        // Check if settings already exist for this user
        Cursor cursor = db.query(TABLE_SETTINGS,
                new String[]{COLUMN_ID},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(settings.getUserId())},
                null, null, null);

        long result;
        if (cursor.moveToFirst()) {
            // Update existing settings
            int settingsId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            result = db.update(TABLE_SETTINGS, values,
                    COLUMN_ID + "=?",
                    new String[]{String.valueOf(settingsId)});
        } else {
            // Insert new settings
            result = db.insert(TABLE_SETTINGS, null, values);
        }

        cursor.close();
        db.close();
        return result != -1;
    }

    /**
     * Load user settings from database
     */
    public UserSettings getUserSettings(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        UserSettings settings = new UserSettings();
        settings.setUserId(userId);

        Cursor cursor = db.query(TABLE_SETTINGS,
                null, // all columns
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            settings.setThemeName(cursor.getString(cursor.getColumnIndex(COLUMN_THEME_NAME)));
            
            // Check if the theme_index column exists (for backward compatibility)
            int themeIndexColumnIndex = cursor.getColumnIndex(COLUMN_THEME_INDEX);
            if (themeIndexColumnIndex != -1) {
                settings.setThemeIndex(cursor.getInt(themeIndexColumnIndex));
            } else {
                // If column doesn't exist, set default index (0)
                settings.setThemeIndex(0);
            }
            
            settings.setVolume(cursor.getFloat(cursor.getColumnIndex(COLUMN_VOLUME)));
        }

        cursor.close();
        db.close();
        return settings;
    }
}