package uk.co.transferx.app.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import uk.co.transferx.app.dto.RecipientDto

@Parcelize
data class TransactionCreate(
    @SerializedName("recipient_id") val recipientId: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("currencyInput") val currencyInput: String,
    @SerializedName("card_id") val cardId: String,
    @SerializedName("message") val message: String?,
    @SerializedName("send_now") val sendNow: Boolean,
    var baseAmount: Int?,
    @SerializedName("repeat") val repeat: Boolean,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("end_time") val endTime: String?,
    @SerializedName("frequency") val frequency: String?,
    var card: Card?,
    var recipientDto: RecipientDto?,
    var transactionNumber: String?,
    var status: String?
) : Parcelable