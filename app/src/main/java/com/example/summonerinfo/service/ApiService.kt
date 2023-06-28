package com.example.summonerinfo.service

import com.example.summonerinfo.dto.ChampionDTO
import com.example.summonerinfo.dto.RotationDTO
import com.example.summonerinfo.dto.SummonerDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("/lol/summoner/v4/summoners/by-name/{name}")
    suspend fun fetchSummonerData(
        @Header("X-Riot-Token") token: String,
        @Path("name") name: String
    ): Response<SummonerDTO>

    @GET("/lol/platform/v3/champion-rotations")
    suspend fun fetchRotationData(
        @Header("X-Riot-Token") token: String
    ): Response<RotationDTO>

    @GET("/lol/champion-mastery/v4/champion-masteries/by-puuid/{encryptedPUUID}")
    suspend fun fetchChampionsMastery(
        @Header("X-Riot-Token") token: String,
        @Path("encryptedPUUID") puuid: String
    ): Response<List<ChampionDTO>>

    @GET("/lol/champion-mastery/v4/champion-masteries/by-puuid/{encryptedPUUID}/by-champion/{championId}")
    suspend fun fetchChampion(
        @Header("X-Riot-Token") token: String,
        @Path("encryptedPUUID") puuid: String,
        @Path("championId") championId: Int
    ): Response<ChampionDTO>
}