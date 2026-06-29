package com.pdmcourse2026.basictemplate.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
  private val sharedPreferences: SharedPreferences =
    context.getSharedPreferences("rankeuca_prefs", Context.MODE_PRIVATE)

  companion object {
    private const val KEY_API_KEY = "api_key"
    private const val KEY_CARNET = "carnet"
  }

  fun getApiKey(): String? = sharedPreferences.getString(KEY_API_KEY, "c1452e68-ee85-4afa-ae8c-281d97fdfa54")

  fun saveApiKey(apiKey: String) {
    sharedPreferences.edit().putString(KEY_API_KEY, apiKey).apply()
  }

  fun getCarnet(): String? = sharedPreferences.getString(KEY_CARNET, "00029823")

  fun saveCarnet(carnet: String) {
    sharedPreferences.edit().putString(KEY_CARNET, carnet).apply()
  }

  fun clear() {
    sharedPreferences.edit().clear().apply()
  }
}
