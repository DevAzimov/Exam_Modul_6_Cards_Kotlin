package com.magicapp.exam_modul_6_cards_kotlin.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.magicapp.exam_modul_6_cards_kotlin.R
import com.magicapp.exam_modul_6_cards_kotlin.adapter.CardAdapter
import com.magicapp.exam_modul_6_cards_kotlin.database.AppDatabase
import com.magicapp.exam_modul_6_cards_kotlin.model.Cards
import com.magicapp.exam_modul_6_cards_kotlin.networking.service.ApiClient
import com.magicapp.exam_modul_6_cards_kotlin.networking.service.CardService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var service: CardService
    private lateinit var ivAddCard: ImageView
    private lateinit var rvCards: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.parseColor("#393939")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        service = ApiClient.createService(CardService::class.java)
        appDatabase = AppDatabase.getInstance(this)

        initViews()

    }

    private fun initViews() {
        rvCards = findViewById(R.id.rvCards)
        ivAddCard = findViewById(R.id.ivAddCard)
        cardAdapter = CardAdapter()
        getCards()
        refreshAdapter()

        ivAddCard.setOnClickListener{
            addCard()
        }

    }

    val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK) {
            val data: Intent? = it.data
            val cardToAdd = data?.getSerializableExtra("card")
            Log.d("@@@", "details: $cardToAdd")
            saveCard(cardToAdd as Cards)
        }
    }

    private fun saveCard(cards: Cards) {
        if (isInternetAvailable()) {
            service.addCard(cards).enqueue(object : Callback<Cards> {
                override fun onResponse(call: Call<Cards>, response: Response<Cards>) {
                    cards.isAvailable = true
                    cards.id = null
                    saveToDatabase(cards)
                    cardAdapter.addCard(response.body()!!)
                    Toast.makeText(this@MainActivity, "Card saved", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Cards>, t: Throwable) {

                }
            })
        } else {
            cards.isAvailable = false
            cards.id
            saveToDatabase(cards)
            cardAdapter.addCard(cards)
        }
    }

    private fun saveToDatabase(cards: Cards) {
        appDatabase.cardDao().addCard(cards)

    }

    private fun addCard() {
        val intent = Intent(this, AddCardActivity::class.java)
        detailLauncher.launch(intent)
    }

    private fun refreshAdapter() {
        rvCards.adapter = cardAdapter
    }

    private fun getCards() {
        if (isInternetAvailable()) {
            val unSavedCards: ArrayList<Cards> = ArrayList()
            val savedCards = appDatabase.cardDao().getCards()
            savedCards.forEach {
                if (!it.isAvailable) {
                    unSavedCards.add(it)
                }
            }

            Log.d("@@@", "getCards: $unSavedCards")

            unSavedCards.forEach {
                it.isAvailable = true
                appDatabase.cardDao().addCard(it)
            }

            unSavedCards.forEach {
                service.addCard(it).request()
            }

            service.getCards().enqueue(object : Callback<List<Cards>> {
                override fun onResponse(call: Call<List<Cards>>, response: Response<List<Cards>>) {
                    cardAdapter.submitDate(response.body()!!)
                }
                override fun onFailure(call: Call<List<Cards>>, t: Throwable) {

                }
            })
        }else {
            cardAdapter.submitDate(appDatabase.cardDao().getCards())
        }
    }

    private fun isInternetAvailable(): Boolean {
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val infoMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val infoWiFi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return infoMobile!!.isConnected || infoWiFi!!.isConnected
    }
}