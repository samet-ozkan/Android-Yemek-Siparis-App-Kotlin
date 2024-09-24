package com.sametozkan.yemeksiparis.data.datasource

import com.sametozkan.yemeksiparis.data.entity.request.SepetYemekReq
import com.sametozkan.yemeksiparis.data.entity.response.SepetYemekRes
import com.sametozkan.yemeksiparis.data.entity.request.SepettekiYemekleriGetirReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettenYemekSilReq
import com.sametozkan.yemeksiparis.data.entity.request.toMap
import com.sametozkan.yemeksiparis.data.entity.response.SepeteYemekEkleRes
import com.sametozkan.yemeksiparis.data.entity.response.SepettekiYemekleriGetirRes
import com.sametozkan.yemeksiparis.data.entity.response.SepettenYemekSilRes
import com.sametozkan.yemeksiparis.data.entity.response.TumYemeklerRes
import com.sametozkan.yemeksiparis.retrofit.YemekSiparisDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YemekSiparisDataSource @Inject constructor(val yemekSiparisDao: YemekSiparisDao) {

    suspend fun tumYemekleriGetir(): TumYemeklerRes = withContext(Dispatchers.IO) {
        return@withContext yemekSiparisDao.tumYemekleriGetir()
    }

    suspend fun sepeteYemekEkle(sepetYemekReq: SepetYemekReq)
            : SepeteYemekEkleRes = withContext(Dispatchers.IO) {
        return@withContext yemekSiparisDao.sepeteYemekEkle(sepetYemekReq.toMap())
    }

    suspend fun sepettekiYemekleriGetir(sepettekiYemekleriGetirReq: SepettekiYemekleriGetirReq)
            : SepettekiYemekleriGetirRes = withContext(Dispatchers.IO) {
        return@withContext yemekSiparisDao.sepettekiYemekleriGetir(sepettekiYemekleriGetirReq.toMap())
    }

    suspend fun sepettenYemekSil(sepettenYemekSilReq: SepettenYemekSilReq)
            : SepettenYemekSilRes = withContext(Dispatchers.IO) {
        return@withContext yemekSiparisDao.sepettenYemekSil(sepettenYemekSilReq.toMap())
    }

}