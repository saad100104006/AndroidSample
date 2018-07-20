package uk.co.transferx.app.tokenmanager

import android.content.SharedPreferences
import android.util.Log
import uk.co.transferx.app.pojo.TokenEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {


    fun saveToken(tokenEntity: TokenEntity) {
        val editableState = sharedPreferences.edit()
        editableState.putString(KEY_TOKEN, tokenEntity.accessToken)
        editableState.putString(KEY_REFRESH_TOKEN, tokenEntity.refreshToken ?: "")
        editableState.putLong(KEY_CREATED, tokenEntity.created)
        editableState.putLong(KEY_EXPIRES_IN, tokenEntity.expiresIn)
        editableState.apply()
        Log.d("Serge", "SaveToken " + tokenEntity.accessToken.takeLast(7))
    }

    fun getToken(): TokenEntity {
        Log.d("Serge", "ReadToken " + sharedPreferences.getString(KEY_TOKEN, "").takeLast(7))
        return TokenEntity(
            sharedPreferences.getString(KEY_TOKEN, ""),
            sharedPreferences.getLong(KEY_EXPIRES_IN, DEFAULT_TIMESTAMP),
            sharedPreferences.getLong(KEY_CREATED, DEFAULT_TIMESTAMP),
            sharedPreferences.getString(KEY_REFRESH_TOKEN, "")

        )
    }

    companion object {
        private const val KEY_TOKEN = "key:token"
        private const val KEY_REFRESH_TOKEN = "key:refresh_token"
        private const val KEY_CREATED = "key:crated"
        private const val KEY_EXPIRES_IN = "key:expires_in"
        private const val DEFAULT_TIMESTAMP = 0L
    }
}