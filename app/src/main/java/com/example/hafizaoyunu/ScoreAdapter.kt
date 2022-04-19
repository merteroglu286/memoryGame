package com.example.hafizaoyunu

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hafizaoyunu.databinding.ScoreItemsBinding

class ScoreAdapter (mContext: Context,val users : ArrayList<UserModel>):RecyclerView.Adapter<ScoreAdapter.ViewHolder>(){

    val myContext = mContext
    val db = DataBaseHelper(myContext)
    private var onclickDeleteItem: ((UserModel) -> Unit)? = null

    fun setOnClickDeleteItem(callback: (UserModel) -> Unit){
        this.onclickDeleteItem= callback
    }

    inner class ViewHolder(scoreItemsBingding: ScoreItemsBinding) :
        RecyclerView.ViewHolder(scoreItemsBingding.root) {
            val txtKullaniciAdi = scoreItemsBingding.textViewKullaniciAdi
            val txtScore = scoreItemsBingding.textViewScore
            val txtTime = scoreItemsBingding.textViewTime
            val bg = scoreItemsBingding.cardView
            val deleteButton = scoreItemsBingding.deleteButton

        init {
            deleteButton.setOnClickListener {
                users.removeAt(adapterPosition)
                notifyDataSetChanged()
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val scoreItemsBingding =
            ScoreItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(scoreItemsBingding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users : UserModel = users[position]
        holder.txtKullaniciAdi.text = users.adSoyad
        holder.txtScore.text = users.skor
        holder.txtTime.text = users.sure

        if(position%2 == 1){
            holder.bg.setBackgroundColor(Color.parseColor("#28a0ad"))
        }else{
            holder.bg.setBackgroundColor(Color.parseColor("#60efff"))
        }

        holder.deleteButton.setOnClickListener { onclickDeleteItem?.invoke(users) }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}