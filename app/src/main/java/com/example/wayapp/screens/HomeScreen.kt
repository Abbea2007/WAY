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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
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
import kotlinx.coroutines.selects.select

data class LostItem(
    val title: String,
    val status: String,
    val isFound: Boolean,
    val time: String,
    val location: String,
    @DrawableRes val image: Int
)

enum class BottomNavItem {
    Home, Search, Notifications, Profile
}

@Composable
fun HomeScreen() {
    val items = listOf(
        LostItem("Audífonos inalámbricos", "Encontrado", true, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle17),
        LostItem("Mochila Negra", "Pérdido", false, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle18),
        LostItem("Llaves de carro", "Pérdido", false, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle19),
        LostItem("Termo para café", "Encontrado", true, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle20),
        LostItem("Audífonos inalámbricos", "Encontrado", true, "Hoy, 10:30 a.m.", "Biblioteca central", R.drawable.rectangle21)
    )

    var selectedItem by remember {mutableStateOf(BottomNavItem.Home)}
    var searchText by remember {mutableStateOf("")}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WayBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 72.dp, bottom = 104.dp)
        ) {
            HomeHeader()

            Spacer(modifier = Modifier.height(18.dp))
            HomeSearchBar(
                value = searchText,
                onValueChange = { searchText = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            HomeTabs()

            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader()

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(items) { item ->
                    ObjectCard(item = item)
                }
            }
        }

        HomeBottomBar(
            selectedItem = selectedItem,
            onItemSelected = { selectedItem = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun HomeHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Hola, Abea 👋",
            color = WayTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "¿Qué estás buscando hoy?",
            color = WayTextSecondary,
            fontSize = 14.sp
        )
    }
}

@Composable
fun HomeSearchBar(
    value: String,
    onValueChange: (String) -> Unit
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
                color = WayTextPrimary,
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
                unfocusedBorderColor = WayBorder,
                focusedContainerColor = WayWhite,
                unfocusedContainerColor = WayWhite,
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
                .background(WayWhite)
                .border(
                    BorderStroke(1.dp, WayBorder),
                    RoundedCornerShape(14.dp)
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
fun HomeTabs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(WayWhite)
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
            color = if (active) WayWhite else WayTextPrimary,
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
            color = WayTextPrimary,
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
fun ObjectCard(item: LostItem) {
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
            .background(WayWhite)
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
                color = WayTextPrimary,
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
                    tint = WayTextPrimary,
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
            tint = WayTextPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun HomeBottomBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
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
            .background(WayWhite)
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
            NavItem(
                label = "Home",
                icon = Icons.Filled.Home,
                active = selectedItem == BottomNavItem.Home,
                onClick = { onItemSelected(BottomNavItem.Home) }
            )

            NavItem(
                label = "Buscar",
                icon = Icons.Filled.Search,
                active = selectedItem == BottomNavItem.Search,
                onClick = { onItemSelected(BottomNavItem.Search) }
            )

            Spacer(modifier = Modifier.width(76.dp))

            NavItem(
                label = "Notificaciones",
                icon = Icons.Filled.Notifications,
                active = selectedItem == BottomNavItem.Notifications,
                onClick = { onItemSelected(BottomNavItem.Notifications) }
            )

            NavItem(
                label = "Perfil",
                icon = Icons.Filled.Person,
                active = selectedItem == BottomNavItem.Profile,
                onClick = { onItemSelected(BottomNavItem.Profile) }
            )
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.Center)
                .offset(y = (-8).dp)
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
                        // Aquí luego navegamos a publicar
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
            .width(68.dp)
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
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}