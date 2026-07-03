package com.example.wayapp.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.wayapp.R
import com.example.wayapp.data.FirestoreManager
import com.example.wayapp.model.ObjetoReportado
import com.example.wayapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class PublishType { Lost, Found }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishScreen(
    onBack: () -> Unit = {}
) {
    val firestoreManager = remember { FirestoreManager() }
    val context = LocalContext.current

    var selectedType by remember { mutableStateOf(PublishType.Lost) }
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    // Estados para Fecha/Hora
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Estado para la URL de la imagen seleccionada
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) } // Estado de carga

    // Lanzador para la galería de fotos
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val isDarkMode = MaterialTheme.colorScheme.background == WayDarkBackground

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.padding(horizontal = 24.dp).padding(top = 24.dp)) {
            PublishHeader(onBack = onBack)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
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
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(text = "Información del objeto", color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(14.dp))

            PhotoUploadCard(
                imageUri = selectedImageUri,
                isDarkMode = isDarkMode,
                onClick = {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PublishInput(
                label = "Nombre del objeto",
                value = name,
                onValueChange = { name = it },
                placeholder = "Ej. Mochila Negra",
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(14.dp))

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

            // Selectores de Fecha y Hora con Capa Invisible
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    PublishInput(label = "Fecha", value = date, onValueChange = {}, placeholder = "DD/MM/AAAA", isDarkMode = isDarkMode)
                    Box(modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                        .background(Color.Transparent)
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    PublishInput(label = "Hora", value = time, onValueChange = {}, placeholder = "HH:MM", isDarkMode = isDarkMode)
                    Box(modifier = Modifier
                        .matchParentSize()
                        .clickable { showTimePicker = true }
                        .background(Color.Transparent)
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

            // Botón para publicar
            Button(
                onClick = {
                    if (name.isNotBlank() && category.isNotBlank() && place.isNotBlank() && date.isNotBlank() && time.isNotBlank()) {
                        isUploading = true
                        Toast.makeText(context, "Publicando...", Toast.LENGTH_SHORT).show()

                        if (selectedImageUri != null) {
                            firestoreManager.subirImagen(selectedImageUri!!) { imageUrl ->
                                if (imageUrl != null) {
                                    publicarObjetoFinal(firestoreManager, name, category, place, date, time, description, selectedType, imageUrl) { exito, mensaje ->
                                        isUploading = false
                                        if (exito) {
                                            Toast.makeText(context, "¡Objeto publicado con éxito!", Toast.LENGTH_SHORT).show()
                                            onBack()
                                        } else {
                                            Toast.makeText(context, "Error: $mensaje", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } else {
                                    isUploading = false
                                    Toast.makeText(context, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            publicarObjetoFinal(firestoreManager, name, category, place, date, time, description, selectedType, "") { exito, mensaje ->
                                isUploading = false
                                if (exito) {
                                    Toast.makeText(context, "¡Objeto publicado (sin foto)!", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    Toast.makeText(context, "Error: $mensaje", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Por favor llena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WayPurple),
                enabled = !isUploading
            ) {
                if (isUploading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(text = "Publicar", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
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
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = datePickerState) }
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
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") } },
            text = { TimePicker(state = timePickerState) }
        )
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
        Text(text = "Categoría", color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(14.dp),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
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
    isDarkMode: Boolean
) {
    Column {
        Text(text = label, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp),
            placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 13.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WayPurple,
                unfocusedBorderColor = if (isDarkMode) MaterialTheme.colorScheme.outline else Color.Gray,
            )
        )
    }
}

@Composable
fun PublishTypeCard(
    title: String,
    @androidx.annotation.DrawableRes image: Int,
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
            .background(if (selected) WayPurple.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface)
            .border(
                BorderStroke(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) WayPurple else if (isDarkMode) MaterialTheme.colorScheme.outline else WayBorder
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = WayPurple.copy(alpha = 0.14f)),
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
        Text(text = title, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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

fun publicarObjetoFinal(
    firestoreManager: FirestoreManager,
    name: String,
    category: String,
    place: String,
    date: String,
    time: String,
    description: String,
    selectedType: PublishType,
    imageUrl: String,
    onResult: (Boolean, String?) -> Unit
) {
    val dateTimeFormat = "$date, $time"
    val nuevoObjeto = ObjetoReportado(
        nombre = name,
        categoria = category,
        ubicacion = place,
        fechaHora = dateTimeFormat,
        descripcion = description,
        estado = if (selectedType == PublishType.Lost) "PERDIDO" else "ENCONTRADO",
        imageUrl = imageUrl,
        idUsuarioReporta = "usuario_demo_123"
    )
    firestoreManager.agregarObjeto(nuevoObjeto) { exito, mensaje ->
        onResult(exito, mensaje)
    }
}

@Composable
fun PhotoUploadCard(
    imageUri: Uri?,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                BorderStroke(1.dp, if (isDarkMode) MaterialTheme.colorScheme.outline else WayBorder),
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = "Miniatura",
                modifier = Modifier.size(50.dp).clip(CircleShape).border(1.dp, WayPurple, CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (isDarkMode) WayDarkBackground else Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Outlined.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        Column {
            Text(text = if (imageUri != null) "Cambiar foto" else "Agregar foto", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = "Un objeto, una foto", color = WayTextSecondary, fontSize = 11.sp)
        }
    }
}