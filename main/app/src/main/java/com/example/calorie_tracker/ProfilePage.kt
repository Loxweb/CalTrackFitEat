package com.example.calorie_tracker

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePage {
    private var name by mutableStateOf("")
    private var email by mutableStateOf("")
    private var weight by mutableStateOf("")
    private var age by mutableStateOf("")
    private var height by mutableStateOf("")
    private var targetWeight by mutableStateOf("")
    private var currentlySelectedGender: Gender = Gender.MALE
    private var myUserData: UserData? = null
    private var currentColorTheme: ColorTheme = ColorTheme.DARK

    @Composable
    fun ProfilePageUI(
        navController: NavHostController,
        myColorThemeViewModel: ColorThemeViewModel,
        server: Server,
        dbman: DatabaseManager
    ) {
        currentColorTheme = myColorThemeViewModel.currentColorTheme
        val nameRememberable = rememberSaveable { mutableStateOf(name) }
        val emailRememberable = rememberSaveable { mutableStateOf(email) }
        val weightRememberable = rememberSaveable { mutableStateOf(weight) }
        val ageRememberable = rememberSaveable { mutableStateOf(age) }
        val heightRememberable = rememberSaveable { mutableStateOf(height) }
        val isDataLoaded = remember { mutableStateOf(false) }

        LaunchedEffect(true) {
            if (!isDataLoaded.value) {
                dbman.checkForExistingUserData(
                    nameRememberable,
                    emailRememberable,
                    ageRememberable,
                    weightRememberable,
                    heightRememberable
                )
                isDataLoaded.value = true
            }
        }

        val targetWeightRememberable = rememberSaveable { mutableStateOf(targetWeight) }
        val currentlySelectedGenderRememberable = rememberSaveable { mutableStateOf(Gender.MALE) }

        name = nameRememberable.value
        email = emailRememberable.value
        weight = weightRememberable.value
        age = ageRememberable.value
        height = heightRememberable.value
        targetWeight = targetWeightRememberable.value
        currentlySelectedGender = currentlySelectedGenderRememberable.value

        Column(modifier = Modifier.fillMaxSize().background(currentColorTheme.backgroundColor)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                    Modifier.size(50.dp).clickable {
                        navController.popBackStack()
                    },
                    myColorThemeViewModel.currentColorTheme.textColor
                )
                Text(
                    "Профиль пользователя:",
                    fontSize = 20.sp,
                    color = currentColorTheme.textColor,
                    modifier = Modifier.align(Alignment.CenterVertically).weight(1f),
                )
                Button(
                    onClick = { updateProfileInformation(dbman) },
                    modifier = Modifier.testTag("updateProfileButton")
                ) {
                    Text(
                        text = "Обновить профиль",
                        fontSize = 15.sp,
                        color = currentColorTheme.textColor
                    )
                }
            }
            Column(modifier = Modifier.fillMaxSize().padding(4.dp).verticalScroll(rememberScrollState())) {
                ProfileRow("Имя", nameRememberable) { nameRememberable.value = it }
                ProfileRow("Email", emailRememberable) { emailRememberable.value = it }
                ProfileRow("Вес (кг)", weightRememberable) { weightRememberable.value = it }
                ProfileRow("Возраст", ageRememberable) { ageRememberable.value = it }
                ProfileRow("Рост (см)", heightRememberable) { heightRememberable.value = it }
                addAllGenderRadioButtons()
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    color = currentColorTheme.rowColor,
                    thickness = 5.dp
                )
                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                ) {
                    ProfileRow("Целевой вес", targetWeightRememberable) { updatedTargetWeight ->
                        targetWeightRememberable.value = updatedTargetWeight
                    }
                }
                Button(
                    onClick = { updateTargetWeight(dbman) },
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp)
                        .testTag("updateTargetWeightButton")
                ) {
                    Text(
                        text = "Обновить целевой вес",
                        fontSize = 15.sp,
                        color = currentColorTheme.textColor
                    )
                }
            }
        }
    }

    @Composable
    fun ProfileRow(label: String, mutableVar: MutableState<String>, onValueChange: (String) -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(currentColorTheme.rowColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label:",
                fontSize = 20.sp,
                color = currentColorTheme.textColor,
                modifier = Modifier.padding(4.dp)
            )
            TextField(
                value = mutableVar.value,
                onValueChange = { onValueChange(it) },
                label = { Text("Введите $label") },
                textStyle = TextStyle(fontSize = 20.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = currentColorTheme.backgroundColor,
                    unfocusedBorderColor = currentColorTheme.backgroundColor,
                    focusedTextColor = currentColorTheme.textColor,
                    unfocusedTextColor = currentColorTheme.textColor
                ),
                modifier = Modifier.padding(8.dp).testTag("${label}-input")
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    fun updateProfileInformation(dbman: DatabaseManager) {
        val ageToInt = age.toIntOrNull()
        val weightToDouble = weight.toDoubleOrNull()
        val heightToDouble = height.toDoubleOrNull()
        val sqlString = server.userDataForSQL()
        if (ageToInt != null && weightToDouble != null && heightToDouble != null) {
            server.setUserData(name, email, ageToInt, weightToDouble, heightToDouble, currentlySelectedGender.ordinal)
            if (currentDateAndWieght != null) {
                currentDateAndWieght.weight = myUserData?.weight ?: 0.0
            }
            CoroutineScope(Dispatchers.IO).launch {
                dbman.clearUserProfile()
                dbman.insertUserProfileData(sqlString)
            }
        } else {
            Log.d("Input Error", "Некорректный ввод для возраста, веса или роста: ")
        }
    }

    fun updateTargetWeight(dbman: DatabaseManager) {
        val targetWeightToInt = targetWeight.toIntOrNull()
        if (targetWeightToInt != null) {
            CoroutineScope(Dispatchers.IO).launch {
                dbman.clearStoredAppData()
                dbman.insertUpdatedWeight(targetWeightToInt, currentColorTheme)
            }
        } else {
            Log.d("Input Error", "Некорректный ввод для целевого веса: '$targetWeight' - должно быть числом")
        }
    }

    @Composable
    fun addAllGenderRadioButtons() {
        var selectedGenderRadioButton by remember { mutableStateOf("Мужской") }
        Column(modifier = Modifier.background(currentColorTheme.backgroundColor)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(
                        selected = (selectedGenderRadioButton == "Мужской"),
                        onClick = {
                            currentlySelectedGender = Gender.MALE
                            selectedGenderRadioButton = "Мужской"
                        }
                    )
                    Text(
                        text = "Мужской",
                        fontSize = 25.sp,
                        color = currentColorTheme.textColor,
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(
                        selected = (selectedGenderRadioButton == "Женский"),
                        onClick = {
                            currentlySelectedGender = Gender.FEMALE
                            selectedGenderRadioButton = "Женский"
                        }
                    )
                    Text(
                        text = "Женский",
                        fontSize = 25.sp,
                        color = currentColorTheme.textColor,
                    )
                }
            }
        }
    }
}
