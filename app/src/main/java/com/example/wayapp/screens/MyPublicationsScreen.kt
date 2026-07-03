package com.example.wayapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayapp.R
import com.example.wayapp.ui.theme.*

data class MyPublication(
    val title: String,
    val status: String, // "Perdido" o "Encontrado"
    val isFound: Boolean,
    val time: String,
    val location: String,
    @DrawableRes val image: Int,
    val isFinished: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPublicationsScreen(
    onBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val isDarkMode = MaterialTheme.colorScheme.background == WayDarkBackground

    val allPublications = listOf(
        MyPublication("Mochila negra", "Perdido", false, "Ayer, 8:00 p.m.", "Edificio A - Aula 201", R.drawable.rectangle18, false),
        MyPublication("Termo azul marino", "Perdido", false, "1 may, 4:20 p.m.", "Gimnasio Universitario", R.drawable.rectangle20, false),
        MyPublication("Libreta con espiral", "Encontrado", true, "30 abr, 11:10 a.m.", "Biblioteca Central", R.drawable.rectangle17, true),
        MyPublication("Paraguas negro", "Encontrado", true, "29 abr, 3:45 p.m.", "Estacionamiento B", R.drawable.rectangle19, true)
    )

    val filteredList = if (selectedTab == 0) {
        allPublications.filter { !it.isFinished }
    } else {
        allPublications.filter { it.isFinished }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis publicaciones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción de compartir */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Compartir",
                            tint = WayPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                TabItem(
                    text = "Publicadas",
                    selected = selectedTab == 0,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = 0 }
                )
                TabItem(
                    text = "Finalizadas",
                    selected = selectedTab == 1,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = 1 }
                )
            }

            HorizontalDivider(color = WayBorder.copy(alpha = 0.5f))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredList) { pub ->
                    MyPublicationCard(pub, isDarkMode)
                }
            }
        }
    }
}

@Composable
fun TabItem(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) WayPurple else WayTextMuted,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(3.dp)
                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                    .background(WayPurple)
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
fun MyPublicationCard(
    pub: MyPublication,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
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
            painter = painterResource(id = pub.image),
            contentDescription = pub.title,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = pub.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (pub.isFound) WayGreen else WayRed)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = pub.status,
                    fontSize = 12.sp,
                    color = if (pub.isFound) WayGreen else WayRed,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = WayTextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = pub.time,
                    fontSize = 12.sp,
                    color = WayTextSecondary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = WayTextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = pub.location,
                    fontSize = 12.sp,
                    color = WayTextSecondary,
                    maxLines = 1
                )
            }
        }

        // Status Badge (Activa / Finalizada)
        Box(
            modifier = Modifier
                .align(Alignment.Bottom)
                .padding(bottom = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (pub.isFinished) Color(0xFFE0E0FF) else WayGreenSoft)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (pub.isFinished) "Finalizada" else "Activa",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (pub.isFinished) WayPurple else WayGreenDark
            )
        }
    }
}
