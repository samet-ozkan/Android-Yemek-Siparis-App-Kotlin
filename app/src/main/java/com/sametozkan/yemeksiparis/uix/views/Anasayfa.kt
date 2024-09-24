package com.sametozkan.yemeksiparis.uix.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.google.gson.Gson
import com.sametozkan.yemeksiparis.R
import com.sametozkan.yemeksiparis.data.entity.response.YemekRes
import com.sametozkan.yemeksiparis.retrofit.ApiUtils
import com.sametozkan.yemeksiparis.ui.theme.CardContainerColor
import com.sametozkan.yemeksiparis.uix.viewmodel.AnasayfaViewModel
import com.sametozkan.yemeksiparis.utils.Siralama
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Anasayfa(navController: NavController, anasayfaViewModel: AnasayfaViewModel) {

    var yemeklerListesi = anasayfaViewModel.yemeklerListesi.observeAsState(listOf())
    var populerYemekler = anasayfaViewModel.populerYemekler.observeAsState(listOf())
    var siralama by remember {
        mutableStateOf(Siralama.NAME_ASCENDING)
    }
    var sortOrderDropdownMenuExpanded by remember {
        mutableStateOf(false)
    }
    var searchQuery by remember {
        mutableStateOf("")
    }
    var filteredYemeklerListesi: List<YemekRes> = ArrayList()

    Scaffold(topBar = {
        TopAppBar(title = {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Merhaba, ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Samet Özkan", fontSize = 18.sp)
                }
            }
        }, actions = {
            IconButton(onClick = { navController.navigate("sepet") }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_shopping_basket_24),
                    contentDescription = "Basket"
                )
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                    if (!yemeklerListesi.value.isNullOrEmpty()) {
                        filteredYemeklerListesi = yemeklerListesi.value.filter {
                            it.yemek_adi.contains(query, ignoreCase = true)
                        }
                    }
                },
                label = {
                    Text(text = "Ara")
                },
                trailingIcon = {
                    if (searchQuery == "") {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "Search"
                        )
                    } else {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Close"
                            )
                        }
                    }
                })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            ) {
                if (searchQuery != "") {
                    Text(text = "${filteredYemeklerListesi.size} adet sonuç bulundu.")
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredYemeklerListesi.size) { index ->
                            UrunlerYemekCard(
                                navController = navController,
                                yemek = filteredYemeklerListesi[index]
                            )
                        }
                    }
                } else {
                    //Text(text = "Popüler", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    LazyRow {
                        items(populerYemekler.value.size) { index ->
                            PopulerYemekCard(
                                navController = navController, yemek = populerYemekler.value[index]
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "Ürünler",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                OutlinedButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { sortOrderDropdownMenuExpanded = true },
                                ) {
                                    when (siralama) {
                                        Siralama.NAME_ASCENDING -> {
                                            Icon(
                                                modifier = Modifier.padding(end = 5.dp),
                                                painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                                                contentDescription = "Upward"
                                            )
                                            Text(text = "Ürün Adı (A-Z)")
                                        }

                                        Siralama.NAME_DESCENDING -> {
                                            Icon(
                                                modifier = Modifier.padding(end = 5.dp),
                                                painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                                                contentDescription = "Downward"
                                            )
                                            Text(text = "Ürün Adı (Z-A)")
                                        }

                                        Siralama.PRICE_ASCENDING -> {
                                            Icon(
                                                modifier = Modifier.padding(end = 5.dp),
                                                painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                                                contentDescription = "Upward"
                                            )
                                            Text(text = "Fiyat (Artan)")
                                        }

                                        Siralama.PRICE_DESCENDING -> {
                                            Icon(
                                                modifier = Modifier.padding(end = 5.dp),
                                                painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                                                contentDescription = "Downward"
                                            )
                                            Text(text = "Fiyat (Azalan)")
                                        }
                                    }
                                }
                                DropdownMenu(
                                    expanded = sortOrderDropdownMenuExpanded,
                                    onDismissRequest = { sortOrderDropdownMenuExpanded = false },
                                    properties = PopupProperties(focusable = true)
                                ) {
                                    DropdownMenuItem(text = { Text(text = "Ürün Adı (A-Z)") },
                                        onClick = {
                                            sortOrderDropdownMenuExpanded = false
                                            siralama = Siralama.NAME_ASCENDING
                                        })
                                    DropdownMenuItem(text = { Text(text = "Ürün Adı (Z-A)") },
                                        onClick = {
                                            sortOrderDropdownMenuExpanded = false
                                            siralama = Siralama.NAME_DESCENDING
                                        })
                                    DropdownMenuItem(text = { Text(text = "Fiyat (Artan)") },
                                        onClick = {
                                            sortOrderDropdownMenuExpanded = false
                                            siralama = Siralama.PRICE_ASCENDING
                                        })
                                    DropdownMenuItem(text = { Text(text = "Fiyat (Azalan)") },
                                        onClick = {
                                            sortOrderDropdownMenuExpanded = false
                                            siralama = Siralama.PRICE_DESCENDING
                                        })
                                }
                            }
                        }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()
                        ) {
                            var sortedYemeklerListesi = yemeklerListesi.value
                            when (siralama) {
                                Siralama.NAME_ASCENDING -> sortedYemeklerListesi =
                                    sortedYemeklerListesi.sortedBy { it.yemek_adi }

                                Siralama.NAME_DESCENDING -> sortedYemeklerListesi =
                                    sortedYemeklerListesi.sortedByDescending { it.yemek_adi }

                                Siralama.PRICE_ASCENDING -> sortedYemeklerListesi =
                                    sortedYemeklerListesi.sortedBy { it.yemek_fiyat }

                                Siralama.PRICE_DESCENDING -> sortedYemeklerListesi =
                                    sortedYemeklerListesi.sortedByDescending { it.yemek_fiyat }
                            }
                            items(sortedYemeklerListesi.size) { index ->
                                UrunlerYemekCard(navController, sortedYemeklerListesi[index])
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PopulerYemekCard(navController: NavController, yemek: YemekRes) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .wrapContentHeight()
            .padding(10.dp)
            .clickable {
                navigateToUrunDetay(navController, yemek)
            },
        colors = CardDefaults.cardColors(
            containerColor = CardContainerColor
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            GlideImage(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                imageModel = ApiUtils.getImageUrl(yemek.yemek_resim_adi)
            )
            Text(yemek.yemek_adi, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun UrunlerYemekCard(navController: NavController, yemek: YemekRes) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navigateToUrunDetay(navController, yemek)
            },
        colors = CardDefaults.cardColors(
            containerColor = CardContainerColor
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
                .padding(5.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            GlideImage(
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp),
                imageModel = ApiUtils.getImageUrl(yemek.yemek_resim_adi)
            )
            Text(yemek.yemek_adi, fontSize = 16.sp)
            Text("${yemek.yemek_fiyat} ₺", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }

}

private fun navigateToUrunDetay(navController: NavController, yemek: YemekRes) {
    val yemekJson = Gson().toJson(yemek)
    navController.navigate("urunDetay/$yemekJson")
}
