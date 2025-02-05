package com.example.scoreapp.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.scoreapp.data.entity.Competition
import com.example.scoreapp.data.entity.Match
import com.example.scoreapp.data.entity.StandingTeam
import com.example.scoreapp.data.entity.Team
import com.example.scoreapp.ui.viewmodel.LeagueDetailViewModel
import com.example.scoreapp.util.UiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class, ExperimentalAnimationApi::class)
@Composable
fun LeagueDetailScreen(
    navController: NavController,
    leagueId: Int,
    viewModel: LeagueDetailViewModel = hiltViewModel()
) {
    val leagueDetailState by viewModel.leagueDetail.collectAsState()
    val teamsState by viewModel.teams.collectAsState()
    val standingsState by viewModel.standings.collectAsState()
    val lastMatchesState by viewModel.lastMatches.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Puan Durumu", "Son Maçlar")

    LaunchedEffect(leagueId) {
        viewModel.loadLeagueDetail(leagueId)
        viewModel.loadTeams(leagueId)
        viewModel.loadStandings(leagueId)
        viewModel.loadLastMatches(leagueId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lig Detayı", style = MaterialTheme.typography.titleLarge) },
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
            when (leagueDetailState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val league = (leagueDetailState as UiState.Success<Competition>).data
                    Column(modifier = Modifier.fillMaxSize()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .shadow(8.dp, shape = MaterialTheme.shapes.medium),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                GlideImage(
                                    model = league.emblem,
                                    contentDescription = league.name,
                                    modifier = Modifier.size(100.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = league.name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Ülke: ${league.area.name}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Kod: ${league.code}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                        // TabRow bölümü
                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = {
                                        Text(
                                            title,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        AnimatedContent(targetState = selectedTabIndex) { tabIndex ->
                            when (tabIndex) {
                                0 -> {
                                    // Puan Durumu içeriği
                                    when (standingsState) {
                                        is UiState.Loading -> {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }
                                        is UiState.Success -> {
                                            val standings = (standingsState as UiState.Success<List<StandingTeam>>).data
                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(horizontal = 16.dp)
                                            ) {
                                                items(standings) { team ->
                                                    StandingItem(team) {
                                                        navController.navigate("team_detail/${team.team.id}")
                                                    }
                                                }
                                            }
                                        }
                                        is UiState.Error -> {
                                            Text(text = "Puan durumu yüklenemedi", color = Color.Red)
                                        }
                                    }
                                }
                                1 -> {
                                    // Son Maçlar içeriği
                                    when (lastMatchesState) {
                                        is UiState.Loading -> {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }
                                        is UiState.Success -> {
                                            val matches = (lastMatchesState as UiState.Success<List<Match>>).data
                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(horizontal = 16.dp)
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
                        }
                    }
                }
                is UiState.Error -> {
                    val errorMessage = (leagueDetailState as UiState.Error).message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Hata: $errorMessage", color = Color.Red)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TeamItem(team: Team, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = team.crest ?: "",
                contentDescription = team.name,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 16.dp)
            )

            Column {
                Text(text = team.name, fontWeight = FontWeight.Bold)
                Text(text = "Stadyum: ${team.venue ?: "Bilinmiyor"}", color = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StandingItem(team: StandingTeam, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${team.position}.", fontWeight = FontWeight.Bold)
            GlideImage(
                model = team.team.crest,
                contentDescription = team.team.name,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = team.team.name, fontWeight = FontWeight.Bold)
                Text(text = "Puan: ${team.points}, Averaj: ${team.goalDifference}")
            }
        }
    }
}
