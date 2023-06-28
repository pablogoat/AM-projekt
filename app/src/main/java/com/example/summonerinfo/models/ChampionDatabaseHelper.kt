package com.example.summonerinfo.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.summonerinfo.dto.ChampionContract
import com.example.summonerinfo.dto.ChampionInfo
import java.io.BufferedReader
import java.io.InputStreamReader

class ChampionDatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "champions.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {

        createChampionTable(db)
        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    private fun createChampionTable(db: SQLiteDatabase) {
        val createChampionTableQuery = """
            CREATE TABLE ${ChampionContract.ChampionEntry.TABLE_NAME} (
                ${ChampionContract.ChampionEntry._ID} INTEGER PRIMARY KEY,
                ${ChampionContract.ChampionEntry.COLUMN_NAME_CHAMPION_ID} INTEGER,
                ${ChampionContract.ChampionEntry.COLUMN_NAME_CHAMPION_NAME} TEXT
            )
        """.trimIndent()
        db.execSQL(createChampionTableQuery)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        val assetManager = context.assets
        val inputStream = assetManager.open("champions.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val championList = mutableListOf<ChampionInfo>()

        reader.useLines { lines ->
            lines.forEach { line ->
                val values = line.split(":").map { it.trim() }
                val championName = values[0]
                val championId = values[1].toInt()
                val champion = ChampionInfo(championId, championName)
                championList.add(champion)
            }
        }

        for (champion in championList) {
            println(champion)
            val values = ContentValues().apply {
                put(ChampionContract.ChampionEntry.COLUMN_NAME_CHAMPION_ID, champion.id)
                put(ChampionContract.ChampionEntry.COLUMN_NAME_CHAMPION_NAME, champion.name)
            }
            db.insert(ChampionContract.ChampionEntry.TABLE_NAME, null, values)
        }
    }
}