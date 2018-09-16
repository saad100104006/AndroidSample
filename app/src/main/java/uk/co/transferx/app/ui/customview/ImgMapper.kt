package uk.co.transferx.app.ui.customview

import uk.co.transferx.app.R
import uk.co.transferx.app.util.Constants.MASTERCARD
import uk.co.transferx.app.util.Constants.VISA

class ImgMapper {

    fun mapNameToFlag(countryName: String): Int {
        return when (countryName) {
            "Uganda" -> R.drawable.uganda
            "Kenya" -> R.drawable.ic_kenya
            "Tanzania" -> R.drawable.ic_tanzania
            else -> throw IllegalStateException("Error Illegal value $countryName")
        }
    }

    fun mapCardType(cardType: String): Int {
        return when (cardType) {
            MASTERCARD -> R.drawable.ic_master_card
            VISA -> R.drawable.ic_visa
            else -> throw IllegalStateException("Error Illegal value $cardType")
        }
    }
}