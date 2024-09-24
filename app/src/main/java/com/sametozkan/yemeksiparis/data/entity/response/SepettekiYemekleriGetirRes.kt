package com.sametozkan.yemeksiparis.data.entity.response

data class SepettekiYemekleriGetirRes(
    val sepet_yemekler: List<SepetYemekRes>?,
    val success: Int
)
