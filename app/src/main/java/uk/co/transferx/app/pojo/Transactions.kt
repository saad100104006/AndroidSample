package uk.co.transferx.app.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transactions(@SerializedName("transactions") val transactions: List<Transaction>) :
    Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
data class Meta(
    @SerializedName("cardInfo") val cardInfo: CardInfo?,
    @SerializedName("recipientInfo") val recipientInfo: RecipientInfo?
) : Parcelable

@Parcelize
data class CardInfo(
    @SerializedName("expDate") val expDate: String,
    @SerializedName("number") val number: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String
) : Parcelable

@Parcelize
data class RecipientInfo(
    @SerializedName("firstname") val firstName: String,
    @SerializedName("lastname") val lastName: String,
    @SerializedName("country") val country: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("image_url") val imgUrl: String
) : Parcelable