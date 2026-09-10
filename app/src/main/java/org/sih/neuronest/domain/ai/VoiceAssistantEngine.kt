package org.sih.neuronest.domain.ai

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceAssistantEngine @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    private val _lastSpokenText = MutableStateFlow("")
    val lastSpokenText: StateFlow<String> = _lastSpokenText

    init {
        textToSpeech = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale.ENGLISH
            isInitialized = true
        }
    }

    /**
     * Speaks high-clarity voice instructions for elderly patients with support for regional prompt fallback
     */
    fun speakInstruction(text: String, language: String = "English") {
        _lastSpokenText.value = text
        _isSpeaking.value = true

        if (isInitialized && textToSpeech != null) {
            // Configure voice language according to regional preference
            when (language.lowercase()) {
                "assamese", "bengali" -> textToSpeech?.language = Locale("bn", "IN")
                "hindi" -> textToSpeech?.language = Locale("hi", "IN")
                else -> textToSpeech?.language = Locale.ENGLISH
            }
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "NeuroNest_TTS_ID")
        }
    }

    fun stopSpeaking() {
        textToSpeech?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}
