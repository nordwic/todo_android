package com.example.homework1

import com.google.accompanist.flowlayout.FlowRow
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.util.*

@Composable
fun EditTodoScreen(
    initial: TodoItem? = null,
    onSave: (TodoItem) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(initial?.text ?: "")) }
    var importance by remember { mutableStateOf(initial?.importance ?: Importance.REGULAR) }
    var color by remember { mutableStateOf(initial?.color ?: android.graphics.Color.WHITE) }
    var deadLine by remember { mutableStateOf(initial?.deadLine) }
    var isDone by remember { mutableStateOf(initial?.isDone ?: false) }

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val isWide = maxWidth > 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            EditTopBar(onCancel = onCancel) {
                val uid = initial?.uid ?: UUID.randomUUID().toString()
                val item = TodoItem(
                    uid = uid,
                    text = text.text,
                    importance = importance,
                    color = color,
                    deadLine = deadLine,
                    isDone = isDone
                )
                onSave(item)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        TodoTextField(value = text, onValueChange = { text = it })
                        Spacer(Modifier.height(8.dp))
                        ImportanceSelector(selected = importance, onSelected = { importance = it })
                        Spacer(Modifier.height(8.dp))
                        DoneToggle(isDone = isDone, onCheckedChange = { isDone = it })
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        DeadlineRow(deadLine = deadLine, onChange = { deadLine = it })
                        Spacer(Modifier.height(8.dp))
                        ColorPickerRow(selectedColor = color, onPick = { color = it })
                        Spacer(Modifier.height(8.dp))
                        Text("Подсказка: свайпните, чтобы пометить выполненным", fontSize = 12.sp)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TodoTextField(value = text, onValueChange = { text = it })
                    ImportanceSelector(selected = importance, onSelected = { importance = it })
                    DeadlineRow(deadLine = deadLine, onChange = { deadLine = it })
                    ColorPickerRow(selectedColor = color, onPick = { color = it })
                    DoneToggle(isDone = isDone, onCheckedChange = { isDone = it })
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            SaveCancelRow(onSaveClick = {
                val uid = initial?.uid ?: UUID.randomUUID().toString()
                val item = TodoItem(
                    uid = uid,
                    text = text.text,
                    importance = importance,
                    color = color,
                    deadLine = deadLine,
                    isDone = isDone
                )
                onSave(item)
            }, onCancelClick = onCancel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTopBar(onCancel: () -> Unit, onSave: () -> Unit) {
    TopAppBar(
        title = { Text("Редактировать дело") },
        actions = {
            IconButton(onClick = onSave) {
                Icon(Icons.Default.Check, contentDescription = "Сохранить")
            }
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = "Отменить")
            }
        }
    )
}

@Composable
private fun TodoTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Описание задачи") },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        singleLine = false,
        maxLines = Int.MAX_VALUE // поле растёт с вводом
    )
}

@Composable
private fun ImportanceSelector(selected: Importance, onSelected: (Importance) -> Unit) {
    Column {
        Text("Важность")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Importance.values().forEach { imp ->
                val chosen = imp == selected
                Surface(
                    tonalElevation = if (chosen) 4.dp else 0.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .clickable { onSelected(imp) }
                        .padding(4.dp)
                ) {
                    Text(
                        text = imp.name.lowercase().replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(8.dp),
                        color = if (chosen) MaterialTheme.colorScheme.primary else Color.Unspecified
                    )
                }
            }
        }
    }
}

@Composable
private fun DeadlineRow(deadLine: LocalDateTime?, onChange: (LocalDateTime?) -> Unit) {
    val ctx = LocalContext.current
    val now = LocalDateTime.now()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Дедлайн", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = deadLine?.toString() ?: "Не задан",
                modifier = Modifier.weight(1f)
            )
            Row {
                Button(onClick = {
                    // Выбор даты
                    val dateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, y, m, d ->
                        val timeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, h, min ->
                            onChange(LocalDateTime.of(y, m + 1, d, h, min))
                        }
                        TimePickerDialog(ctx, timeListener, now.hour, now.minute, true).show()
                    }
                    DatePickerDialog(ctx, dateListener, now.year, now.monthValue - 1, now.dayOfMonth).show()
                }) {
                    Text("Выбрать дату")
                }

                if (deadLine != null) {
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(onClick = { onChange(null) }) {
                        Text("Очистить")
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorPickerRow(selectedColor: Int, onPick: (Int) -> Unit) {
    val colors = listOf(
        android.graphics.Color.parseColor("#FFCDD2"),
        android.graphics.Color.parseColor("#C8E6C9"),
        android.graphics.Color.parseColor("#BBDEFB"),
        android.graphics.Color.parseColor("#FFF9C4"),
        android.graphics.Color.parseColor("#D1C4E9")
    )

    Column {
        Text("Цвет заметки", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        FlowRow(
            mainAxisSpacing = 12.dp,
            crossAxisSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            colors.forEach { c ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(androidx.compose.ui.graphics.Color(c), shape = RoundedCornerShape(6.dp))
                        .clickable { onPick(c) },
                    contentAlignment = Alignment.Center
                ) {
                    if (c == selectedColor) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Выбранный цвет",
                            tint = androidx.compose.ui.graphics.Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DoneToggle(isDone: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = isDone, onCheckedChange = onCheckedChange)
        Spacer(Modifier.width(8.dp))
        Text("Выполнено")
    }
}

@Composable
private fun SaveCancelRow(onSaveClick: () -> Unit, onCancelClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = onCancelClick, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.Close, contentDescription = "Отмена")
            Spacer(Modifier.width(8.dp))
            Text("Отмена")
        }
        Spacer(Modifier.width(12.dp))
        Button(onClick = onSaveClick, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.Check, contentDescription = "Сохранить")
            Spacer(Modifier.width(8.dp))
            Text("Сохранить")
        }
    }
}
