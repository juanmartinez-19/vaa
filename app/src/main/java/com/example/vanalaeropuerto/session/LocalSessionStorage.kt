package com.example.vanalaeropuerto.session

import android.content.Context

class LocalSessionStorage(context: Context) {

    private val prefs = context.getSharedPreferences(
        "local_session",
        Context.MODE_PRIVATE
    )

    fun save(uid: String) {
        prefs.edit()
            .putString("uid", uid)
            .apply()
    }

    fun get(): SessionState.LoggedIn? {
        val uid = prefs.getString("uid", null) ?: return null

        return SessionState.LoggedIn(
            uid = uid
        )
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
