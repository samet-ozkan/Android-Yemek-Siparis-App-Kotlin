package com.sametozkan.yemeksiparis.uix.views

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import com.sametozkan.yemeksiparis.MainActivity
import com.sametozkan.yemeksiparis.R
import com.sametozkan.yemeksiparis.data.entity.request.SepetYemekReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettekiYemekleriGetirReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettenYemekSilReq
import com.sametozkan.yemeksiparis.data.entity.response.SepetYemekRes
import com.sametozkan.yemeksiparis.data.entity.response.YemekRes
import com.sametozkan.yemeksiparis.retrofit.ApiUtils
import com.sametozkan.yemeksiparis.ui.theme.CardContainerColor
import com.sametozkan.yemeksiparis.uix.viewmodel.SepetViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sepet(navController: NavController, sepetViewModel: SepetViewModel) {
    var sepet = sepetViewModel.sepet.observeAsState(listOf())

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        sepetViewModel.sepettekiYemekleriGetir(SepettekiYemekleriGetirReq("sametozkan"))
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Sepet") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    },
        snackbarHost = { snackbarHostState }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.weight(5f)) {
                items(sepet.value.size) { index ->
                    SepetYemekCard(
                        sepetYemekRes = sepet.value[index], sepetViewModel = sepetViewModel
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                var toplamTutar = 0
                sepet.value.map { toplamTutar += (it.yemek_fiyat * it.yemek_siparis_adet) }
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Toplam Tutar: ${toplamTutar} ₺",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (toplamTutar != 0) {
                            sepetViewModel.sepetiOnayla { sonuc ->
                                if (sonuc) {
                                    sepetViewModel.bildirimOlustur((1..10000).random())
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Sepet onaylanamadı!")
                                    }
                                }
                            }
                        }
                    }) {
                    Text(text = "Sepeti Onayla", fontSize = 18.sp)
                }
            }

        }
    }

}

@Composable
fun SepetYemekCard(sepetYemekRes: SepetYemekRes, sepetViewModel: SepetViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardContainerColor
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .weight(3f),
                imageModel = ApiUtils.getImageUrl(sepetYemekRes.yemek_resim_adi)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(6f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = sepetYemekRes.yemek_adi, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Fiyat: ${sepetYemekRes.yemek_fiyat * sepetYemekRes.yemek_siparis_adet} ₺",
                    fontSize = 16.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedIconButton(modifier = Modifier.size(24.dp), onClick = {
                        if (sepetYemekRes.yemek_siparis_adet > 1) {
                            sepetViewModel.sepettekiYemegiGuncelle(
                                sepetYemekRes.yemek_adi, sepetYemekRes.kullanici_adi, -1
                            )
                        }
                    }) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                            contentDescription = "Down"
                        )
                    }

                    Text(
                        text = "${sepetYemekRes.yemek_siparis_adet}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    OutlinedIconButton(modifier = Modifier.size(24.dp), onClick = {
                        sepetViewModel.sepettekiYemegiGuncelle(
                            sepetYemekRes.yemek_adi, sepetYemekRes.kullanici_adi, 1
                        )
                    }) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                            contentDescription = "Up"
                        )
                    }
                }
            }
            IconButton(modifier = Modifier.weight(1f), onClick = {
                sepetViewModel.sepettenYemekSil(
                    SepettenYemekSilReq(
                        sepetYemekRes.sepet_yemek_id, sepetYemekRes.kullanici_adi
                    )
                )
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = "Delete"
                )
            }
        }
    }

}