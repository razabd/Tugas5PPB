package com.example.tugas5ppb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugas5ppb.ui.theme.Tugas5PPBTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas5PPBTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    var selectedScreen by remember { mutableStateOf("Konversi Celsius-Fahrenheit") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "TugasPPB5",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("Konversi Celsius-Fahrenheit", "Mata Uang", "Kalkulator")) { screen ->
                Button(
                    onClick = { selectedScreen = screen },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(screen)
                }
            }
        }

        when (selectedScreen) {
            "Konversi Celsius-Fahrenheit" -> KonversiSuhu()
            "Mata Uang" -> KonversiMataUang()
            "Kalkulator" -> Kalkulator()
        }
    }
}

@Composable
fun KonversiSuhu() {
    var suhu by remember { mutableStateOf("") }
    var hasilFahrenheit by remember { mutableStateOf("") }

    Column {
        Text(
            text = "Konversi Suhu",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = suhu,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("-?\\d*\\.?\\d*"))) {
                    suhu = newValue
                }
            },
            label = { Text("Masukkan Suhu (°C)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                val celsius = suhu.toDoubleOrNull()
                if (celsius != null) {
                    val fahrenheit = (celsius * 9 / 5) + 32
                    hasilFahrenheit = String.format("%.2f °F", fahrenheit)
                } else {
                    hasilFahrenheit = "Masukkan angka valid"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Konversi ke Fahrenheit")
        }

        Text(
            text = hasilFahrenheit,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun KonversiMataUang() {
    var usd by remember { mutableStateOf("") }
    var hasilIDR by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = "Konversi USD ke IDR"
        )

        Text(
            text = "Kurs 16.800,28 berdasarkan 28 Apr, 19.50 UTC",
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        TextField(
            value = usd,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("-?\\d*\\.?\\d*"))) {
                    usd = newValue
                }
            },
            label = { Text("Masukkan USD") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                val usdAmount = usd.toDoubleOrNull()
                if (usdAmount != null) {
                    val idr = usdAmount * 16800.28
                    val locale = Locale("id", "ID")
                    val numberFormat = NumberFormat.getNumberInstance(locale)
                    numberFormat.minimumFractionDigits = 0
                    numberFormat.maximumFractionDigits = 0
                    hasilIDR = "Rp ${numberFormat.format(idr)}"
                } else {
                    hasilIDR = "Masukkan angka valid"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Konversi ke IDR")
        }

        Text(
            text = hasilIDR,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun Kalkulator() {
    var angka1 by remember { mutableStateOf("") }
    var angka2 by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf("") }
    var displayText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = displayText.ifEmpty { "0" },
            fontSize = 32.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Button grid
        val buttons = listOf(
            listOf("C", "", "", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                "C" -> {
                                    angka1 = ""
                                    angka2 = ""
                                    operator = ""
                                    displayText = ""
                                }
                                "+", "-", "*", "/" -> {
                                    if (angka1.isNotEmpty() && angka2.isEmpty()) {
                                        operator = label
                                        displayText = "$angka1 $label"
                                    }
                                }
                                "=" -> {
                                    if (angka1.isNotEmpty() && angka2.isNotEmpty() && operator.isNotEmpty()) {
                                        val num1 = angka1.toDoubleOrNull()
                                        val num2 = angka2.toDoubleOrNull()
                                        if (num1 != null && num2 != null) {
                                            val result = when (operator) {
                                                "+" -> num1 + num2
                                                "-" -> num1 - num2
                                                "*" -> num1 * num2
                                                "/" -> if (num2 != 0.0) num1 / num2 else "Error"
                                                else -> 0.0
                                            }
                                            displayText = if (result is Double && result.isFinite()) {
                                                if (result % 1 == 0.0) result.toInt().toString()
                                                else result.toString()
                                            } else {
                                                "Error"
                                            }
                                            angka1 = displayText
                                            angka2 = ""
                                            operator = ""
                                        } else {
                                            displayText = "Error"
                                        }
                                    }
                                }
                                "." -> {
                                    if (operator.isEmpty() && !angka1.contains(".")) {
                                        angka1 += if (angka1.isEmpty()) "0." else "."
                                        displayText = angka1
                                    } else if (operator.isNotEmpty() && !angka2.contains(".")) {
                                        angka2 += if (angka2.isEmpty()) "0." else "."
                                        displayText = "$angka1 $operator $angka2"
                                    }
                                }
                                else -> {
                                    if (operator.isEmpty()) {
                                        angka1 += label
                                        displayText = angka1
                                    } else {
                                        angka2 += label
                                        displayText = "$angka1 $operator $angka2"
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        enabled = label.isNotEmpty()
                    ) {
                        Text(text = label, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    Tugas5PPBTheme {
        AppContent()
    }
}