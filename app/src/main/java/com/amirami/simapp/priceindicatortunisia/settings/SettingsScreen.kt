package com.amirami.simapp.priceindicatortunisia.settings

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.amirami.simapp.priceindicatortunisia.R
import com.amirami.simapp.priceindicatortunisia.cartefidelite.room.domain.model.FidCardEntity
import com.amirami.simapp.priceindicatortunisia.google_sign.GoogleAuthUiClient
import com.amirami.simapp.priceindicatortunisia.google_sign.SignInState
import com.amirami.simapp.priceindicatortunisia.google_sign.UserData
import com.amirami.simapp.priceindicatortunisia.products.ProductsViewModel
import com.amirami.simapp.priceindicatortunisia.productsnames.ProductNameViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.TextWithIcon
import com.amirami.simapp.priceindicatortunisia.ui.componenet.barcode.BarCodeViewModel
import com.amirami.simapp.priceindicatortunisia.ui.componenet.bottomnavigationbar.BottomNavigationBar
import com.amirami.simapp.priceindicatortunisia.ui.componenet.dialogs.CustomDialogue
import com.amirami.simapp.priceindicatortunisia.ui.componenet.topbar.TopBar
import com.amirami.simapp.priceindicatortunisia.ui.theme.Theme
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.pranavpandey.android.dynamic.toasts.DynamicToast

@Composable
fun SettingsScreen(
    userData: UserData?,
    googleAuthUiClient: GoogleAuthUiClient,
    state: SignInState,
    barCodeViewModel: BarCodeViewModel,
    productsViewModel: ProductsViewModel,
    navController: NavHostController,
    productNameViewModel: ProductNameViewModel,
    onSignInClick: () -> Unit,
    onSignOut: () -> Unit,
    ondeleteAccount: () -> Unit,
    resetState: () -> Unit,
    settingViewModel : SettingViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if(state.isSignInSuccessful) {
          /*  Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()*/

            resetState()
        }
    }

if(settingViewModel.showCustomDialog)    CustomDialogue(
      dialogueInfo = settingViewModel.dialogueInfo,
        onDismiss = {
            settingViewModel.onDialogueInfoChange(DialogueInfo())
            settingViewModel.onShowCustomDialogChange(false)
            },
    onClick = {


        if (settingViewModel.dialogueInfo.title == context.getString(R.string.nous_contacter_par_email))
            reportProblem(context = context)

        settingViewModel.onShowCustomDialogChange(false)
        settingViewModel.onDialogueInfoChange(DialogueInfo())

    }

    )
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onItemClick = {
                    productsViewModel.resetErreurValue()
                    barCodeViewModel.onfidCardInfo(FidCardEntity())
                    navController.navigate(it.route)
                },

                productsViewModel = productsViewModel
            )
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            AnimatedVisibility(visible = userData?.profilePictureUrl != null && state.isSignInSuccessful) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(userData?.profilePictureUrl != null) {
                        AsyncImage(
                            model = userData.profilePictureUrl,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    if(userData?.username != null) {
                        Text(
                            text = userData.username,
                            textAlign = TextAlign.Center,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }





            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = { settingViewModel.setThemeMode(Theme.SYSTEM_THEME.theme) },
                    label = { Text("System") },

                    leadingIcon = {
                        AnimatedVisibility(visible = settingViewModel.darkTheme == Theme.SYSTEM_THEME.theme) {
                            Icon(
                                Icons.Filled.Done,
                                contentDescription = "System",
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(6.dp))
                AssistChip(
                    onClick = { settingViewModel.setThemeMode(Theme.DARK.theme) },
                    label = { Text("Dark") },
                    leadingIcon = {
                        AnimatedVisibility(visible = settingViewModel.darkTheme == Theme.DARK.theme) {
                            Icon(
                                Icons.Filled.Done,
                                contentDescription ="Dark",
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(6.dp))
                AssistChip(
                    onClick = { settingViewModel.setThemeMode(Theme.LIGHT.theme) },
                    label = { Text("Light") },
                    leadingIcon = {
                        AnimatedVisibility(visible = settingViewModel.darkTheme == Theme.LIGHT.theme) {
                            Icon(
                                Icons.Filled.Done,
                                contentDescription = "Light",
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    }
                )
            }


            Spacer(modifier = Modifier.height(30.dp))
            TextWithIcon(
                text =  if(googleAuthUiClient.getSignedInUser() == null)
                    context.getString(R.string.Connecter)
                else context.getString(R.string.Déconnecter),
                icon = if(googleAuthUiClient.getSignedInUser() == null) R.drawable.ic_signin
                else R.drawable.ic_signout,
                onClick = {
                    if(googleAuthUiClient.getSignedInUser() == null)  onSignInClick()
                    else onSignOut()
                }
            )


            if(googleAuthUiClient.getSignedInUser() != null){
                Spacer(modifier = Modifier.height(30.dp))

                TextWithIcon(
                    text =  context.getString(R.string.Supprimer_compte),
                    icon = R.drawable.ic_signout,
                    onClick = {
                        ondeleteAccount()
                    }
                )
            }



            Spacer(modifier = Modifier.height(30.dp))
            TextWithIcon(
                text = context.getString(R.string.nous_contacter_par_email),
                icon = R.drawable.ic_send,
                onClick = {
                    settingViewModel.onDialogueInfoChange(
                        DialogueInfo(
                            title = context.getString(R.string.nous_contacter_par_email),
                            msg =  context.getString(R.string.jai_un_probleme_message),
                            imageVector =     Icons.Default.Email

                        ))
                    settingViewModel.onShowCustomDialogChange(true)

                }
            )

            Spacer(modifier = Modifier.height(30.dp))
            TextWithIcon(
                text = context.getString(R.string.Qu_est_ce_que_application),
                icon = R.drawable.ic_disclaimer,
                onClick = {
                    settingViewModel.onDialogueInfoChange(DialogueInfo(
                        title = context.getString(R.string.Qu_est_ce_que_application),
                        msg =  context.getString(
                            R.string.Qu_est_ce_que_cette_application,
                            productNameViewModel.productLocalNamesList.size.toString()
                        )
                    ))
                    settingViewModel.onShowCustomDialogChange(true)

                }
            )

            Spacer(modifier = Modifier.height(30.dp))
            TextWithIcon(
                text = context.getString(R.string.more_application),
                icon = R.drawable.ic_baseline_shop_24,
                onClick = {
                    more_apps(context = context)
                }
            )

            Spacer(modifier = Modifier.height(30.dp))
            TextWithIcon(
                text = context.getString(R.string.evaluez_application),
                icon =  R.drawable.ic_rate,
                onClick = {
                    rate(context = context)
                }
            )

            Spacer(modifier = Modifier.height(30.dp))
            TextWithIcon(
                text = context.getString(R.string.licenses),
                icon = R.drawable.ic_licenses,
                onClick = {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                }
            )

        }
    }



}

private fun rate(context : Context) {
    val uri = Uri.parse("market://details?id=" + context.packageName)
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(
        (
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
    )
    try {
        context.startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
            )
        )
    }
}


private fun reportProblem(context : Context) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.app_email)))
        intent.type = "message/rfc822" // "text/plain"

        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.Nous_Contacter))

        context.startActivity(Intent.createChooser(intent, context.getString(R.string.Nous_contacter_Email)))
    } catch (e: Exception) {
        DynamicToast.makeError(context, e.toString(), 9).show()
    }
}

private fun more_apps(context : Context) {
    val uri = Uri.parse("https://play.google.com/store/apps/developer?id=AmiRami")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(
        (
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
    )
    try { context.startActivity(goToMarket) } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/developer?id=AmiRami")
            )
        )
    }
}