package com.example.DatabaseConfig

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.cdimascio.dotenv.Dotenv

fun initSupabase() {
    val dotenv = Dotenv.configure().load()

    val supabaseUrl = dotenv["SUPABASE_URL"] ?: throw IllegalArgumentException("SUPABASE_URL is missing")
    val supabaseKey = dotenv["SUPABASE_KEY"] ?: throw IllegalArgumentException("SUPABASE_KEY is missing")

    val supabase = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ) {
        install(Postgrest)
    }
}
