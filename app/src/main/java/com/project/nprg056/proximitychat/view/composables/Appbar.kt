package com.project.nprg056.proximitychat.view.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.project.nprg056.proximitychat.R

@Composable
fun Appbar(title: String, subTitle: String? = null, action: () -> Unit) {
    SmallTopAppBar(
        title = {
            if (subTitle != null)
                Column {
                    Text(text = title)
                    Text(text = subTitle, style = MaterialTheme.typography.titleSmall)
                }
            else
                Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = action
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_description)
                )
            }
        }
    )
}