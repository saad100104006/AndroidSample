package uk.co.transferx.app.pojo

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import uk.co.transferx.app.util.Constants.SPACE

@Parcelize
data class Card(
    @Expose(serialize = false, deserialize = true) @SerializedName("id") val id: String?,
    @Expose(serialize = false, deserialize = true) @SerializedName("name") val name: String,
    @Expose(serialize = false, deserialize = true) @SerializedName("number") val number: String,
    @Expose(serialize = false, deserialize = true) @SerializedName("type") val type: String,
    @Expose(serialize = false, deserialize = true) @SerializedName("expDate") val expDate: String,
    @Expose(serialize = false, deserialize = true) @SerializedName("cvc") val cvc: String
) : Parcelable {
    override fun toString(): String {
        return "Ending in ${(number.takeLast(4))}"
    }

    fun getFormattedNumber(): String {

        val result = StringBuilder()
        for (i in 0 until number.length) {
            if (i % 4 == 0 && i != 0) {
                result.append(SPACE)
            }
            result.append(number.elementAt(i))
        }
        return result.toString()
    }
}
