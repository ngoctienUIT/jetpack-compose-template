package com.ngoctientnt.template.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ngoctientnt.template.R
import com.ngoctientnt.template.feature.locale.LocaleViewModel
import com.ngoctientnt.template.feature.theme.ThemeViewModel
import com.ngoctientnt.template.ui.component.LanguageSelector
import com.ngoctientnt.template.ui.component.ThemeSelector
import com.ngoctientnt.template.ui.component.button.AppOutlinedButton

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    localeViewModel: LocaleViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val isLoggingOut by profileViewModel.isLoggingOut.collectAsStateWithLifecycle()
    val pendingLanguage by localeViewModel.pendingLanguage.collectAsStateWithLifecycle()
    val canApplyLanguage by localeViewModel.canApplyLanguage.collectAsStateWithLifecycle()
    val pendingThemeMode by themeViewModel.pendingThemeMode.collectAsStateWithLifecycle()
    val canApplyTheme by themeViewModel.canApplyTheme.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        LanguageSelector(
            selectedLanguage = pendingLanguage,
            onLanguageSelected = localeViewModel::selectLanguage,
            onApply = localeViewModel::applyLanguage,
            canApply = canApplyLanguage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        ThemeSelector(
            selectedThemeMode = pendingThemeMode,
            onThemeModeSelected = themeViewModel::selectThemeMode,
            onApply = themeViewModel::applyThemeMode,
            canApply = canApplyTheme,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        AppOutlinedButton(
            text = stringResource(R.string.profile_logout),
            onClick = profileViewModel::logout,
            enabled = !isLoggingOut,
            loading = isLoggingOut,
            fullWidth = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
        )
    }
}
