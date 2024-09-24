package com.sametozkan.yemeksiparis.uix.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sametozkan.yemeksiparis.data.entity.response.YemekRes
import com.sametozkan.yemeksiparis.data.repo.YemekSiparisRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnasayfaViewModel @Inject constructor(var yemekSiparisRepo: YemekSiparisRepo) : ViewModel() {

    companion object {
        private val TAG = "AnasayfaViewModel"
    }

    private val _yemeklerListesi = MutableLiveData<List<YemekRes>>()
    private val _populerYemekler = MutableLiveData<List<YemekRes>>()
    val yemeklerListesi: LiveData<List<YemekRes>>
        get() = _yemeklerListesi
    val populerYemekler: LiveData<List<YemekRes>>
        get() = _populerYemekler

    init {
        tumYemekleriGetir()
    }

    fun tumYemekleriGetir() {
        viewModelScope.launch(Dispatchers.IO) {
            val tumYemeklerRes = yemekSiparisRepo.tumYemekleriGetir()
            if (tumYemeklerRes.success == 1) {
                _yemeklerListesi.postValue(tumYemeklerRes.yemekler)
                if (_populerYemekler.value == null || _populerYemekler.value!!.isEmpty()) {
                    _populerYemekler.postValue(tumYemeklerRes.yemekler.shuffled().take(8))
                }
            } else {
                Log.e(TAG, "tumYemekleriGetir: Web service response başarısız oldu.")
            }
        }
    }
}