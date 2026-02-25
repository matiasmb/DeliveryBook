package com.deliverybook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.deliverybook.core.AppConstants
import com.deliverybook.domain.usecase.ObserveRecentContactsUseCase
import com.deliverybook.feature.contacts.detail.ContactDetailScreen
import com.deliverybook.feature.contacts.detail.ContactDetailViewModel
import com.deliverybook.feature.contacts.list.ContactsListScreen
import com.deliverybook.feature.contacts.list.ContactsListViewModel
import com.deliverybook.feature.splash.SplashScreen
import com.deliverybook.ui.theme.DeliveryBookTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var observeRecentContactsUseCase: ObserveRecentContactsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { false }
        super.onCreate(savedInstanceState)

        val showSplash = mutableStateOf(true)
        lifecycleScope.launch {
            val start = System.currentTimeMillis()
            withContext(Dispatchers.IO) {
                observeRecentContactsUseCase(AppConstants.Search.RECENT_LIMIT).first()
            }
            val elapsed = System.currentTimeMillis() - start
            val remaining = AppConstants.Splash.MIN_DISPLAY_MS - elapsed
            if (remaining > 0) delay(remaining)
            showSplash.value = false
        }

        setContent {
            DeliveryBookTheme {
                Surface {
                    RootContent(showSplash = showSplash)
                }
            }
        }
    }
}

@Preview(name = "RootContent (Splash)", showBackground = true)
@Composable
private fun RootContentSplashPreview() {
    DeliveryBookTheme {
        Surface {
            RootContent(showSplash = remember { mutableStateOf(true) })
        }
    }
}

@Composable
private fun RootContent(showSplash: MutableState<Boolean>, modifier: Modifier = Modifier) {
    val show by showSplash
    if (show) {
        SplashScreen(modifier = modifier)
    } else {
        AppNavHost(modifier = modifier)
    }
}

@Composable
private fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppConstants.Navigation.ROUTE_CONTACTS_LIST,
        modifier = modifier
    ) {
        composable(route = AppConstants.Navigation.ROUTE_CONTACTS_LIST) {
            val viewModel: ContactsListViewModel = hiltViewModel()
            ContactsListScreen(
                viewModel = viewModel,
                onContactClick = { dni ->
                    navController.navigate("${AppConstants.Navigation.ROUTE_CONTACT_DETAIL}?${AppConstants.Navigation.ARG_DNI}=$dni")
                },
                onCreateNewContact = {
                    navController.navigate(AppConstants.Navigation.ROUTE_CONTACT_DETAIL)
                }
            )
        }
        composable(
            route = "${AppConstants.Navigation.ROUTE_CONTACT_DETAIL}?${AppConstants.Navigation.ARG_DNI}={${AppConstants.Navigation.ARG_DNI}}",
            arguments = listOf(
                navArgument(AppConstants.Navigation.ARG_DNI) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val viewModel: ContactDetailViewModel =
                hiltViewModel(backStackEntry)
            ContactDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

