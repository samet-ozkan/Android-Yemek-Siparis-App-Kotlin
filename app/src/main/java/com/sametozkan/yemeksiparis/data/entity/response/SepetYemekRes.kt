package com.sametozkan.yemeksiparis.data.entity.response

data class SepetYemekRes(
    val sepet_yemek_id: Int,
    val yemek_adi: String,
    val yemek_resim_adi: String,
    val yemek_fiyat: Int,
    val yemek_siparis_adet: Int,
    val kullanici_adi: String
)
