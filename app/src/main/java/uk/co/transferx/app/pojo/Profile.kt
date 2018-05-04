package uk.co.transferx.app.pojo

import com.google.gson.annotations.SerializedName

data class Profile(@SerializedName("email") val email: String,
                   @SerializedName("isOrg") val isOrganization: Boolean,
                   @SerializedName("firstname") val firstName: String,
                   @SerializedName("lastname") val lastName: String,
                   @SerializedName("birthdate") val birthDate: String,
                   @SerializedName("phone") val phone: String,
                   @SerializedName("address_1") val addressOne: String,
                   @SerializedName("address_2") val addressTwo: String,
                   @SerializedName("city") val city: String,
                   @SerializedName("country") val country: String,
                   @SerializedName("postCode") val postCode: String)