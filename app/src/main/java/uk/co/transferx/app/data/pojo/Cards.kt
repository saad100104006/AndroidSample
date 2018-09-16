package uk.co.transferx.app.data.pojo

import com.google.gson.annotations.SerializedName

data class Cards(@SerializedName("cards") var cards: List<Card> = emptyList())