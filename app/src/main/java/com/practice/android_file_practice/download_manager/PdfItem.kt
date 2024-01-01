package com.practice.android_file_practice.download_manager

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PdfItem(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(width = 6.dp, color = Color.Gray)
            .shadow(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(modifier = Modifier.padding(8.dp), imageVector = Icons.Default.AccountBox, contentDescription = "icon")
        Text(modifier = Modifier.padding(8.dp), text = title)
    }
}