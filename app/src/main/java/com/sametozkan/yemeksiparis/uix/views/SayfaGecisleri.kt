package com.sametozkan.yemeksiparis.uix.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.sametozkan.yemeksiparis.data.entity.response.YemekRes
import com.sametozkan.yemeksiparis.uix.viewmodel.AnasayfaViewModel
import com.sametozkan.yemeksiparis.uix.viewmodel.SepetViewModel
import com.sametozkan.yemeksiparis.uix.viewmodel.UrunDetayViewModel

@Composable
fun SayfaGecisleri(
    anasayfaViewModel: AnasayfaViewModel,
    urunDetayViewModel: UrunDetayViewModel,
    sepetViewModel: SepetViewModel
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "anasayfa") {
        composable("anasayfa") {
            Anasayfa(navController, anasayfaViewModel)
        }
        composable("urunDetay/{yemek}", arguments = listOf(
            navArgument("yemek") {
                type = NavType.StringType
            }
        )) {
            val json = it.arguments?.getString("yemek")
            val yemek = Gson().fromJson(json, YemekRes::class.java)
            UrunDetay(
                yemek = yemek,
                urunDetayViewModel = urunDetayViewModel,
                navController = navController
            )
        }
        composable("sepet") {
            Sepet(navController = navController, sepetViewModel = sepetViewModel)
        }
    }
}