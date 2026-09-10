package org.sih.neuronest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "memory_cue")
data class MemoryCue(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val relationTag: String, // GRANDSON, DAUGHTER, HOME, PET, FESTIVAL
    val descriptionText: String,
    val imagePathOrResource: String,
    val audioVoiceNoteText: String
)
