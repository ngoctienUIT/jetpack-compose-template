package com.ngoctientnt.template.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.R
import com.ngoctientnt.template.app.navigation.LocalAppNavigator
import com.ngoctientnt.template.app.navigation.MainRoute
import com.ngoctientnt.template.ui.component.button.AppFilledButton
import com.ngoctientnt.template.ui.component.button.AppTextButton
import com.ngoctientnt.template.ui.component.input.AppTextField

@Composable
fun LoginScreen() {
    val navigator = LocalAppNavigator.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        AppTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.login_email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        AppTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.login_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        AppFilledButton(
            text = stringResource(R.string.login_sign_in),
            onClick = { navigator.replaceAll(MainRoute()) },
            fullWidth = true,
            modifier = Modifier.padding(top = 24.dp),
        )

        AppTextButton(
            text = stringResource(R.string.login_forgot_password),
            onClick = { },
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}
