package com.example.hafizaoyunu

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hafizaoyunu.databinding.DialogViewBinding
import com.example.hafizaoyunu.databinding.FragmentBaslangicEkraniBinding


class BaslangicEkrani : Fragment() {

    private lateinit var fragmentBindingBaslangicEkrani : FragmentBaslangicEkraniBinding
    private lateinit var dialogViewBinding : DialogViewBinding
    private lateinit var dialog:  AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentBindingBaslangicEkrani = FragmentBaslangicEkraniBinding.inflate(inflater,container,false)


        val animDrawable = fragmentBindingBaslangicEkrani.rootLayout.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(1)
        animDrawable.setExitFadeDuration(5000)
        animDrawable.start()

        fragmentBindingBaslangicEkrani.button.setOnClickListener {
            dialogEkraniniGetir()
           /* activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_container, OyunEkrani())
                ?.commit() */

        }

        fragmentBindingBaslangicEkrani.btnSkorlar.setOnClickListener {
            activity?.let{
                val intent = Intent (it, ScoreBoardActivity::class.java)
                it.startActivity(intent)
            }
        }

        return fragmentBindingBaslangicEkrani.root
    }


    private fun dialogEkraniniGetir() {

        val alertDialog = AlertDialog.Builder(context)
        dialogViewBinding = DialogViewBinding.inflate(layoutInflater)
        alertDialog.setView(dialogViewBinding.root)

        dialogViewBinding.button4x4.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_container, OyunEkrani_4x4())
                ?.commit()

            dialog.dismiss()
        }
        dialogViewBinding.button6x6.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_container, OyunEkrani_6x6())
                ?.commit()

            dialog.dismiss()
        }
        dialog = alertDialog.create()
        dialog.show()


    }

}