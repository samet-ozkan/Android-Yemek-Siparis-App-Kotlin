package com.sametozkan.yemeksiparis.retrofit

import com.sametozkan.yemeksiparis.data.entity.request.SepetYemekReq
import com.sametozkan.yemeksiparis.data.entity.response.SepetYemekRes
import com.sametozkan.yemeksiparis.data.entity.response.SepeteYemekEkleRes
import com.sametozkan.yemeksiparis.data.entity.request.SepettekiYemekleriGetirReq
import com.sametozkan.yemeksiparis.data.entity.response.SepettekiYemekleriGetirRes
import com.sametozkan.yemeksiparis.data.entity.request.SepettenYemekSilReq
import com.sametozkan.yemeksiparis.data.entity.response.SepettenYemekSilRes
import com.sametozkan.yemeksiparis.data.entity.response.TumYemeklerRes
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface YemekSiparisDao {

    @GET("/yemekler/tumYemekleriGetir.php")
    suspend fun tumYemekleriGetir(): TumYemeklerRes

    @FormUrlEncoded
    @POST("/yemekler/sepeteYemekEkle.php")
    suspend fun sepeteYemekEkle(@FieldMap sepetYemekFields: Map<String, String>): SepeteYemekEkleRes

    @FormUrlEncoded
    @POST("/yemekler/sepettekiYemekleriGetir.php")
    suspend fun sepettekiYemekleriGetir(@FieldMap sepettekiYemekleriGetirFields: Map<String, String>)
            : SepettekiYemekleriGetirRes

    @FormUrlEncoded
    @POST("/yemekler/sepettenYemekSil.php")
    suspend fun sepettenYemekSil(@FieldMap sepettenYemekSilFields: Map<String, String>) : SepettenYemekSilRes


}