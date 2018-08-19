package uk.co.transferx.app.pojo

import com.google.gson.annotations.SerializedName

data class UpdateFCMToken(
    @SerializedName("token") val token: String,
    @SerializedName("platform") val platform: String = "android"
)