package uk.co.transferx.app.data.pojo

import com.google.gson.annotations.SerializedName

data class ChangePassword(@SerializedName("current") val currentPass: String,
                          @SerializedName("new_upass") val newPass: String)
