package com.sametozkan.yemeksiparis.uix.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sametozkan.yemeksiparis.R
import com.sametozkan.yemeksiparis.data.entity.request.SepetYemekReq
import com.sametozkan.yemeksiparis.data.entity.request.SepettekiYemekleriGetirReq
import com.sametozkan.yemeksiparis.data.entity.response.SepetYemekRes
import com.sametozkan.yemeksiparis.data.entity.response.YemekRes
import com.sametozkan.yemeksiparis.retrofit.ApiUtils
import com.sametozkan.yemeksiparis.ui.theme.CardContainerColor
import com.sametozkan.yemeksiparis.ui.theme.DarkGreen
import com.sametozkan.yemeksiparis.uix.viewmodel.UrunDetayViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrunDetay(
    navController: NavController,
    yemek: YemekRes,
    urunDetayViewModel: UrunDetayViewModel
) {

    var adet by remember {
        mutableStateOf(1)
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = yemek.yemek_adi) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(150.dp))
                    Card(
                        modifier = Modifier
                            .width(300.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardContainerColor
                        ),
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ) {
                        Spacer(modifier = Modifier.height(150.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom = 20.dp,
                                        start = 10.dp,
                                        end = 10.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(onClick = {
                                    if (adet > 1)
                                        adet -= 1
                                }) {
                                    Text(text = "-", fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "${adet}",
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                OutlinedButton(onClick = { adet += 1 }) {
                                    Text(text = "+", fontWeight = FontWeight.Bold)
                                }
                            }

                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = yemek.yemek_adi,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = "Birim Fiyatı: ${yemek.yemek_fiyat} ₺",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            HorizontalDivider(modifier = Modifier.padding(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${yemek.yemek_fiyat * adet} ₺",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkGreen
                                )
                                Button(
                                    onClick = {
                                        val sepetYemekRes = SepetYemekReq(
                                            yemek.yemek_adi,
                                            yemek.yemek_resim_adi,
                                            yemek.yemek_fiyat,
                                            adet,
                                            "sametozkan"
                                        )
                                        val sepeteEklendiMi: (Boolean) -> Unit = { sonuc ->
                                            if (sonuc) {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Sepete eklendi!")
                                                }
                                            } else {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Sepete eklenemedi!")
                                                }
                                            }
                                        }
                                        urunDetayViewModel.sepeteYemekEkle(
                                            sepetYemekRes,
                                            SepettekiYemekleriGetirReq("sametozkan"),
                                            sepeteEklendiMi
                                        )
                                    }) {
                                    Text(text = "Sepete Ekle", fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }

                GlideImage(
                    imageModel = ApiUtils.getImageUrl(yemek.yemek_resim_adi),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                        .align(Alignment.TopCenter)
                )

            }
        }
    }
}