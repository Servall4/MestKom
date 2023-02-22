package com.example.mestkom

import android.text.Layout.Alignment
import androidx.compose.runtime.Composable
import java.lang.reflect.Modifier


@Composable
fun SecretScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.center())
    {
        Text(text = "You're authenticated")
    }
}