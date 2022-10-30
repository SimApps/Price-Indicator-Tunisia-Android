package com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.utils.AutoCompleteBox
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.utils.AutoCompleteSearchBarTag
import com.amirami.simapp.priceindicatortunisia.ui.componenet.searchview.utils.asAutoCompleteEntities
import com.amirami.simapp.priceindicatortunisia.ui.navigation.ListScreens
import com.amirami.simapp.priceindicatortunisia.ui.screens.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.HOME_SCREEN
import com.amirami.simapp.priceindicatortunisia.utils.Functions
import com.amirami.simapp.priceindicatortunisia.utils.Functions.capitalizeWords
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(
    navController: NavHostController,
    barCodeViewModel: BarCodeViewModel,
    prodname: ArrayList<String>,
    productsViewModel: ProductsViewModel,
    searchViewModel: SearchViewModel

) {
    val context = LocalContext.current
    val backStackEntry = navController.currentBackStackEntryAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scope = rememberCoroutineScope()

    /**
     * to get previous screen
    navController.backQueue.forEach { entry ->
    Log.d("TAG", "${entry.destination.route}")
    }
     */


    when (navBackStackEntry?.destination?.route) {
        ListScreens.Accueil.Route -> {
            searchViewModel.onsearchViewVisibilityStatesChanged(true)
        }
        ListScreens.AddModify.Route -> {
            searchViewModel.onsearchViewVisibilityStatesChanged(true)
        }

        else -> {
            searchViewModel.onsearchViewVisibilityStatesChanged(false)
        }
    }

    val autoCompleteEntities = prodname.asAutoCompleteEntities(
        filter = { item, query ->
            //  item.lowercase(Locale.getDefault()).startsWith(query.lowercase(Locale.getDefault()))
            item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
        }
    )
    if (barCodeViewModel.fidCardBarCodeInfo.value != "" && barCodeViewModel.sendBarCodeTo == HOME_SCREEN) {
        val barecodeValue = if (Functions.isNumber(barCodeViewModel.fidCardBarCodeInfo.value)) {
            Functions.removeLeadingZeroes(barCodeViewModel.fidCardBarCodeInfo.value)
        } else barCodeViewModel.fidCardBarCodeInfo.value

        barecodeValue.let {
            searchViewModel.onsearchValue(it)
            productsViewModel.getProds(Functions.searchType(it), it.capitalizeWords())

            val fidcard = FidCardEntity(name = "", value = "", barecodeformat = -1, barecodetype = -1)
            barCodeViewModel.onfidCardInfo(fidcard)
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    AnimatedVisibility(
        visible = searchViewModel.searchViewVisibilityStates,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            AutoCompleteBox(
                items = autoCompleteEntities,
                itemContent = { prodNameList ->
                    SearchAutoCompleteItem(prodNameList.value)
                }
            ) {
                // var value by remember { mutableStateOf("") }
                val view = LocalView.current

                onItemSelected { item ->
                    item.value.let {
                        searchViewModel.onsearchValue(it)
                        productsViewModel.getProds(Functions.searchType(it), it)
                    }
                    filter(searchViewModel.searchValue)

                    // searchViewModel.onsearchValue("")

                    scope.launch {
                        keyboardController!!.hide()
                        delay(100) //  delay here IS nessesary to hide keyboard + scope
                        view.clearFocus()
                    }
                }

                IconsInSearchView(
                    modifier = Modifier.testTag(AutoCompleteSearchBarTag),
                    value = searchViewModel.searchValue,
                    label = context.getString(R.string.search_hint),
                    onDoneActionClick = {
                        productsViewModel.getProds(
                            Functions.searchType(searchViewModel.searchValue),
                            searchViewModel.searchValue.capitalizeWords()
                        )
                        filter(searchViewModel.searchValue)

                        view.clearFocus()
                    },
                    onClearClick = {
                        searchViewModel.onsearchValue("")
                        filter(searchViewModel.searchValue)
                        view.clearFocus()
                    },
                    onScanClick = {
                        barCodeViewModel.onsendBarCodeTo(HOME_SCREEN)
                        navController.navigate(ListScreens.BarCodeCameraPreview.Route)
                    },
                    onFocusChanged = { focusState ->
                        //   isSearching = isKeyboardOpen != Keyboard.Closed
                        isSearching = focusState.isFocused
                    },
                    onValueChanged = { query ->
                        searchViewModel.onsearchValue(query) // value = query
                        filter(searchViewModel.searchValue)
                    }
                )
            }
        }
    )
}

@Composable
fun SearchAutoCompleteItem(product: String?) {
    Card(
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                Column {
                    if (product != null) {
                        Text(text = product, style = MaterialTheme.typography.subtitle2)
                    }
                    // Text(text = product.phoneNumber, style = MaterialTheme.typography.subtitle2)
                    // Text(text = product.email, style = MaterialTheme.typography.subtitle2)
                }
            }
        }
    }
}

enum class Keyboard {
    Opened, Closed
}

//    val isKeyboardOpen by keyboardAsState()
@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}
