package org.sih.neuronest.presentation.caregiver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.sih.neuronest.presentation.components.LargeAccessibilityButton
import org.sih.neuronest.theme.PatientTextPrimary
import org.sih.neuronest.theme.PatientTextSecondary
import org.sih.neuronest.theme.PrimaryTeal

@Composable
fun CaregiverSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: CaregiverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var contactNumber by remember { mutableStateOf(uiState.profile.caregiverContactPhone) }
    var language by remember { mutableStateOf(uiState.profile.preferredLanguage) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "⚙️ Caregiver Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Patient Profile Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PatientTextPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(text = "Caregiver Emergency Contact Phone:", fontSize = 14.sp, color = PatientTextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryTeal)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Preferred Regional Language Voice:", fontSize = 14.sp, color = PatientTextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = language,
                    onValueChange = { language = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryTeal)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        LargeAccessibilityButton(
            text = "Save Settings 💾",
            onClick = { onNavigateBack() }
        )
    }
}
