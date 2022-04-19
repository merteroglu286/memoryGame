package com.example.hafizaoyunu

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hafizaoyunu.databinding.ActivityScoreBoardBinding

class ScoreBoardActivity : AppCompatActivity() {
    companion object{
        lateinit var dbHandler: DataBaseHelper
    }

    private lateinit var scoreBoardBinding: ActivityScoreBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoreBoardBinding = ActivityScoreBoardBinding.inflate(layoutInflater)
        setContentView(scoreBoardBinding.root)
/*
        val context = this
        var db = DataBaseHelper(context)

        var data = db.readData()
        scoreBoardBinding.textView7.text = ""
        for(i in 0 until data.size){
            scoreBoardBinding.textView7.append(data.get(i).id.toString()+ " "
            + data.get(i).adSoyad + " " + data.get(i).skor + "\n")
        }
*/

        dbHandler = DataBaseHelper(this)
        viewUsers()


        val animDrawable = scoreBoardBinding.rootLayout.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(1)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()

        val usersList = dbHandler.getUsers(this)
        val adapter = ScoreAdapter(this,usersList)
        val rv : RecyclerView = findViewById(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false) as RecyclerView.LayoutManager
        rv.adapter = adapter
        adapter.setOnClickDeleteItem {
            deleteUsers(it.id)

        }
    }

    private fun viewUsers(){
        val usersList = dbHandler.getUsers(this)
        val adapter = ScoreAdapter(this,usersList)
        val rv : RecyclerView = findViewById(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    private fun deleteUsers(id:Int){
        dbHandler.deleteData(id)
        viewUsers()
    }


}