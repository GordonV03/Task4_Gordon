package com.example.task3_gordon.data.repositories

import android.util.Log
import com.example.task3_gordon.domain.entities.Hero
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class OpenDotaHeroRepository : HeroRepository {

    private val client = OkHttpClient()

    override fun getHeroes(): List<Hero> {
        val request = Request.Builder()
            .url("https://api.opendota.com/api/heroes")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")

            val jsonData = JSONArray(response.body?.string() ?: "")
            Log.d("OpenDotaHeroRepository", "Response: $jsonData")

            val heroes = mutableListOf<Hero>()
            for (i in 0 until jsonData.length()) {
                val heroJson = jsonData.getJSONObject(i)
                val rolesJsonArray = heroJson.getJSONArray("roles")

                // Преобразуем массив ролей в строку
                val roles = (0 until rolesJsonArray.length()).joinToString(", ") {
                    rolesJsonArray.getString(it)
                }

                val hero = Hero(
                    heroId = heroJson.getInt("id"),
                    localizedName = heroJson.getString("localized_name"),
                    roles = roles
                )
                heroes.add(hero)
            }
            return heroes.sortedBy { it.localizedName }
        }
    }
}