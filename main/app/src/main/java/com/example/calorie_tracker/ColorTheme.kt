package com.example.calorie_tracker

import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

enum class ColorTheme(
    val backgroundColor: Color,
    val textColor: Color,
    val rowColor: Color
) {
    DARK(
        backgroundColor = Color(0xFF1E1E32),
        textColor = Color.White,
        rowColor = Color(0xFF2D3C50)
    ),
    LIGHT(
        backgroundColor = Color(0xFFBEBEC8),
        textColor = Color.Black,
        rowColor = Color(0xFF969696)
    ),
    CUSTOM(
        backgroundColor = Color.Blue,
        textColor = Color.Black,
        rowColor = Color.Magenta
    )
}

class ColorThemeViewModel : ViewModel() {
    var currentColorTheme by mutableStateOf(ColorTheme.DARK)

    fun updateColorTheme(theme: ColorTheme, dbman: DatabaseManager) {
        currentColorTheme = theme
        dbman.updateColorThemeInDB(theme.ordinal)
    }
}

@Composable
fun changeColorToTheme(theme: ColorTheme) {
    val backgroundColor = theme.backgroundColor
    val textColor = theme.textColor
    val rowColor = theme.rowColor
}
