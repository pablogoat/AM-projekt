package com.example.summonerinfo.dto

data class ChampionDTO (
    val championId: Int,
    val championLevel: Int,
    val championPoints: Int,
    val lastPlayTime: Long,
    val chestGranted: Boolean
    )