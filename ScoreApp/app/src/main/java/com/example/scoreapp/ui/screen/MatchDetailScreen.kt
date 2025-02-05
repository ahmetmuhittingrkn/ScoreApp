package com.example.scoreapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.scoreapp.data.entity.MatchDetailResponse
import com.example.scoreapp.ui.viewmodel.MatchDetailViewModel
import com.example.scoreapp.util.UiState
import com.example.scoreapp.util.formatDate
import com.example.scoreapp.util.translateDuration
import com.example.scoreapp.util.translateMatchStatus
import com.example.scoreapp.util.translateWinner

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MatchDetailScreen(
    matchId: Int,
    viewModel: MatchDetailViewModel = hiltViewModel()
) {
    val matchDetailState by viewModel.matchDetail.collectAsState()

    LaunchedEffect(matchId) {
        viewModel.loadMatchDetail(matchId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Maç Detayı", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
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
            when (matchDetailState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val matchDetail = (matchDetailState as UiState.Success<MatchDetailResponse>).data

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                GlideImage(
                                    model = matchDetail.homeTeam?.crest ?: "",
                                    contentDescription = matchDetail.homeTeam?.name,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = matchDetail.homeTeam?.name ?: "",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "vs",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                GlideImage(
                                    model = matchDetail.awayTeam?.crest ?: "",
                                    contentDescription = matchDetail.awayTeam?.name,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = matchDetail.awayTeam?.name ?: "",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .shadow(8.dp, shape = MaterialTheme.shapes.medium),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Tarih: ${formatDate(matchDetail.utcDate ?: "Bilinmiyor")}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Statü: ${translateMatchStatus(matchDetail.status)}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Maç Günü: ${matchDetail.matchday ?: "Bilinmiyor"}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Stadyum: ${matchDetail.venue ?: "Bilinmiyor"}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .shadow(8.dp, shape = MaterialTheme.shapes.medium),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Kartı şeffaf yapıp, iç kısımda gradient uygulayacağız
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
                                            )
                                        )
                                    )
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Skor",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "${matchDetail.score?.fullTime?.home ?: "-"}",
                                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                                            color = Color.White
                                        )
                                        Text(
                                            text = " : ",
                                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${matchDetail.score?.fullTime?.away ?: "-"}",
                                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    if (matchDetail.score?.halfTime != null) {
                                        Text(
                                            text = "İlk Yarı: ${matchDetail.score.halfTime?.home ?: "-"} : ${matchDetail.score.halfTime?.away ?: "-"}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .shadow(8.dp, shape = MaterialTheme.shapes.medium),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Maç Süresi: ${translateDuration(matchDetail.score?.duration)}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Kazanan: ${translateWinner(matchDetail.score?.winner)}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Hakemler:",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                matchDetail.referees?.forEach { referee ->
                                    Text(
                                        text = "${referee.name ?: "Bilinmiyor"} - ${referee.nationality ?: "Bilinmiyor"}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Hata: ${(matchDetailState as UiState.Error).message}", color = Color.Red)
                    }
                }
            }
        }
    }
}
