package com.magicapp.exam_modul_6_cards_kotlin.database

import androidx.room.*
import androidx.room.Dao
import com.magicapp.exam_modul_6_cards_kotlin.model.Cards


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCard(card: Cards)

    @Query("SELECT * FROM cards")
    fun getCards(): List<Cards>

    @Query("DELETE FROM cards")
    fun delete()
}