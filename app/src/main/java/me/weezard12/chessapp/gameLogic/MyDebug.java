package me.weezard12.chessapp.gameLogic;

import android.util.Log;

public final class MyDebug {

    private static final boolean DEBUG_ENABLED = false;
    public static void log(String massage){
        if(DEBUG_ENABLED)
            Log.i("debug", massage);
    }
    public static void log(String from, String massage){
        if(DEBUG_ENABLED)
            Log.i("debug",from + ": " + massage);
    }


}
