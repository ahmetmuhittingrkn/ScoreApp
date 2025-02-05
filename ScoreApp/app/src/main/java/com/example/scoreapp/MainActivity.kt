package com.example.scoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scoreapp.ui.screen.LeagueDetailScreen
import com.example.scoreapp.ui.screen.LeagueListScreen
import com.example.scoreapp.ui.screen.TeamDetailScreen
import com.example.scoreapp.ui.screen.MatchDetailScreen
import com.example.scoreapp.ui.theme.ScoreAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ScoreAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "league_list"
                ) {
                    composable("league_list") {
                        LeagueListScreen(navController)
                    }
                    composable("league_detail/{leagueId}") { backStackEntry ->
                        val leagueId = backStackEntry.arguments?.getString("leagueId")?.toIntOrNull()
                        leagueId?.let {
                            LeagueDetailScreen(navController, leagueId = it)
                        }
                    }
                    composable("team_detail/{teamId}") { backStackEntry ->
                        val teamId = backStackEntry.arguments?.getString("teamId")?.toIntOrNull()
                        teamId?.let {
                            TeamDetailScreen(navController, teamId = it)
                        }
                    }
                    composable("match_detail/{matchId}") { backStackEntry ->
                        val matchId = backStackEntry.arguments?.getString("matchId")?.toIntOrNull()
                        matchId?.let {
                            MatchDetailScreen(it)
                        }
                    }
                }
            }
        }
    }
}
