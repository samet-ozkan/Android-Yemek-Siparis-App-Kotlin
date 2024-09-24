package com.sametozkan.yemeksiparis.uix.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametozkan.yemeksiparis.data.entity.request.SepetYemekReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettekiYemekleriGetirReq
import com.sametozkan.yemeksiparis.data.repo.YemekSiparisRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UrunDetayViewModel @Inject constructor(val yemekSiparisRepo: YemekSiparisRepo) : ViewModel() {

    companion object {
        private val TAG = "UrunDetayViewModel"
    }

    fun sepeteYemekEkle(
        sepetYemekReq: SepetYemekReq,
        sepettekiYemekleriGetirReq: SepettekiYemekleriGetirReq,
        sepeteEklendiMi: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val sepettekiYemekleriGetirRes =
                yemekSiparisRepo.sepettekiYemekleriGetir(sepettekiYemekleriGetirReq)
            if (!sepettekiYemekleriGetirRes.sepet_yemekler.isNullOrEmpty()) {
                val yemek =
                    sepettekiYemekleriGetirRes.sepet_yemekler!!.find { it.yemek_adi == sepetYemekReq.yemek_adi }
                yemek?.let {
                    val adetArttirildiMi = yemekSiparisRepo.adetGuncelle(
                        sepetYemekReq.yemek_adi,
                        sepetYemekReq.kullanici_adi,
                        sepetYemekReq.yemek_siparis_adet
                    )
                    if (adetArttirildiMi) {
                        Log.i(TAG, "sepeteYemekEkle: Adet arttırma başarılı!")
                        sepeteEklendiMi(true)
                    } else {
                        Log.e(TAG, "sepeteYemekEkle: Adet arttırma başarısız!")
                        sepeteEklendiMi(false)
                    }
                    return@launch
                }
            }
            val sepetYemekRes = yemekSiparisRepo.sepeteYemekEkle(sepetYemekReq)
            if (sepetYemekRes.success == 1) {
                Log.i(TAG, "sepeteYemekEkle: Sepete yemek ekleme başarılı!")
                sepeteEklendiMi(true)
            } else {
                Log.e(TAG, "sepeteYemekEkle: ${sepetYemekRes.message}")
                sepeteEklendiMi(false)
            }
        }
    }

}