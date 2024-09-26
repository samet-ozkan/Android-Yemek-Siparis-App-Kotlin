# Android-Yemek-Siparis-App-Kotlin
Android Jetpack Compose Bootcamp Bitirme Projesi

## Özellikler
* Ürünleri arama
* Ürünleri sıralama
* Sepette ve detay sayfasında ürün adeti belirleme
* Ürünü sepete ekleme
* Ürünü sepetten silme
* Sipariş onaylama

## Kullanılan Teknolojiler
* MVVM
* Retrofit
* Glide
* Dagger Hilt
* WorkerManager
* Coroutines
* LiveData
* Compose UI

## Uygulama Videosu
https://github.com/user-attachments/assets/1db4a605-2ed6-465a-a815-50a3aa8489d9

## WebService Yapısı
### GET: Tüm Yemekleri Getir
```json
{
  "yemekler": [
    {
      "yemek_id": "1",
      "yemek_adi": "Ayran",
      "yemek_resim_adi": "ayran.png",
      "yemek_fiyat": "10"
    },
    {
    "yemek_id": "2",
    "yemek_adi": "Su",
    "yemek_resim_adi": "su.png",
    "yemek_fiyat": "5"
    }
  ],
  "success": "1"
}
```

### POST: Sepete Yemek Ekleme
#### Parametreler
* yemek_adi: String
* yemek_resim_adi: String
* yemek_fiyat: Int
* yemek_siparis_adet: Int
* kullanici_adi: String
```json
{
  "success": 0,
  "message": "Required field(s) is missing"
}
```

### POST: Sepetteki Yemekleri Getir
#### Parametreler
* kullanici_adi: String
```json
{
  "sepet_yemekler": [
  {
    "sepet_yemek_id": "1",
    "yemek_adi": "Ayran",
    "yemek_resim_adi": "ayran.png",
    "yemek_fiyat": "10",
    "yemek_siparis_adet": "2",
    "kullanici_adi": "sametozkan"
  }
],
  "message": "Required field(s) is missing"
}
```

### POST: Sepetten Yemek Silme
#### Parametreler
* sepet_yemek_id: Int
* kullanici_adi: String
```json
{
  "success": 0,
  "message": "Required field(s) is missing"
}
```

## İletişim
<p>Email: <a href="mailto:samet-ozkan@outlook.com">samet-ozkan@outlook.com</a></p>
