package com.example.colorpalettesapp.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.backendless.rt.command.Command.string
import com.example.colorpalettesapp.R.*
import com.example.colorpalettesapp.domain.model.MessageBarState
import com.example.colorpalettesapp.presentation.component.GoogleButton
import com.example.colorpalettesapp.presentation.component.MessageBar

@Composable
fun LoginContent(
    signedInState: Boolean,
    messageBarState: MessageBarState?,
    onButtonClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.weight(1f)) {
            MessageBar(messageBarState = messageBarState)
        }
        Column(
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp),
                painter = painterResource(id = drawable.ic_google_logo),
                contentDescription = "Google Logo"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = string.sign_in_title),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.h5.fontSize
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .alpha(ContentAlpha.medium),
                text = stringResource(id = string.sign_in_subtitle),
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            GoogleButton(
                loadingState = signedInState,
                onClick = onButtonClicked
            )
        }
    }
}