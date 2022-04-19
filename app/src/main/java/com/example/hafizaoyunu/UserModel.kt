package com.example.hafizaoyunu

class UserModel {
    var id: Int = 0
    var adSoyad: String = ""
    var skor: String = ""
    var sure: String = ""

    constructor(adSoyad:String,skor:String,sure:String){
        this.adSoyad = adSoyad
        this.skor = skor
        this.sure = sure
    }
    constructor(){

    }
}