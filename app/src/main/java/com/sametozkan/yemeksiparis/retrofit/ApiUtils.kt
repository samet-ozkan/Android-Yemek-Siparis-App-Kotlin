package com.sametozkan.yemeksiparis.retrofit

class ApiUtils {
    companion object{
        val BASE_URL = "http://kasimadalan.pe.hu"

        fun getKisilerDao() : YemekSiparisDao{
            return RetrofitClient.getClient(BASE_URL).create(YemekSiparisDao::class.java)
        }

        fun getImageUrl(yemekResimAdi: String): String{
            return "${BASE_URL}/yemekler/resimler/${yemekResimAdi}"
        }
    }
}