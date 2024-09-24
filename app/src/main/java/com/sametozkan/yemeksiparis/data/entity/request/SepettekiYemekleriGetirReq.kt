package com.sametozkan.yemeksiparis.data.entity.request

data class SepettekiYemekleriGetirReq(
    val kullanici_adi : String
)

fun SepettekiYemekleriGetirReq.toMap(): Map<String, String> {
    return mapOf(
        "kullanici_adi" to kullanici_adi
    )
}
