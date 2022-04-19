package com.example.hafizaoyunu
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.hafizaoyunu.databinding.DialogToLossBinding
import com.example.hafizaoyunu.databinding.DialogToWinBinding
import com.example.hafizaoyunu.databinding.FragmentOyunEkrani4x4Binding
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


class OyunEkrani_4x4 : Fragment() {
    private lateinit var binding: FragmentOyunEkrani4x4Binding

    private lateinit var dialogtoWinBinding: DialogToWinBinding
    private lateinit var dialogtoLossBinding: DialogToLossBinding
    private lateinit var dialog: AlertDialog

    private var eslesmeSayaci : Int = 0
    private var hamleSayaci : Int = 0
    private var oyunSkoru : Int = 0

    private var zaman = object : CountDownTimer(60000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            binding.timer.setText((millisUntilFinished / 1000).toString() + " saniye")
        }

        override fun onFinish() {
            dialogToLossEkraniniGetir()
        }
    }

    private val db = context?.let { DataBaseHelper(it) }

    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOyunEkrani4x4Binding.inflate(inflater,container,false)


        val images = mutableListOf(R.drawable.vector_adb, R.drawable.vector_airplane, R.drawable.vector_beach, R.drawable.vector_flare
        , R.drawable.vector_safety,R.drawable.vector_origin,R.drawable.vector_lightlub,R.drawable.vector_gamepad)
        // Add each image twice so we can create pairs
        images.addAll(images)
        // Randomize the order of images
        images.shuffle()

        buttons = listOf(binding.imageButton1,binding.imageButton2,binding.imageButton3,binding.imageButton4
            ,binding.imageButton5,binding.imageButton6,binding.imageButton7,binding.imageButton8
            ,binding.imageButton9,binding.imageButton10,binding.imageButton11,binding.imageButton12
            ,binding.imageButton13,binding.imageButton14,binding.imageButton15,binding.imageButton16)

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                binding.hamleSayisi.text = (++hamleSayaci).toString()
                // Update models
                updateModels(index)
                // Update the UI for the game
                updateViews()
            }
        }



        zaman.start()




        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    showAlertDialog()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(requireContext()).setMessage(getString(R.string.alertMessage)).setPositiveButton(getString(R.string.alertPositive)
        ,object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.main_container, BaslangicEkrani())
                        ?.commit()
                }
        }).setNegativeButton(getString(R.string.alertNegative),object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
            .show()
    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f
                button.isClickable = false
            }
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.main_icon)

        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]
        // Error checking:
        if (card.isFaceUp) {
            return
        }
        // Three cases
        // 0 cards previously flipped over => restore cards + flip over the selected card
        // 1 card previously flipped over => flip over the selected card + check if the images match
        // 2 cards previously flipped over => restore cards + flip over the selected card
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 selected cards previously
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            // exactly 1 card was selected previously
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp


    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {

        if (cards[position1].identifier == cards[position2].identifier) {
            cards[position1].isMatched = true
            cards[position2].isMatched = true

            binding.eslesmeSayisi.text = (++eslesmeSayaci).toString() + "/ 8"
            oyunSkoru = oyunSkoru+10

            if(eslesmeSayaci == 8){
                dialogToWinEkraniniGetir()
                zaman.cancel()
            }

        }
        else{
            oyunSkoru = oyunSkoru-2
        }
        binding.oyunSkoru.text = oyunSkoru.toString()
    }


    private fun dialogToWinEkraniniGetir() {

        val alertDialog = AlertDialog.Builder(context)
        dialogtoWinBinding = DialogToWinBinding.inflate(layoutInflater)
        alertDialog.setView(dialogtoWinBinding.root)

        dialogtoWinBinding.dialogOyunSkoru.text = binding.oyunSkoru.text
        dialogtoWinBinding.dialogOyunSuresi.text = binding.timer.text

        var db = DataBaseHelper(requireContext())

        val party = Party(
            speed = 0.5f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        dialogtoWinBinding.konfettiView.start(party)


        dialogtoWinBinding.btnKaydet.setOnClickListener {

            var kullaniciAdi = dialogtoWinBinding.kullaniciAdi.text.toString()
            var skor = dialogtoWinBinding.dialogOyunSkoru.text.toString()
            var sure = dialogtoWinBinding.dialogOyunSuresi.text.toString()

            if(kullaniciAdi.isNotEmpty() && skor.isNotEmpty() && sure.isNotEmpty()){
                var kullanici = UserModel(kullaniciAdi,skor,sure)
                db.insertData(kullanici)
            }else{
                Toast.makeText(context, "Lütfen boş alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }

            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_container, BaslangicEkrani())
                ?.commit()

            dialog.dismiss()
        }
        dialog = alertDialog.create()
        dialog.show()



    }
    private fun dialogToLossEkraniniGetir() {

        val alertDialog = AlertDialog.Builder(context)
        dialogtoLossBinding = DialogToLossBinding.inflate(layoutInflater)
        alertDialog.setView(dialogtoLossBinding.root)

        dialogtoLossBinding.dialogOyunSkoru.text = binding.oyunSkoru.text


        dialogtoLossBinding.btnTekrarOyna.setOnClickListener {


            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_container, BaslangicEkrani())
                ?.commit()

            dialog.dismiss()
        }
        dialog = alertDialog.create()
        dialog.show()


    }

}