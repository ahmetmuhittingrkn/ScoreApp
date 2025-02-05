package com.example.scoreapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.scoreapp.data.entity.Match
import com.example.scoreapp.data.entity.Team
import com.example.scoreapp.ui.viewmodel.TeamDetailViewModel
import com.example.scoreapp.util.UiState
import com.example.scoreapp.util.formatDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun TeamDetailScreen(
    navController: NavController,
    teamId: Int,
    viewModel: TeamDetailViewModel = hiltViewModel()
) {
    val teamDetailState by viewModel.teamDetail.collectAsState()
    val teamMatchesState by viewModel.teamMatches.collectAsState()

    LaunchedEffect(teamId) {
        viewModel.loadTeamDetail(teamId)
        viewModel.loadTeamMatches(teamId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Takım Detayı", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        // Gradient arka plan
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            when (teamDetailState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val team = (teamDetailState as UiState.Success<Team>).data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Ortada konumlandırılmış takım bilgilerini gösteren Card
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(bottom = 16.dp)
                                    .shadow(8.dp, shape = MaterialTheme.shapes.medium),
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    GlideImage(
                                        model = team.crest,
                                        contentDescription = team.name,
                                        modifier = Modifier.size(140.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = team.name,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 28.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Kuruluş: ${team.founded ?: "Bilinmiyor"}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Stadyum: ${team.venue ?: "Bilinmiyor"}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        // Maçlar başlığı
                        Text(
                            text = "Maçlar",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        // Takımın maçları
                        when (teamMatchesState) {
                            is UiState.Loading -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }
                            is UiState.Success -> {
                                val matches = (teamMatchesState as UiState.Success<List<Match>>).data
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 16.dp)
                                ) {
                                    items(matches) { match ->
                                        MatchItem(match, navController)
                                    }
                                }
                            }
                            is UiState.Error -> {
                                Text(text = "Maçlar yüklenemedi", color = Color.Red)
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Takım bilgisi yüklenemedi", color = Color.Red)
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MatchItem(match: Match, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("match_detail/${match.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = formatDate(match.utcDate),
                fontSize = 14.sp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = match.homeTeam.crest ?: "",
                    contentDescription = match.homeTeam.name,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 8.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = match.homeTeam.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        text = "${match.score?.fullTime?.home ?: "-"} : ${match.score?.fullTime?.away ?: "-"}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(text = match.awayTeam.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                GlideImage(
                    model = match.awayTeam.crest ?: "",
                    contentDescription = match.awayTeam.name,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}
