package com.ngoctientnt.template.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.ngoctientnt.template.app.navigation.LocalAppNavigator
import com.ngoctientnt.template.feature.locale.LocaleViewModel
import com.ngoctientnt.template.ui.component.LanguageSelector

@Composable
fun ProfileScreen(
    localeViewModel: LocaleViewModel = hiltViewModel(),
) {
    val navigator = LocalAppNavigator.current
    val pendingLanguage by localeViewModel.pendingLanguage.collectAsStateWithLifecycle()
    val canApplyLanguage by localeViewModel.canApplyLanguage.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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

        Button(
            onClick = navigator::pop,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(stringResource(R.string.action_back))
        }
    }
}
