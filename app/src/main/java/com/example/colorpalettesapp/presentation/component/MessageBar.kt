package com.example.colorpalettesapp.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.colorpalettesapp.domain.model.MessageBarState
import com.example.colorpalettesapp.ui.theme.ErrorRed
import kotlinx.coroutines.delay

@Composable
fun MessageBar(messageBarState: MessageBarState?) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = messageBarState) {
        startAnimation = true
        delay(3000)
        startAnimation = false
    }

    AnimatedVisibility(
        visible = messageBarState?.error != null && startAnimation,
        enter = expandVertically(
            animationSpec = tween(300),
            expandFrom = Alignment.Top
        ),
        exit = shrinkVertically(
            animationSpec = tween(300),
            shrinkTowards = Alignment.Top
        )
    ) {
        messageBarState?.error?.message?.let {
            Message(messageBarState = messageBarState)
        }
    }
}

@Composable
fun Message(messageBarState: MessageBarState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = ErrorRed)
            .padding(horizontal = 20.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Warning Icon",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(12.dp))
        messageBarState.error?.message?.let {
            Text(
                text = it,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.button.fontSize,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
@Preview
fun MessageBarPreview() {
    Message(
        messageBarState = MessageBarState(
            error = Exception("Something went wrong.")
        )
    )
}