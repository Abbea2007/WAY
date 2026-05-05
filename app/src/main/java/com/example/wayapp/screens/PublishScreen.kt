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
import com.example.wayapp.data.FirestoreManager
import com.example.wayapp.model.ObjetoReportado
import com.example.wayapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class PublishType {
    Lost, Found
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishScreen(
    onBack: () -> Unit = {}
) {
    // Instancia de FirestoreManager
    val firestoreManager = remember { FirestoreManager() }

    var selectedType by remember { mutableStateOf(PublishType.Lost) }

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Variables para Fecha y Hora
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    // Estados para mostrar los diálogos
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

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
                image = R.drawable.lost_backpack, // Asegúrate de que el nombre coincide con tus recursos
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

            // Dropdown de Categoría
            CategoryDropdownMenu(
                selectedCategory = category,
                onCategorySelected = { category = it },
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

            // Selectores de Fecha y Hora
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f).clickable { showDatePicker = true }) {
                    PublishInput(
                        label = "Fecha",
                        value = date,
                        onValueChange = {},
                        placeholder = "DD/MM/AAAA",
                        isDarkMode = isDarkMode,
                        readOnly = true
                    )
                }
                Box(modifier = Modifier.weight(1f).clickable { showTimePicker = true }) {
                    PublishInput(
                        label = "Hora",
                        value = time,
                        onValueChange = {},
                        placeholder = "HH:MM",
                        isDarkMode = isDarkMode,
                        readOnly = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            PublishInput(
                label = "Descripción",
                value = description,
                onValueChange = { description = it },
                placeholder = "Agrega detalles que ayuden a encontrarlo",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Botón de Publicar Modificado
            Button(
                onClick = {
                    if (name.isNotBlank() && category.isNotBlank() && place.isNotBlank() && date.isNotBlank() && time.isNotBlank()) {

                        val dateTimeFormat = "$date, $time"

                        val nuevoObjeto = ObjetoReportado(
                            nombre = name,
                            categoria = category,
                            ubicacion = place,
                            fechaHora = dateTimeFormat,
                            descripcion = description,
                            estado = if (selectedType == PublishType.Lost) "PERDIDO" else "ENCONTRADO"
                        )

                        firestoreManager.agregarObjeto(nuevoObjeto) { exito, mensaje ->
                            if (exito) {
                                // Aquí puedes mostrar un Toast de éxito y navegar hacia atrás
                                onBack()
                            } else {
                                // Aquí puedes manejar el error visualmente
                                println("Error al publicar: $mensaje")
                            }
                        }
                    } else {
                        // Aquí deberías mostrar un mensaje pidiendo llenar los campos obligatorios
                        println("Por favor llena todos los campos obligatorios")
                    }
                },
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
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Lógica del DatePickerDialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        date = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Lógica del TimePickerDialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val isPm = timePickerState.hour >= 12
                    val amPm = if (isPm) "PM" else "AM"
                    val hour12 = if (timePickerState.hour % 12 == 0) 12 else timePickerState.hour % 12
                    val minuteFormat = timePickerState.minute.toString().padStart(2, '0')
                    time = "$hour12:$minuteFormat $amPm"
                    showTimePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }
}

// (Aquí van PublishHeader, PublishTypeCard y PhotoUploadCard que ya tenías igual)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownMenu(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    isDarkMode: Boolean
) {
    val categories = listOf("Ropa", "Llaves", "Cartera", "Útiles", "Electrónica", "Otros")
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Categoría",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Selecciona una categoría", fontSize = 13.sp) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().height(52.dp).menuAnchor(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = WayPurple,
                    unfocusedBorderColor = if (isDarkMode) MaterialTheme.colorScheme.outline else Color.Gray,
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onCategorySelected(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PublishInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isDarkMode: Boolean,
    readOnly: Boolean = false // Añadido para los campos de fecha/hora
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
            readOnly = readOnly,
            enabled = !readOnly, // Si es readonly, deshabilitamos la escritura nativa para que funcione el click
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WayPurple,
                unfocusedBorderColor = if (isDarkMode) MaterialTheme.colorScheme.outline else Color.Gray,
                disabledBorderColor = if (isDarkMode) MaterialTheme.colorScheme.outline else Color.Gray,
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}