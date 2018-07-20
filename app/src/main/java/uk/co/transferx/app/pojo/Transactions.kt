package uk.co.transferx.app.pojo

import com.google.gson.annotations.SerializedName

data class Transactions(@SerializedName("transactions") val transactions: List<Transaction>)

data class Transaction(
    @SerializedName("id") val id: String,
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("created") val created: String,
    @SerializedName("updated") val updated: String,
    @SerializedName("repeat") val repeat: Boolean,
    @SerializedName("frequency") val frequency: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    @SerializedName("meta") val meta: Meta?
)

data class Meta(
    @SerializedName("cardInfo") val cardInfo: CardInfo?,
    @SerializedName("recipientInfo") val recipientInfo: RecipientInfo?
)

data class CardInfo(
    @SerializedName("expDate") val expDate: String,
    @SerializedName("number") val number: String,
    @SerializedName("name") val name: String
)

data class RecipientInfo(
    @SerializedName("firstname") val firstName: String,
    @SerializedName("lastname") val lastName: String,
    @SerializedName("country") val country: String,
    @SerializedName("phone") val phone: String
)