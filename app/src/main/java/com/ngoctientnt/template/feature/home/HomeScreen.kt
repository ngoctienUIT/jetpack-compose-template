package com.ngoctientnt.template.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.ngoctientnt.template.ui.component.button.AppFilledButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.R
import com.ngoctientnt.template.app.navigation.DetailRoute
import com.ngoctientnt.template.app.navigation.LocalAppNavigator

@Composable
fun HomeScreen() {
    val navigator = LocalAppNavigator.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.home_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        AppFilledButton(
            onClick = { navigator.navigate(DetailRoute(id = "42")) },
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.home_open_detail),
        )
    }
}
