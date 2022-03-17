package com.magicapp.exam_modul_6_cards_kotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.magicapp.exam_modul_6_cards_kotlin.model.Cards

@Database(entities = [Cards ::class], version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cardDao(): Dao

    companion object {

        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "card.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}