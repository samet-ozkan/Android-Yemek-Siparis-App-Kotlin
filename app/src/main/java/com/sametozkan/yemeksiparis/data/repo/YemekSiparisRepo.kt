package com.sametozkan.yemeksiparis.data.repo

import android.util.Log
import com.sametozkan.yemeksiparis.data.datasource.YemekSiparisDataSource
import com.sametozkan.yemeksiparis.data.entity.request.SepetYemekReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettekiYemekleriGetirReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettenYemekSilReq
import com.sametozkan.yemeksiparis.data.entity.response.SepetYemekRes
import com.sametozkan.yemeksiparis.data.entity.response.SepeteYemekEkleRes
import com.sametozkan.yemeksiparis.data.entity.response.SepettekiYemekleriGetirRes
import com.sametozkan.yemeksiparis.data.entity.response.SepettenYemekSilRes
import com.sametozkan.yemeksiparis.data.entity.response.TumYemeklerRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YemekSiparisRepo @Inject constructor(val yemekSiparisDataSource: YemekSiparisDataSource) {

    companion object {
        private val TAG = "YemekSiparisRepo"
    }

    val mutex = Mutex()

    suspend fun tumYemekleriGetir(): TumYemeklerRes = yemekSiparisDataSource.tumYemekleriGetir()


    suspend fun sepeteYemekEkle(sepetYemekReq: SepetYemekReq): SepeteYemekEkleRes =
        yemekSiparisDataSource.sepeteYemekEkle(sepetYemekReq)

    suspend fun sepettekiYemekleriGetir(sepettekiYemekleriGetirReq: SepettekiYemekleriGetirReq): SepettekiYemekleriGetirRes {
        try {
            return yemekSiparisDataSource.sepettekiYemekleriGetir(sepettekiYemekleriGetirReq)
        } catch (e: Exception) {
            Log.e(TAG, "sepettekiYemekleriGetir: Hata")
        }
        return SepettekiYemekleriGetirRes(null, 1)
    }

    suspend fun sepettenYemekSil(sepettenYemekSilReq: SepettenYemekSilReq): SepettenYemekSilRes =
        yemekSiparisDataSource.sepettenYemekSil(sepettenYemekSilReq)

    suspend fun adetGuncelle(
        yemekAdi: String, kullaniciAdi: String, adetiDegistir: Int
    ): Boolean {
        mutex.lock()
        val sepettekiYemekleriGetirRes =
            sepettekiYemekleriGetir(SepettekiYemekleriGetirReq(kullaniciAdi))
        val sepetYemek =
            sepettekiYemekleriGetirRes.sepet_yemekler?.find { it.yemek_adi == yemekAdi }!!
        val result = withContext(Dispatchers.IO) {
            val sepettenYemekSilRes = async {
                sepettenYemekSil(
                    SepettenYemekSilReq(
                        sepetYemek.sepet_yemek_id, kullaniciAdi
                    )
                )
            }.await()
            if (sepettenYemekSilRes.success == 1) {
                Log.i(TAG, "sepettekiYemegiGuncelle: Sepetten yemek silme başarılı!")
                if(sepetYemek.yemek_siparis_adet == 1 && adetiDegistir < 0){
                    Log.e(TAG, "adetGuncelle: Adet 1'den küçük olamaz!", )
                    return@withContext true
                }
                val sepeteYemekEkleRes = async {
                    sepeteYemekEkle(
                        SepetYemekReq(
                            sepetYemek.yemek_adi,
                            sepetYemek.yemek_resim_adi,
                            sepetYemek.yemek_fiyat,
                            sepetYemek.yemek_siparis_adet + adetiDegistir,
                            kullaniciAdi
                        )
                    )
                }.await()
                if (sepeteYemekEkleRes.success == 1) {
                    Log.i(TAG, "sepettekiYemegiGuncelle: Sepete yemek ekleme başarılı!")
                    true
                } else {
                    Log.e(TAG, "sepettekiYemegiGuncelle: ${sepeteYemekEkleRes.message}")
                    false
                }
            } else {
                Log.e(TAG, "sepettekiYemegiGuncelle: ${sepettenYemekSilRes.message}")
                false
            }
        }
        mutex.unlock()
        return result
    }
}