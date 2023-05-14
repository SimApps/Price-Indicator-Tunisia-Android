package com.amirami.simapp.priceindicatortunisia.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.amirami.simapp.priceindicatortunisia.screens.settings.SettingViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController



@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme ->  darkColorScheme()//darkColorScheme
        else -> lightColorScheme()//lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


@Composable
fun Theme(settingViewModel: SettingViewModel)  {
    settingViewModel.setisDarkThemeMode(
        if(settingViewModel.darkTheme == Theme.SYSTEM_THEME.theme ||
        settingViewModel.darkTheme ==""){
        isSystemInDarkTheme()
    }else   settingViewModel.darkTheme == Theme.DARK.theme)

}

enum class Theme(var theme: String) {
    DARK("DARK"),
    LIGHT("LIGHT"),
    SYSTEM_THEME("SYSTEM_THEME");

}