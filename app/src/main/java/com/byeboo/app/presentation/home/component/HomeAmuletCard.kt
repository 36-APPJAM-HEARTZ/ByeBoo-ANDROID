package com.byeboo.app.presentation.home.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun HomeAmuletCard(
    frontImageRes: Int,
    backImageRes: Int,
    description: String,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "rotation"
    )

    val showBack = rotation >= 90f
    val density = LocalDensity.current.density
    val cameraDistance = remember { 12f * density }

    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .aspectRatio(290 / 419f)
            .graphicsLayer {
                rotationY = rotation
                this.cameraDistance = cameraDistance
            }.clickable(enabled = !isFlipped) { onFlip() }
    ) {
        if (showBack) {
            Box(
                modifier =
                Modifier
                    .matchParentSize()
                    .graphicsLayer { rotationY = 180f }
            ) {
                Image(
                    painter = painterResource(backImageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = description,
                    style = ByeBooTheme.typography.body6,
                    color = ByeBooTheme.colors.secondary50,
                    textAlign = TextAlign.Center,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(37.dp))
                        .padding(top = screenHeightDp(131.dp))
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(frontImageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
