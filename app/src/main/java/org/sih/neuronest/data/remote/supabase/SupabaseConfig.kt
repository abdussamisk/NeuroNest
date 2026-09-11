package org.sih.neuronest.data.remote.supabase

object SupabaseConfig {
    var SUPABASE_URL: String = "https://wyqjrwcbbvhnddhnpicf.supabase.co"
    var SUPABASE_ANON_KEY: String = "sb_publishable_g2pd8ZdXJvetQDWYB94RMQ_HedhrfYm"

    fun getHeaders(): Map<String, String> {
        return mapOf(
            "apikey" to SUPABASE_ANON_KEY,
            "Authorization" to "Bearer $SUPABASE_ANON_KEY",
            "Content-Type" to "application/json",
            "Prefer" to "resolution=merge-duplicates"
        )
    }
}
