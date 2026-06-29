package com.pdmcourse2026.basictemplate.data.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {
  var authToken: String? = null

  val client = HttpClient(OkHttp) {
    // Parseo automáaico de JSON
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
      })
    }

    // Plugin de loggin
    install(Logging) {
      logger = object : Logger {
        override fun log(message: String) {
          Log.d("KtorClient", message)
        }
      }
      level = LogLevel.ALL
    }

    // Configuracion para todas las peticiones
    defaultRequest {
      url("https://qjcxdvfzyseuvezacxsd.supabase.co/functions/v1/rankeuca/")
      header(HttpHeaders.Accept, "application/json")
      authToken?.let { token ->
        header(HttpHeaders.Authorization, "Bearer $token")
      }
    }
  }
}