package com.example.summonerinfo.service

import android.content.Context
import com.example.summonerinfo.R
import com.example.summonerinfo.dto.ChampionDTO
import com.example.summonerinfo.dto.RotationDTO
import com.example.summonerinfo.dto.SummonerDTO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RiotApiFetcher(private val context: Context) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://eun1.api.riotgames.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun fetchSummonerData(name: String): SummonerDTO? {
        val token = context.getString(R.string.riot_token)

        return try {
            val response = apiService.fetchSummonerData(token, name)
            if (response.isSuccessful) {
                val data = response.body()
                data?.let {
                    // Przekształć dane na obiekt SummonerDTO
                    val summonerDTO = SummonerDTO(
                        puuid = it.puuid,
                        revisionDate = it.revisionDate,
                        summonerLevel = it.summonerLevel
                    )
                    summonerDTO
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchRotationData(): RotationDTO? {
        val token = context.getString(R.string.riot_token)

        return try {
            val response = apiService.fetchRotationData(token)
            if (response.isSuccessful) {
                val data = response.body()
                data?.let {
                    // Przekształć dane na obiekt RotationDTO
                    val rotationDTO = RotationDTO(
                        freeChampionIds = it.freeChampionIds
                    )
                    rotationDTO
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchChampion(championId: Int, puuid: String): ChampionDTO? {
        val token = context.getString(R.string.riot_token)

        return try {
            val response = apiService.fetchChampion(token, puuid, championId)
            if (response.isSuccessful) {
                val data = response.body()
                data?.let {
                    val championDTO = ChampionDTO(
                        championId = it.championId,
                        championLevel = it.championLevel,
                        championPoints = it.championPoints,
                        lastPlayTime = it.lastPlayTime,
                        chestGranted = it.chestGranted
                    )
                    championDTO
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchChampions(puuid: String): List<ChampionDTO>? {
        val token = context.getString(R.string.riot_token)

        return try {
            val response = apiService.fetchChampionsMastery(token, puuid)
            if (response.isSuccessful) {
                val data = response.body()
                data?.let {
                    val championDTOs = mutableListOf<ChampionDTO>()
                    for (championData in it) {
                        val championDTO = ChampionDTO(
                            championId = championData.championId,
                            championLevel = championData.championLevel,
                            championPoints = championData.championPoints,
                            lastPlayTime = championData.lastPlayTime,
                            chestGranted = championData.chestGranted
                        )
                        championDTOs.add(championDTO)
                    }
                    championDTOs
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
