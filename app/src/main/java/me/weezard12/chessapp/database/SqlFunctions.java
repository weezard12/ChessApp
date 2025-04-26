package me.weezard12.chessapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * defines an SQLite database table of players' names and number of wins
 */
public class SqlFunctions extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "fourinarow.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "users";
    public static final String UID = "_id";                 // primary Key, automatic ID
    public static final String KEY_NAME = "name";           // name of the player
    public static final String SCORE = "score";               // number of wins
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME +
            " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + SCORE + " INTEGER,"
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_PASSWORD + " TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ TABLE_NAME;

    public SqlFunctions(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void addData(String name, int score)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(SCORE, score);
        boolean inserted =  db.insert(TABLE_NAME, null, cv)>0;

    }

    /**
     * @param name
     * @return
     */
    public boolean exist( String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_NAME +"= ? " , new String[]{name});

        if(c.moveToFirst()) {// if moveToFirst() returns false - c is empty
            c.close();
            db.close();
            return true;
        }
        c.close();
        return false;
    }

    /**
     * @param name points
     * @return
     */
    public boolean addToPlayerList(String name, int points)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_NAME +"= ? " , new String[]{name});

        /*  if the player exist in the database */
        if(c!=null && c.moveToFirst()) {// if moveToFirst() returns false - c is empty
            Log.d("in addToPlayerList", "exist");
            String d = c.getString(1);
            int score = c.getInt(2);
            score+=points;
            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, name);
            cv.put(SCORE, score);
            db.update(TABLE_NAME, cv, KEY_NAME+"= ? " , new String[]{name});
            c.close();
            db.close();
            return true;
        }
        /* adding a new player to the database*/
        else {
            this.addData(name,points);
        }
        c.close();
        return false;
    }


    public void remove( String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_NAME + "= ? " , new String[]{name});
    }

    // Registration: Insert a new user
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if username or email already exists
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_NAME + "=? OR " + COLUMN_EMAIL + "=?",
                new String[]{username, email});

        if (cursor.moveToFirst()) {
            // User with the given username or email already exists.
            cursor.close();
            db.close();
            return false; // or handle the error as needed
        }

        cursor.close();

        // If not, insert new record
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    // Login: Check if user exists with given email and password
    public boolean loginUserByUsername(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + KEY_NAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();
        return result;
    }



}
