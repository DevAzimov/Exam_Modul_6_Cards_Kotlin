package com.magicapp.exam_modul_6_cards_kotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity (tableName = "cards")
data class Cards (

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val cardNumber: Long? = null,
    val cardHolder: String? = null,
    var isAvailable: Boolean = false,
    val cardData: String? = null,
    val cvv: Int

):Serializable