package com.sametozkan.yemeksiparis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sametozkan.yemeksiparis.ui.theme.YemekSiparisTheme
import com.sametozkan.yemeksiparis.uix.viewmodel.AnasayfaViewModel
import com.sametozkan.yemeksiparis.uix.viewmodel.SepetViewModel
import com.sametozkan.yemeksiparis.uix.viewmodel.UrunDetayViewModel
import com.sametozkan.yemeksiparis.uix.views.SayfaGecisleri
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val anasayfaViewModel: AnasayfaViewModel by viewModels()
    val urunDetayViewModel: UrunDetayViewModel by viewModels()
    val sepetViewModel: SepetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YemekSiparisTheme {
                SayfaGecisleri(
                    anasayfaViewModel = anasayfaViewModel,
                    urunDetayViewModel = urunDetayViewModel,
                    sepetViewModel = sepetViewModel
                )
            }
        }
    }
}