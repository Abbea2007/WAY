package com.example.wayapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.ui.theme.*
import com.example.wayapp.R

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit = {}
) {
    var isLogin by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WayBackground)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 92.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isLogin) "Bienvenido de nuevo" else "Crea tu cuenta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = WayTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isLogin) "Inicia sesión para continuar" else "Únete a la comunidad WAY",
                fontSize = 14.sp,
                color = WayTextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthSwitch(
                isLogin = isLogin,
                onChange = { isLogin = it }
            )

            Spacer(modifier = Modifier.height(36.dp))

            if (!isLogin) {
                AuthInput(
                    placeholder = "Nombre completo",
                    icon = { Icon(Icons.Outlined.Person, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            AuthInput(
                placeholder = "Correo electrónico",
                icon = { Icon(Icons.Outlined.Email, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthInput(
                placeholder = "Contraseña",
                isPassword = true,
                icon = { Icon(Icons.Outlined.Lock, contentDescription = null) }
            )

            if (!isLogin) {
                Spacer(modifier = Modifier.height(16.dp))

                AuthInput(
                    placeholder = "Confirmar contraseña",
                    isPassword = true,
                    icon = { Icon(Icons.Outlined.Lock, contentDescription = null) }
                )
            }

            if (isLogin) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = WayPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryAuthButton(
                text = if (isLogin) "Iniciar Sesión" else "Crear Cuenta",
                onClick = onLoginSuccess
            )

            Spacer(modifier = Modifier.height(32.dp))

            DividerWithText()

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialButton(
                    text = "Google",
                    iconRes = R.drawable.ic_google,
                    modifier = Modifier.weight(1f)
                )

                SocialButton(
                    text = "Facebook",
                    iconRes = R.drawable.ic_facebook,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text(
                    text = if (isLogin) "¿No tienes cuenta? " else "¿Ya tienes cuenta? ",
                    color = WayTextSecondary,
                    fontSize = 14.sp
                )

                Text(
                    text = if (isLogin) "Crear cuenta" else "Iniciar Sesión",
                    color = WayPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        isLogin = !isLogin
                    }
                )
            }
        }
    }
}

@Composable
fun AuthSwitch(
    isLogin: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AuthSwitchItem(
            text = "Iniciar Sesión",
            active = isLogin,
            modifier = Modifier.weight(1f),
            onClick = { onChange(true) }
        )

        AuthSwitchItem(
            text = "Crear Cuenta",
            active = !isLogin,
            modifier = Modifier.weight(1f),
            onClick = { onChange(false) }
        )
    }
}

@Composable
fun AuthSwitchItem(
    text: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) WayPurple else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (active) WayWhite else WayTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AuthInput(
    placeholder: String,
    isPassword: Boolean = false,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(WayWhite)
            .border(
                width = 1.dp,
                color = WayBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(22.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides WayTextMuted) {
                icon()
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = placeholder,
            color = WayTextMuted,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        if (isPassword) {
            Icon(
                imageVector = Icons.Outlined.Visibility,
                contentDescription = null,
                tint = WayTextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun PrimaryAuthButton(
    text: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "primary_button_scale"
    )

    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .scale(scale),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            WayPurpleGradientStart,
                            WayPurpleGradientEnd
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = WayWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DividerWithText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = WayBorder
        )

        Text(
            text = "o continúa con",
            color = WayTextMuted,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = WayBorder
        )
    }
}

@Composable
fun SocialButton(
    text: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "social_button_scale"
    )

    Row(
        modifier = modifier
            .height(56.dp)
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .background(WayWhite)
            .border(
                BorderStroke(1.dp, WayBorder),
                RoundedCornerShape(14.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = WayPurple.copy(alpha = 0.12f)
                ),
                onClick = onClick
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            color = WayTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}