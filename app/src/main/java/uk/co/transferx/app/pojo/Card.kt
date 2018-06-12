package uk.co.transferx.app.pojo

import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String,
    @SerializedName("number") val number: String,
    @SerializedName("type") val type: String,
    @SerializedName("expDate") val expDate: String,
    @SerializedName("cvc") val cvc: String
) {
    override fun toString(): String {
        return "Ending in ${(number.takeLast(4))}"
    }
}
