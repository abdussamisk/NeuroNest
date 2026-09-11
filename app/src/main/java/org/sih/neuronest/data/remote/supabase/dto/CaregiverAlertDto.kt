package org.sih.neuronest.data.remote.supabase.dto

import com.google.gson.annotations.SerializedName
import org.sih.neuronest.data.local.entity.CaregiverAlert

data class CaregiverAlertDto(
    @SerializedName("id") val id: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("alert_type") val alertType: String,
    @SerializedName("severity") val severity: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("is_dismissed") val isDismissed: Boolean
) {
    companion object {
        fun fromEntity(entity: CaregiverAlert): CaregiverAlertDto {
            return CaregiverAlertDto(
                id = entity.id,
                timestamp = entity.timestamp,
                alertType = entity.alertType,
                severity = entity.severity,
                title = entity.title,
                description = entity.description,
                isDismissed = entity.isDismissed
            )
        }
    }

    fun toEntity(): CaregiverAlert {
        return CaregiverAlert(
            id = id,
            timestamp = timestamp,
            alertType = alertType,
            severity = severity,
            title = title,
            description = description,
            isDismissed = isDismissed
        )
    }
}
