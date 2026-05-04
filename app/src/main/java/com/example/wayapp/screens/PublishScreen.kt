package com.example.wayapp.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.R
import com.example.wayapp.ui.theme.*

enum class PublishType {
    Lost, Found
}

@Composable
fun PublishScreen(
    onBack: () -> Unit = {}
) {
    var selectedType by remember { mutableStateOf(PublishType.Lost) }

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val isDarkMode = MaterialTheme.colorScheme.background == WayDarkBackground

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PublishHeader(onBack = onBack)

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            PublishTypeCard(
                title = "Pérdido",
                image = R.drawable.lost_backpack,
                selected = selectedType == PublishType.Lost,
                isDarkMode = isDarkMode,
                modifier = Modifier.weight(1f),
                onClick = { selectedType = PublishType.Lost }
            )

            PublishTypeCard(
                title = "Encontrado",
                image = R.drawable.found_backpack,
                selected = selectedType == PublishType.Found,
                isDarkMode = isDarkMode,
                modifier = Modifier.weight(1f),
                onClick = { selectedType = PublishType.Found }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {

            Text(
                text = "Información del objeto",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            PhotoUploadCard(isDarkMode = isDarkMode)

            Spacer(modifier = Modifier.height(24.dp))

            PublishInput(
                label = "Nombre del objeto",
                value = name,
                onValueChange = { name = it },
                placeholder = "Ej. Mochila Negra",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(14.dp))

            PublishInput(
                label = "Categoría",
                value = category,
                onValueChange = { category = it },
                placeholder = "Selecciona una categoría",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(14.dp))

            PublishInput(
                label = "Lugar",
                value = place,
                onValueChange = { place = it },
                placeholder = "¿Dónde ocurrió?",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(14.dp))

            PublishInput(
                label = "Fecha y hora",
                value = dateTime,
                onValueChange = { dateTime = it },
                placeholder = "Selecciona fecha y hora",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(14.dp))

            PublishInput(
                label = "Descripción",
                value = description,
                onValueChange = { description = it },
                placeholder = "Agrega detalles que ayuden a encontrarlo",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WayPurple
                )
            ) {
                Text(
                    text = "Publicar",
                    color = WayWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PublishHeader(
    onBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "¿Qué deseas publicar?",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Selecciona el tipo de publicación",
                color = WayTextSecondary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun PublishTypeCard(
    title: String,
    @DrawableRes image: Int,
    selected: Boolean,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = "publish_type_scale"
    )

    Column(
        modifier = modifier
            .height(122.dp)
            .scale(scale)
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) WayPurple.copy(alpha = 0.08f)
            else MaterialTheme.colorScheme.surface)
            .border(
                BorderStroke(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) WayPurple
                    else if (isDarkMode) MaterialTheme.colorScheme.outline
                    else WayBorder
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = WayPurple.copy(alpha = 0.14f)
                ),
                onClick = onClick
            )
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = title,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PhotoUploadCard(
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outline else WayBorder
                ),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(
                    if (isDarkMode) WayDarkBackground else Color(0xFFF5F5F5)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Column {
            Text(
                text = "Agregar fotos",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Max. 5 fotos",
                color = WayTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun PublishInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isDarkMode: Boolean
) {
    Column {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    color = WayTextMuted,
                    fontSize = 13.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WayPurple,
                unfocusedBorderColor = if (isDarkMode) {
                    MaterialTheme.colorScheme.outline
                } else {
                    WayBorder
                },
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = WayPurple
            )
        )
    }
}