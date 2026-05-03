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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.R
import com.example.wayapp.ui.theme.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.wayapp.data.FirestoreManager
import com.example.wayapp.model.ObjetoReportado

data class LostItem(
    val title: String,
    val status: String,
    val isFound: Boolean,
    val time: String,
    val location: String,
    @DrawableRes val image: Int
)

enum class BottomNavItem {
    Home, Search, Notifications, Settings
}

@Composable
fun HomeScreen(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    // 1. INSTANCIAMOS EL BACKEND Y EL CONTEXTO
    val firestoreManager = remember { FirestoreManager() }
    val context = LocalContext.current

    val items = listOf(
        LostItem("Audífonos inalámbricos", "Encontrado", true, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle17),
        LostItem("Mochila Negra", "Pérdido", false, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle18),
        LostItem("Llaves de carro", "Pérdido", false, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle19),
        LostItem("Termo para café", "Encontrado", true, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle20),
        LostItem("Audífonos inalámbricos", "Encontrado", true, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle21)
    )

    var selectedItem by remember { mutableStateOf(BottomNavItem.Home) }
    var searchText by remember { mutableStateOf("") }
    val isDarkMode = MaterialTheme.colorScheme.background == WayDarkBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 72.dp, bottom = 104.dp)
        ) {
            when (selectedItem) {
                BottomNavItem.Home -> {
                    HomeHeader(isDarkMode = isDarkMode)

                    Spacer(modifier = Modifier.height(18.dp))

                    HomeSearchBar(
                        value = searchText,
                        onValueChange = { searchText = it },
                        isDarkMode = isDarkMode
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    HomeTabs(isDarkMode = isDarkMode)

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionHeader()

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(items) { item ->
                            ObjectCard(
                                item = item,
                                isDarkMode = isDarkMode
                            )
                        }
                    }
                }

                BottomNavItem.Settings -> {
                    SettingsScreen(
                        selectedTheme = themeMode,
                        onThemeChange = onThemeChange
                    )
                }

                BottomNavItem.Search -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Pantalla Buscar", color = MaterialTheme.colorScheme.onBackground)
                    }
                }

                BottomNavItem.Notifications -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Notificaciones", color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
        }

        // 2. MODIFICAMOS LA BARRA INFERIOR PARA MANDAR EL OBJETO
        HomeBottomBar(
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            onAddClick = {
                // Creamos un objeto de prueba
                val objetoPrueba = ObjetoReportado(
                    titulo = "Termo Stanley Verde",
                    descripcion = "Lo dejé olvidado en la mesa de la cafetería.",
                    estado = "PERDIDO",
                    ubicacionGeneral = "Cafetería UAM",
                    fechaHora = "Hoy, 1:50 p.m.",
                    idUsuarioReporta = "usuario_demo_123"
                )

                // Lo mandamos a la nube
                firestoreManager.agregarObjeto(objetoPrueba) { exito, mensaje ->
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun HomeHeader(
    onProfileClick: () -> Unit = {},
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Hola, Abea 👋",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "¿Qué estás buscando hoy?",
                color = WayTextSecondary,
                fontSize = 14.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onProfileClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_photo),
                contentDescription = "Perfil",
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (isDarkMode)
                            MaterialTheme.colorScheme.outline
                        else
                            WayWhite,
                        shape = CircleShape
                    ),

                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun HomeSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                    text = "Buscar objetos, categorías, lugares...",
                    color = WayTextMuted,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = WayTextMuted,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = Color.Black.copy(alpha = 0.08f),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                ),
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

        Box(
            modifier = Modifier
                .size(48.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(14.dp),
                    ambientColor = Color.Black.copy(alpha = 0.08f),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                )
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = if (isDarkMode)
                        MaterialTheme.colorScheme.outline
                    else
                        WayBorder,
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = "Filtros",
                tint = WayPurple,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun HomeTabs(isDarkMode: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .then(
                if (isDarkMode) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(24.dp)
                    )
                } else Modifier
            )
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TabPill("Todos", active = true, modifier = Modifier.weight(1f))
        TabPill("Pérdidos", active = false, modifier = Modifier.weight(1f))
        TabPill("Encontrados", active = false, modifier = Modifier.weight(1f))
    }
}

@Composable
fun TabPill(
    text: String,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) WayPurple else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (active) WayWhite else MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SectionHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Publicaciones Recientes",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "ver todo",
            color = WayPurple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ObjectCard(
    item: LostItem,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .then(
                if (isDarkMode) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else Modifier
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentDescription = item.title,
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = if (item.isFound) WayGreen else WayRed,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(if (item.isFound) "E  ${item.status}" else "P  ${item.status}")
                    }

                    withStyle(
                        SpanStyle(
                            color = WayTextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(" - ${item.time}")
                    }
                },
                fontSize = 12.sp,
                maxLines = 1
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = item.location,
                    color = WayTextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }

        Icon(
            imageVector = Icons.Outlined.BookmarkBorder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun HomeBottomBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    onAddClick: () -> Unit = {}, // <-- 1. AGREGAMOS ESTE PARÁMETRO
    modifier: Modifier = Modifier
) {
    val plusInteractionSource = remember { MutableInteractionSource() }
    val plusPressed by plusInteractionSource.collectIsPressedAsState()

    val plusScale by animateFloatAsState(
        targetValue = if (plusPressed) 0.92f else 1f,
        label = "plus_button_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(MaterialTheme.colorScheme.surface)
            .shadow(
                elevation = 8.dp,
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
            .padding(horizontal = 22.dp)
            .padding(bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(label = "Home", icon = Icons.Filled.Home, active = selectedItem == BottomNavItem.Home, onClick = { onItemSelected(BottomNavItem.Home) })
            NavItem(label = "Buscar", icon = Icons.Filled.Search, active = selectedItem == BottomNavItem.Search, onClick = { onItemSelected(BottomNavItem.Search) })
            Spacer(modifier = Modifier.width(76.dp))
            NavItem(label = "Notificaciones", icon = Icons.Filled.Notifications, active = selectedItem == BottomNavItem.Notifications, onClick = { onItemSelected(BottomNavItem.Notifications) })
            NavItem(label = "Configuración", icon = Icons.Filled.Settings, active = selectedItem == BottomNavItem.Settings, onClick = { onItemSelected(BottomNavItem.Settings) })
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.Center)
                .offset(y = (-5).dp)
                .scale(plusScale)
                .shadow(
                    elevation = 18.dp,
                    shape = CircleShape,
                    ambientColor = WayPurple.copy(alpha = 0.35f),
                    spotColor = WayPurple.copy(alpha = 0.35f)
                )
                .clip(CircleShape)
                .background(WayPurple)
                .clickable(
                    interactionSource = plusInteractionSource,
                    indication = ripple(
                        bounded = true,
                        color = WayWhite.copy(alpha = 0.25f)
                    ),
                    onClick = {
                        onAddClick() // <-- 2. EJECUTAMOS LA ACCIÓN AQUÍ
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Publicar",
                tint = WayWhite,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun NavItem(
    label: String,
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        label = "nav_item_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .width(67.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = false,
                    color = WayPurple.copy(alpha = 0.16f)
                ),
                onClick = onClick
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (active) WayPurple else WayTextMuted,
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = label,
            color = if (active) WayPurple else WayTextMuted,
            fontSize = 9.sp,
            maxLines = 1
        )
    }
}