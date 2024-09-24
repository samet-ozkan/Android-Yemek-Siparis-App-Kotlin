package com.sametozkan.yemeksiparis.uix.viewmodel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametozkan.yemeksiparis.MainActivity
import com.sametozkan.yemeksiparis.R
import com.sametozkan.yemeksiparis.data.entity.request.SepetYemekReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettekiYemekleriGetirReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettenYemekSilReq
import com.sametozkan.yemeksiparis.data.entity.response.SepetYemekRes
import com.sametozkan.yemeksiparis.data.repo.YemekSiparisRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SepetViewModel @Inject constructor(
    val yemekSiparisRepo: YemekSiparisRepo,
    @ApplicationContext val applicationContext: Context
) : ViewModel() {

    companion object {
        private val TAG = "SepetViewModel"
    }

    private val _sepet = MutableLiveData<List<SepetYemekRes>>()
    val sepet: LiveData<List<SepetYemekRes>>
        get() = _sepet

    init {
        sepettekiYemekleriGetir(SepettekiYemekleriGetirReq("sametozkan"))
    }

    fun sepettekiYemekleriGetir(sepettekiYemekleriGetirReq: SepettekiYemekleriGetirReq) {
        viewModelScope.launch(Dispatchers.IO) {
            val sepettekiYemekleriGetirRes =
                yemekSiparisRepo.sepettekiYemekleriGetir(sepettekiYemekleriGetirReq)
            if (sepettekiYemekleriGetirRes.success == 1) {
                if (!sepettekiYemekleriGetirRes.sepet_yemekler.isNullOrEmpty()) {
                    _sepet.postValue(sepettekiYemekleriGetirRes.sepet_yemekler.sortedBy { it.yemek_adi })
                } else {
                    _sepet.postValue(ArrayList())
                }
                Log.i(TAG, "sepettekiYemekleriGetir: Sepetteki yemekleri getirme başarılı!")
            } else {
                Log.e(TAG, "sepettekiYemekleriGetir: Sepetteki yemekleri getirme başarısız!")
            }
        }
    }

    fun sepettekiYemegiGuncelle(
        yemekAdi: String, kullaniciAdi: String, adetiDegistir: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val guncellendiMi =
                yemekSiparisRepo.adetGuncelle(yemekAdi, kullaniciAdi, adetiDegistir)
            if (guncellendiMi) {
                Log.i(TAG, "sepettekiYemegiGuncelle: Güncelleme işlemi başarılı!")
                sepettekiYemekleriGetir(SepettekiYemekleriGetirReq("sametozkan"))
            } else
                Log.e(TAG, "sepettekiYemegiGuncelle: Güncelleme işlemi başarısız!")
        }
    }

    fun sepettenYemekSil(sepettenYemekSilReq: SepettenYemekSilReq) {
        viewModelScope.launch(Dispatchers.IO) {
            val sepettenYemekSilRes = yemekSiparisRepo.sepettenYemekSil(sepettenYemekSilReq)
            if (sepettenYemekSilRes.success == 1) {
                Log.i(TAG, "sepettenYemekSil: Sepetten yemek silme başarılı!")
                sepettekiYemekleriGetir(SepettekiYemekleriGetirReq("sametozkan"))
            } else {
                Log.e(TAG, "sepettenYemekSil: ${sepettenYemekSilRes.message}")
            }
        }
    }

    fun bildirimOlustur(siparisId: Int) {
        val builder: NotificationCompat.Builder
        val bildirimYoneticisi =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(applicationContext, MainActivity::class.java)
        val gidilecekIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val kanalId = "1"
            val kanalAd = "siparisOnayKanali"
            val kanalAciklama = "Sipariş onaylandığında gönderilecek olan bildirim."
            val kanalOnceligi = NotificationManager.IMPORTANCE_HIGH
            var kanal: NotificationChannel? = bildirimYoneticisi.getNotificationChannel(kanalId)
            if (kanal == null) {
                kanal = NotificationChannel(kanalId, kanalAd, kanalOnceligi)
                kanal.description = kanalAciklama
                bildirimYoneticisi.createNotificationChannel(kanal)
            }
            builder = NotificationCompat.Builder(applicationContext, kanalId)
            builder.setContentTitle("Siparişiniz Onaylandı!")
                .setContentText(
                    "${siparisId} numaralı siparişiniz onaylandı. "
                )
                .setSmallIcon(R.drawable.baseline_shopping_basket_24)
                .setContentIntent(gidilecekIntent)
                .setAutoCancel(true)
        } else {
            builder = NotificationCompat.Builder(applicationContext)
            builder.setContentTitle("Siparişiniz Onaylandı!")
                .setContentText(
                    "${siparisId} numaralı siparişiniz onaylandı. "
                )
                .setSmallIcon(R.drawable.baseline_shopping_basket_24)
                .setContentIntent(gidilecekIntent).setAutoCancel(true).priority =
                Notification.PRIORITY_HIGH
        }
        bildirimYoneticisi.notify(1, builder.build())
    }

    fun sepetiOnayla(sepetOnaylandiMi: (Boolean) -> Unit) {
        if (!_sepet.value.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val sepetiSil = _sepet.value!!.map { sepetYemekRes ->
                    async {
                        sepettenYemekSil(
                            SepettenYemekSilReq(
                                sepetYemekRes.sepet_yemek_id,
                                "sametozkan"
                            )
                        )
                    }
                }
                try {
                    sepetiSil.awaitAll()
                    sepetOnaylandiMi(true)
                } catch (exception: Exception) {
                    sepetOnaylandiMi(false)
                }

            }
        }
    }
}