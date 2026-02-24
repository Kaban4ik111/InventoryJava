package com.example.inventory;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
    }

    public void saveUserId(int userId) {
        prefs.edit().putInt("user_id", userId).apply();
    }

    public int getUserId() {
        return prefs.getInt("user_id", -1);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }
}