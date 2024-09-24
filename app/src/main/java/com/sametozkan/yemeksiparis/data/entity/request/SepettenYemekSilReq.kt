package com.sametozkan.yemeksiparis.data.entity.request

data class SepettenYemekSilReq(
    val sepet_yemek_id: Int,
    val kullanici_adi: String
)

fun SepettenYemekSilReq.toMap(): Map<String, String> {
    return mapOf(
        "sepet_yemek_id" to sepet_yemek_id.toString(),
        "kullanici_adi" to kullanici_adi
    )
}
