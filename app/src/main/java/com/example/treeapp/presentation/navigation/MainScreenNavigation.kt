package com.example.treeapp.presentation.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.treeapp.presentation.MainScreen

fun NavGraphBuilder.mainScreen(navController: NavHostController) {
    composable("main_screen/{id}/{name}", arguments = listOf(
        navArgument("id") { type = NavType.IntType },
        navArgument("name") { type = NavType.StringType },
    )
    ) {
        val id = remember { it.arguments?.getInt("id") }
        val name = remember { it.arguments?.getString("name") }
        MainScreen(navController = navController, id = id ?: -1, name = name ?: "")
    }
}