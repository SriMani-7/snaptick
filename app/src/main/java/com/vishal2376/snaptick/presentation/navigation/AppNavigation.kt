package com.vishal2376.snaptick.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vishal2376.snaptick.presentation.TaskViewModel
import com.vishal2376.snaptick.presentation.add_edit_screen.AddTaskScreen
import com.vishal2376.snaptick.presentation.add_edit_screen.EditTaskScreen
import com.vishal2376.snaptick.presentation.home_screen.HomeScreen

@Composable
fun AppNavigation(taskViewModel: TaskViewModel) {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = Routes.HomeScreen.name
	) {
		composable(route = Routes.HomeScreen.name) {
			val tasks by taskViewModel.taskList.collectAsStateWithLifecycle(initialValue = emptyList())

			HomeScreen(
				tasks = tasks,
				onEvent = taskViewModel::onEvent,
				onEditTask = { id ->
					navController.navigate(route = "${Routes.EditTaskScreen.name}/$id")
				},
				onAddTask = {
					navController.navigate(route = Routes.AddTaskScreen.name)
				})
		}

		composable(route = Routes.AddTaskScreen.name) {
			AddTaskScreen(
				onEvent = taskViewModel::onEvent,
				onClose = { navController.popBackStack() })
		}

		composable(
			route = "${Routes.EditTaskScreen.name}/{id}",
			arguments = listOf(navArgument("id") {
				type = NavType.IntType
			})
		) { navBackStackEntry ->
			navBackStackEntry.arguments?.getInt("id").let { id ->
				val task = taskViewModel.getTaskById(id!!)
				EditTaskScreen(task = task,
					onEvent = taskViewModel::onEvent,
					onBack = { navController.popBackStack() })
			}
		}
	}
}