package org.sih.neuronest.presentation.patient.vault

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.sih.neuronest.presentation.patient.PatientViewModel
import org.sih.neuronest.theme.PatientTextPrimary
import org.sih.neuronest.theme.PatientTextSecondary
import org.sih.neuronest.theme.SecondaryWarmGold

@Composable
fun MemoryVaultScreen(
    onNavigateBack: () -> Unit,
    viewModel: PatientViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val sampleVaultCues = listOf(
        Triple("Grandson Rahul", "👦", "Rahul lives in Guwahati. He loves playing cricket and visits every Sunday."),
        Triple("Ancestral Home in Tezpur", "🏡", "Wooden home near Brahmaputra river with beautiful tea garden views."),
        Triple("Rongali Bihu Memories", "🥁", "Spring festival with family dhol drums and homemade pitha sweets.")
    )

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
                text = "🖼️ Personal Memory Vault",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sampleVaultCues) { cue ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = cue.second, fontSize = 40.sp)
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    text = cue.first,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PatientTextPrimary
                                )
                            }
                            IconButton(onClick = { /* Speak audio note */ }) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Audio", tint = SecondaryWarmGold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = cue.third,
                            fontSize = 15.sp,
                            color = PatientTextSecondary,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}
