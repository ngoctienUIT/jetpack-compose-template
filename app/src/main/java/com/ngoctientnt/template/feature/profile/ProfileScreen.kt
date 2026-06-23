package com.ngoctientnt.template.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Settings
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
import com.ngoctientnt.template.ui.component.button.AppTextButton
import com.ngoctientnt.template.ui.component.toast.LocalAppToastController
import com.ngoctientnt.template.ui.component.dialog.AppConfirmDialog
import com.ngoctientnt.template.ui.component.dialog.rememberAppDialogState
import com.ngoctientnt.template.ui.component.sheet.AppActionBottomSheet
import com.ngoctientnt.template.ui.component.sheet.AppSheetAction
import com.ngoctientnt.template.ui.component.sheet.rememberAppBottomSheetState

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

    val logoutDialogState = rememberAppDialogState()
    val optionsSheetState = rememberAppBottomSheetState()
    val toast = LocalAppToastController.current
    val languageAppliedMessage = stringResource(R.string.toast_language_applied)
    val themeAppliedMessage = stringResource(R.string.toast_theme_applied)

    AppConfirmDialog(
        state = logoutDialogState,
        title = stringResource(R.string.logout_confirm_title),
        message = stringResource(R.string.logout_confirm_message),
        confirmText = stringResource(R.string.profile_logout),
        dismissText = stringResource(R.string.action_cancel),
        onConfirm = {
            logoutDialogState.hide()
            profileViewModel.logout()
        },
        destructive = true,
        loading = isLoggingOut,
    )

    AppActionBottomSheet(
        state = optionsSheetState,
        title = stringResource(R.string.profile_more_options_title),
        subtitle = stringResource(R.string.profile_more_options_subtitle),
        actions = listOf(
            AppSheetAction(
                label = stringResource(R.string.settings_theme),
                icon = Icons.Outlined.Settings,
                onClick = { /* TODO: navigate to theme settings */ },
            ),
            AppSheetAction(
                label = stringResource(R.string.settings_language),
                icon = Icons.Outlined.Language,
                onClick = { /* TODO: navigate to language settings */ },
            ),
        ),
    )

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
            onApply = {
                localeViewModel.applyLanguage()
                toast.showSuccess(languageAppliedMessage)
            },
            canApply = canApplyLanguage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        ThemeSelector(
            selectedThemeMode = pendingThemeMode,
            onThemeModeSelected = themeViewModel::selectThemeMode,
            onApply = {
                themeViewModel.applyThemeMode()
                toast.showSuccess(themeAppliedMessage)
            },
            canApply = canApplyTheme,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        AppTextButton(
            text = stringResource(R.string.profile_more_options),
            onClick = optionsSheetState::show,
            fullWidth = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        AppOutlinedButton(
            text = stringResource(R.string.profile_logout),
            onClick = logoutDialogState::show,
            enabled = !isLoggingOut,
            loading = isLoggingOut,
            fullWidth = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )
    }
}
