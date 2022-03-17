package com.magicapp.exam_modul_6_cards_kotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magicapp.exam_modul_6_cards_kotlin.R
import com.magicapp.exam_modul_6_cards_kotlin.model.Cards

class CardAdapter: RecyclerView.Adapter<CardAdapter.CardVH>() {

    private val cards:ArrayList<Cards> = ArrayList()

    inner class CardVH(private val view: View) :RecyclerView.ViewHolder(view) {

        private val tvCardNumber : TextView = view.findViewById(R.id.tvCardNumber)
        private val tvCardHolder : TextView = view.findViewById(R.id.tvCardHolder)
        private val tvExpireDate : TextView = view.findViewById(R.id.tvExpireDate)
        private val ivCard : ImageView = view.findViewById(R.id.ivCard)

        fun bind(position: Int) {
            tvCardNumber.text = cards[position].cardNumber.toString().creditCardFormatted
            tvCardHolder.text = cards[position].cardHolder
            tvExpireDate.text = cards[position].cardData
            if (position % 2 == 0){
                ivCard.setImageResource(R.drawable.backround_border_card1)
            }else{
                ivCard.setImageResource(R.drawable.backround_border_card2)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardVH = CardVH(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_card, parent, false
        )
    )

    override fun onBindViewHolder(holder: CardAdapter.CardVH, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cards.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitDate(cards: List<Cards>){
        this.cards.addAll(cards)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addCard(cards: Cards) {
        this.cards.add(cards)
        notifyDataSetChanged()
    }

    val String.creditCardFormatted: String
    get() {val preparedString = replace(" ", "").trim()
        val result = StringBuilder()
        for (i in preparedString.indices) {
            if (i % 4 == 0 && i != 0) {
                result.append(" ")
            }
            result.append(preparedString[i])
        }
        return result.toString()
    }
}

