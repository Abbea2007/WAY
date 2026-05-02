package com.example.wayapp.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.R
import com.example.wayapp.ui.theme.*
data class OnboardingPage(
    @DrawableRes val image: Int,
    val titleBlack: String,
    val titlePurple: String,
    val description: String
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit = {}
) {
    val pages = listOf(
        OnboardingPage(
            image = R.drawable.onboarding1,
            titleBlack = "Encuentra lo que",
            titlePurple = "Importa",
            description = "Explora objetos perdidos y encontrados\ncerca de ti de forma rápida y sencilla."
        ),
        OnboardingPage(
            image = R.drawable.onboarding2,
            titleBlack = "Publica y ayuda a",
            titlePurple = "tu comunidad",
            description = "Publica objetos encontrados o perdidos\ny ayuda a que vuelvan a su dueño."
        ),
        OnboardingPage(
            image = R.drawable.onboarding3,
            titleBlack = "Recibe notificaciones",
            titlePurple = "al instante",
            description = "Entérate en tiempo real cuando haya\nnuevos objetos cerca de ti."
        )
    )

    var currentPage by remember { mutableIntStateOf(0) }

    val page = pages[currentPage]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD5CAF6),
                        Color(0xFFD0CBE5),
                        Color(0xFFFFFFFF)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 1900f)
                )
            )
            .padding(horizontal = 28.dp, vertical = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            Image(
                painter = painterResource(id = page.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = buildAnnotatedString {
                    append(page.titleBlack)
                    append("\n")
                    withStyle(
                        SpanStyle(
                            color = WayPurple,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(page.titlePurple)
                    }
                },
                textAlign = TextAlign.Center,
                color = WayTextPrimary,
                fontSize = 25.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = page.description,
                textAlign = TextAlign.Center,
                color = WayTextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            DotsIndicator(
                totalDots = pages.size,
                selectedIndex = currentPage
            )

            Spacer(modifier = Modifier.height(36.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onFinish
                ) {
                    Text(
                        text = "Skip",
                        color = WayTextMuted,
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = {
                        if (currentPage < pages.lastIndex) {
                            currentPage++
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier.size(
                        width = if (currentPage == pages.lastIndex) 132.dp else 56.dp,
                        height = 56.dp
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WayPurple
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    if (currentPage == pages.lastIndex) {
                        Text(
                            text = "Get started",
                            color = WayWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "Siguiente",
                            tint = WayWhite,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index <= selectedIndex) WayPurple
                        else Color(0xFFD9D9D9)
                    )
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 430, heightDp = 932)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen()
}