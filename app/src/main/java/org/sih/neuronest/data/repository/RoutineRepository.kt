package org.sih.neuronest.data.repository

import kotlinx.coroutines.flow.Flow
import org.sih.neuronest.data.local.dao.RoutineDao
import org.sih.neuronest.data.local.entity.MemoryCue
import org.sih.neuronest.data.local.entity.RoutineReminder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutineRepository @Inject constructor(
    private val routineDao: RoutineDao
) {

    val remindersFlow: Flow<List<RoutineReminder>> = routineDao.getAllRemindersFlow()
    val memoryCuesFlow: Flow<List<MemoryCue>> = routineDao.getAllMemoryCuesFlow()

    suspend fun seedInitialDataIfEmpty() {
        // Seeds initial daily reminders for dementia routine guidance
        val sampleReminders = listOf(
            RoutineReminder(
                title = "Morning Herbal Tea & Breakfast",
                category = "MEAL",
                scheduledTimeFormatted = "08:00 AM",
                audioPromptText = "It is 8 AM. Please enjoy your morning tea and nutritious breakfast.",
                visualCueIcon = "ic_tea"
            ),
            RoutineReminder(
                title = "Morning BP & Memory Pill",
                category = "MEDICINE",
                scheduledTimeFormatted = "09:00 AM",
                audioPromptText = "Time for your morning medicine with a glass of water.",
                visualCueIcon = "ic_medicine"
            ),
            RoutineReminder(
                title = "Hydration & Water Break",
                category = "HYDRATION",
                scheduledTimeFormatted = "11:30 AM",
                audioPromptText = "Stay hydrated! Drink a fresh glass of water.",
                visualCueIcon = "ic_water"
            ),
            RoutineReminder(
                title = "Afternoon Memory Game Session",
                category = "GAME",
                scheduledTimeFormatted = "04:00 PM",
                audioPromptText = "Time for your daily memory game exercise!",
                visualCueIcon = "ic_game"
            )
        )

        sampleReminders.forEach { routineDao.insertReminder(it) }

        // Seeds initial family memory cues
        val sampleCues = listOf(
            MemoryCue(
                title = "Grandson Rahul",
                relationTag = "GRANDSON",
                descriptionText = "Rahul lives in Guwahati. He loves playing cricket and visits every Sunday.",
                imagePathOrResource = "img_rahul",
                audioVoiceNoteText = "Grandpa, Rahul here! Sending you lots of love!"
            ),
            MemoryCue(
                title = "Ancestral Home in Tezpur",
                relationTag = "HOME",
                descriptionText = "Beautiful wooden house near the Brahmaputra river with tea gardens.",
                imagePathOrResource = "img_tezpur",
                audioVoiceNoteText = "Remember the gentle evening breeze by the tea garden."
            ),
            MemoryCue(
                title = "Bihu Festival Celebration",
                relationTag = "FESTIVAL",
                descriptionText = "Rongali Bihu Spring festival with traditional dhol drums and pitha sweets.",
                imagePathOrResource = "img_bihu",
                audioVoiceNoteText = "Listen to the joyful beat of the Bihu Dhol!"
            )
        )

        sampleCues.forEach { routineDao.insertMemoryCue(it) }
    }

    suspend fun acknowledgeReminder(id: String) {
        routineDao.acknowledgeReminder(id)
    }

    suspend fun addMemoryCue(cue: MemoryCue) {
        routineDao.insertMemoryCue(cue)
    }
}
