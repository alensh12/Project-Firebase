package com.example.amprime.firebaseauth.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CoinList {
    @SerializedName("Response")
    @Expose
    var response: String? = null
    @SerializedName("Message")
    @Expose
    var message: String? = null
    @SerializedName("BaseImageUrl")
    @Expose
    var baseImageUrl: String? = null
    @SerializedName("BaseLinkUrl")
    @Expose
    var baseLinkUrl: String? = null
    @SerializedName("Data")
    @Expose
    var data: Map<String, CryptoData>? = null
    @SerializedName("Type")
    @Expose
    var type: Int? = null

    inner class CryptoData {
        @SerializedName("Id")
        @Expose
        var id: Long? = null
        @SerializedName("Url")
        @Expose
        var url: String? = null
        @SerializedName("ImageUrl")
        @Expose
        var imageUrl: String? = null
        @SerializedName("Name")
        @Expose
        var name: String? = null
        @SerializedName("Symbol")
        @Expose
        var symbol: String? = null
        @SerializedName("CoinName")
        @Expose
        var coinName: String? = null
        @SerializedName("FullName")
        @Expose
        var fullName: String? = null
        @SerializedName("Algorithm")
        @Expose
        var algorithm: String? = null
        @SerializedName("ProofType")
        @Expose
        var proofType: String? = null
        @SerializedName("FullyPremined")
        @Expose
        var fullyPremined: String? = null
        @SerializedName("TotalCoinSupply")
        @Expose
        var totalCoinSupply: String? = null
        @SerializedName("PreMinedValue")
        @Expose
        var preMinedValue: String? = null
        @SerializedName("TotalCoinsFreeFloat")
        @Expose
        var totalCoinsFreeFloat: String? = null
        @SerializedName("SortOrder")
        @Expose
        var sortOrder: String? = null
        @SerializedName("Sponsored")
        @Expose
        var sponsored: Boolean? = null
    }
}