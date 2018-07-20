package uk.co.transferx.app.pojo

import com.google.gson.annotations.SerializedName

data class TokenEntity(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expiresIn") val expiresIn: Long,
    @SerializedName("created") val created: Long,
    @SerializedName("refresh_token") val refreshToken: String?
)