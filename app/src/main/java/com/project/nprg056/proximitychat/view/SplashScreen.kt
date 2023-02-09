package com.project.nprg056.proximitychat.view

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.nprg056.proximitychat.BuildConfig
import com.project.nprg056.proximitychat.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreenView(home: () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        
        val scale = remember {
            androidx.compose.animation.core.Animatable(0.0f)
        }

        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(800, easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
            )
            delay(1000)
            home()
        }
        
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Icon",
                modifier = Modifier
                    .requiredSize(300.dp)
                    .padding(40.dp)
                    .scale(scale.value)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Proximity Chat",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Text(
            text = "Version - ${BuildConfig.VERSION_NAME}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A)
@Composable
fun PreviewSplashScreen() {
    Column(verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Icon",
            alignment = Alignment.Center, modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        )
        Text(
            text = "asdas",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
