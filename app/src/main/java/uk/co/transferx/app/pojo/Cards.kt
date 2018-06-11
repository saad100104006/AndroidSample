package uk.co.transferx.app.pojo

import com.google.gson.annotations.SerializedName

data class Cards(@SerializedName("cards") var cards: List<Card> = emptyList())