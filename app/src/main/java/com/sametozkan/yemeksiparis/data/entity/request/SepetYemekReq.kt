package com.sametozkan.yemeksiparis.data.entity.request

data class SepetYemekReq(
    val yemek_adi: String,
    val yemek_resim_adi: String,
    val yemek_fiyat: Int,
    val yemek_siparis_adet: Int,
    val kullanici_adi: String
)

fun SepetYemekReq.toMap(): Map<String, String> {
    return mapOf(
        "yemek_adi" to yemek_adi,
        "yemek_resim_adi" to yemek_resim_adi,
        "yemek_fiyat" to yemek_fiyat.toString(),
        "yemek_siparis_adet" to yemek_siparis_adet.toString(),
        "kullanici_adi" to kullanici_adi
    )
}