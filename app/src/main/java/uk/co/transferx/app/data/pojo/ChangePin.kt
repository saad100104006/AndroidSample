package uk.co.transferx.app.data.pojo

import com.google.gson.annotations.SerializedName

data class ChangePin(@SerializedName("current") val currentPin: String,
                     @SerializedName("new_upin") val newPin: String)