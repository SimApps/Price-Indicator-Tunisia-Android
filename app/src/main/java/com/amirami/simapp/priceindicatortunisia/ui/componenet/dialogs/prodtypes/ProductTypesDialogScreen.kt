package com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.prodtypes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.addmodify.AddModifyViewModel
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.getbyproducttype.GetProductByTypesListView
import com.amirami.simapp.priceindicatortunisia.ui.componenet.getbyproducttype.arrayTypeLoader
import com.amirami.simapp.priceindicatortunisia.utils.Constants.Companion.ACTION_SHOW_TYPES

@Composable
fun ProductTypesDialogScreen(
    productTypesDialogViewModel: ProductTypesDialogViewModel,
    navController: NavHostController,
    productsViewModel: ProductsViewModel = hiltViewModel(),
    addModifyViewModel: AddModifyViewModel
) {
    val context = LocalContext.current
    val dialogTitle = productTypesDialogViewModel.prodTypesDialogTitleStates
    val dialogContent = productTypesDialogViewModel.prodTypesDialogContentStates
Log.d("iillnnjbh","dialogTitle "+ dialogTitle)
Log.d("iillnnjbh","dialogContent "+ dialogContent)
    Column(
        modifier = Modifier
            //   .fillMaxSize()
            .wrapContentHeight()
            // .background(Color.White)
            .padding(16.dp)
            .padding(bottom = 60.dp)
            .verticalScroll(rememberScrollState())
        //   verticalArrangement = Arrangement.SpaceEvenly,
        //   horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "$dialogTitle : $dialogContent")
        Spacer(modifier = Modifier.height(30.dp))
        productsViewModel.onActionTypesListViewChanged(ACTION_SHOW_TYPES)

        if (dialogTitle == context.resources.getString(R.string.Catégories) ||
            dialogTitle == context.resources.getString(R.string.SousCatégories) ||
            dialogTitle == context.resources.getString(R.string.sousousCatégories)
        ) {
            productsViewModel.onTypesArraysChange(context.resources.getStringArray(arrayTypeLoader(dialogContent)))
        } else productsViewModel.onTypesArraysChange(context.resources.getStringArray(R.array.productFeauteredTypeArray))

        GetProductByTypesListView(
            productsViewModel = productsViewModel,
            addModifyViewModel= addModifyViewModel,
            from = dialogTitle
        )
    }
}
