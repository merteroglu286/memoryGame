package com.example.hafizaoyunu

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val databaseName = "Veritabani2"
val tableName = "kullanicilar"
val columnName = "AdiSoyadi"
val columnTime = "Sure"
val columnScore = "Skoru"
val columnId = "Id"

class DataBaseHelper(var context: Context):SQLiteOpenHelper(context, databaseName,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        var createTable = " CREATE TABLE " + tableName +"(" +
                columnId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                columnName + " VARCHAR(256)," +
                columnTime + " VARCHAR(256)," +
                columnScore + " VARCHAR(256))"
        db?.execSQL(createTable)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(kullanici : UserModel){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(columnName,kullanici.adSoyad)
        cv.put(columnScore,kullanici.skor)
        cv.put(columnTime,kullanici.sure)

        var sonuc = db.insert(tableName,null,cv)

        if (sonuc == (-1).toLong()){
            Toast.makeText(context,"Hatalı",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context,"Başarılı",Toast.LENGTH_LONG).show()

        }

    }

    @SuppressLint("Range")
    fun getUsers(myContext:Context):ArrayList<UserModel>{
        val query = "Select * From $tableName"
        val db : SQLiteDatabase = this.readableDatabase
        val cursor = db.rawQuery(query,null)
        val users = ArrayList<UserModel>()

        if(cursor.count == 0){

        }else{
            while (cursor.moveToNext()){
                var kullanici = UserModel()
                kullanici.id = cursor.getString(cursor.getColumnIndex(columnId)).toInt()
                kullanici.adSoyad = cursor.getString(cursor.getColumnIndex(columnName))
                kullanici.skor = cursor.getString(cursor.getColumnIndex(columnScore))
                kullanici.sure = cursor.getString(cursor.getColumnIndex(columnTime))
                users.add(kullanici)
            }
        }

        cursor.close()
        db.close()
        return users
    }

    fun deleteData(id : Int):Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(columnId,id)

        val success = db.delete(tableName,"id = $id",null)
        db.close()
        return success
    }

}