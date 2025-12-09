package com.example.calorie_tracker

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class AddFoodPage {
    enum class AddFoodScreens {
        OVERVIEW,
        ADDING,
        CUSTOM_ENTRY
    }

    data class FoodEntry(
        var meal: String,
        var description: String,
        var calories: String,
        var protein: String,
        var carbs: String,
        var fat: String
    )

    data class MealInformation(
        var totalCalories: MutableState<String>,
        var totalCarbs: MutableState<String>,
        var totalProtein: MutableState<String>,
        var totalFat: MutableState<String>,
        var items: MutableList<ItemInfo>
    )

    private lateinit var myColorThemeViewModel: ColorThemeViewModel
    lateinit var mealInformation: MealInformation

    @SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
    @Composable
    fun FoodPageUI(
        navController: NavHostController,
        meal: String,
        myColorThemeViewModel: ColorThemeViewModel,
        dbman: DatabaseManager
    ) {
        this@AddFoodPage.myColorThemeViewModel = myColorThemeViewModel
        val curNavController = rememberNavController()
        when (meal) {
            "Breakfast" -> mealInformation = breakfastInformation
            "Lunch" -> mealInformation = lunchInformation
            "Dinner" -> mealInformation = dinnerInformation
            "Desert" -> mealInformation = desertInformation
            "Snacks" -> mealInformation = snacksInformation
        }
        NavHost(curNavController, startDestination = AddFoodScreens.OVERVIEW.name) {
            composable(AddFoodScreens.OVERVIEW.name) {
                OverViewScreen(navController, meal, curNavController, dbman)
            }
            composable(AddFoodScreens.ADDING.name) {
                AddFoodScreen(curNavController)
            }
            composable(AddFoodScreens.CUSTOM_ENTRY.name) {
                CustomEntry(curNavController, dbman, meal)
            }
        }
    }

    @Composable
    fun OverViewScreen(
        navController: NavHostController,
        mealType: String,
        curNavController: NavHostController,
        dbman: DatabaseManager
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "",
                        Modifier
                            .size(50.dp)
                            .clickable {
                                navController.navigate(Screens.MAINSCREEN.name)
                            }
                    )
                    Text(
                        mealType,
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = {
                            curNavController.navigate(AddFoodScreens.ADDING.name)
                        },
                        Modifier
                            .width(300.dp)
                            .padding(20.dp)
                            .testTag("add-food"),
                    ) {
                        Text(
                            "Добавить еще",
                            fontSize = 17.sp
                        )
                    }
                }
            },
            containerColor = myColorThemeViewModel.currentColorTheme.backgroundColor,
            contentColor = myColorThemeViewModel.currentColorTheme.textColor
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (LocalConfiguration.current.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1.5f)
                            .background(myColorThemeViewModel.currentColorTheme.rowColor),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (mealType) {
                            "Breakfast" -> Image(
                                painter = painterResource(R.drawable.breakfastgraphic),
                                contentDescription = "Изображение завтрака"
                            )
                            "Lunch" -> Image(
                                painter = painterResource(R.drawable.lunchbox),
                                contentDescription = "Изображение обеда"
                            )
                            "Dinner" -> Image(
                                painter = painterResource(R.drawable.dinnergraphic),
                                contentDescription = "Изображение ужина"
                            )
                            "Snacks" -> Image(
                                painter = painterResource(R.drawable.snackgraphic),
                                contentDescription = "Изображение перекуса"
                            )
                            "Desert" -> Image(
                                painter = painterResource(R.drawable.desertgraphic),
                                contentDescription = "Изображение десерта"
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(4f)
                ) {
                    Row(
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        MacrosColumn(mealInformation.totalCalories.value, "Кал", "Калории")
                        MacrosColumn(mealInformation.totalCarbs.value, "г", "Углеводы")
                        MacrosColumn(mealInformation.totalProtein.value, "г", "Белок")
                        MacrosColumn(mealInformation.totalFat.value, "г", "Жиры")
                    }
                    ItemList(dbman, mealType, curNavController)
                }
            }
        }
    }

    @Composable
    fun RowScope.MacrosColumn(amount: String, unit: String, type: String) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    "$amount $unit",
                    fontSize = 20.sp
                )
            }
            Row {
                Text(
                    type,
                    fontSize = 15.sp
                )
            }
        }
    }

    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun ItemList(dbman: DatabaseManager, mealName: String, curNavController: NavHostController) {
        LazyColumn(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(mealInformation.items.size) { index ->
                Column(
                    modifier = Modifier.testTag("item-${index}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(9f)
                        ) {
                            Text(
                                mealInformation.items[index].description,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .testTag("item-name"),
                                fontSize = 25.sp,
                            )
                            Text(
                                "${mealInformation.items[index].calories} калорий, ${mealInformation.items[index].carbs}г углеводов, ${mealInformation.items[index].protein}г белка, ${mealInformation.items[index].fat}г жиров",
                                modifier = Modifier
                                    .padding(start = 7.dp)
                                    .testTag("item-values"),
                                fontSize = 17.sp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(top = 5.dp, end = 5.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("delete-item")
                                    .clickable {
                                        val temp = FoodEntry(
                                            meal = mealName,
                                            description = mealInformation.items[index].description,
                                            calories = mealInformation.items[index].calories,
                                            protein = mealInformation.items[index].protein,
                                            fat = mealInformation.items[index].fat,
                                            carbs = mealInformation.items[index].carbs
                                        )
                                        mealInformation.totalCalories.value = (
                                                mealInformation.totalCalories.value.toInt() -
                                                        mealInformation.items[index].calories.toInt()
                                                ).toString()
                                        mealInformation.totalFat.value = (
                                                mealInformation.totalFat.value.toInt() -
                                                        mealInformation.items[index].fat.toInt()
                                                ).toString()
                                        mealInformation.totalCarbs.value = (
                                                mealInformation.totalCarbs.value.toInt() -
                                                        mealInformation.items[index].carbs.toInt()
                                                ).toString()
                                        mealInformation.totalProtein.value = (
                                                mealInformation.totalProtein.value.toInt() -
                                                        mealInformation.items[index].protein.toInt()
                                                ).toString()
                                        mealInformation.items.removeAt(index)
                                        dbman.deleteFoodItem(temp)
                                        curNavController.navigate(AddFoodScreens.OVERVIEW.name)
                                    }
                            )
                        }
                    }
                    HorizontalDivider(
                        color = myColorThemeViewModel.currentColorTheme.rowColor,
                        thickness = 2.dp
                    )
                }
            }
        }
    }

    @Composable
    fun AddFoodScreen(curNavController: NavHostController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(myColorThemeViewModel.currentColorTheme.backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                    Modifier
                        .size(50.dp)
                        .clickable {
                            curNavController.navigate(AddFoodScreens.OVERVIEW.name)
                        },
                    myColorThemeViewModel.currentColorTheme.textColor
                )
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Добавить ниже",
                        fontSize = 22.sp,
                        color = myColorThemeViewModel.currentColorTheme.textColor,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.Center),
                    )
                }
            }
            CustomEntry(curNavController)
        }
    }

    @Composable
    fun ColumnScope.CustomEntry(curNavController: NavHostController) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .size(width = 320.dp, height = 100.dp)
                    .testTag("custom-entry")
                    .clickable {
                        curNavController.navigate(AddFoodScreens.CUSTOM_ENTRY.name)
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = myColorThemeViewModel.currentColorTheme.rowColor
                )
            ) {
                Text(
                    "Ручной ввод",
                    modifier = Modifier.padding(top = 20.dp, start = 40.dp),
                    fontSize = 20.sp,
                    color = myColorThemeViewModel.currentColorTheme.textColor
                )
                Text(
                    "Вручную добавьте продукт с макронутриентами",
                    modifier = Modifier.padding(start = 40.dp),
                    fontSize = 13.sp,
                    color = myColorThemeViewModel.currentColorTheme.textColor
                )
            }
        }
    }

    @Composable
    fun CustomEntry(
        curNavController: NavHostController,
        dbman: DatabaseManager,
        meal: String
    ) {
        val description = rememberSaveable { mutableStateOf("") }
        val calories = rememberSaveable { mutableStateOf("") }
        val protein = rememberSaveable { mutableStateOf("") }
        val carbs = rememberSaveable { mutableStateOf("") }
        val fat = rememberSaveable { mutableStateOf("") }
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(myColorThemeViewModel.currentColorTheme.backgroundColor)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                    Modifier
                        .size(50.dp)
                        .clickable {
                            curNavController.popBackStack()
                        },
                    myColorThemeViewModel.currentColorTheme.textColor
                )
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Ручной ввод",
                        fontSize = 22.sp,
                        color = myColorThemeViewModel.currentColorTheme.textColor,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .align(Alignment.Center),
                    )
                }
            }
            CustomEntryValues("Описание", description)
            CustomEntryValues("Калории", calories)
            CustomEntryValues("Белок", protein)
            CustomEntryValues("Углеводы", carbs)
            CustomEntryValues("Жиры", fat)
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    onClick = {
                        if (santizeCustomEntry(
                                description.value,
                                calories.value,
                                protein.value,
                                carbs.value,
                                fat.value
                            )
                        ) {
                            val foodEntry = FoodEntry(
                                meal = meal,
                                description = description.value,
                                calories = calories.value,
                                protein = protein.value,
                                carbs = carbs.value,
                                fat = fat.value
                            )
                            val item = ItemInfo(
                                description = description.value,
                                calories = calories.value,
                                protein = protein.value,
                                carbs = carbs.value,
                                fat = fat.value
                            )
                            mealInformation.items.add(item)
                            mealInformation.totalCalories.value = (
                                    calories.value.toInt() +
                                            mealInformation.totalCalories.value.toInt()
                                    ).toString()
                            mealInformation.totalFat.value = (
                                    fat.value.toInt() +
                                            mealInformation.totalFat.value.toInt()
                                    ).toString()
                            mealInformation.totalCarbs.value = (
                                    carbs.value.toInt() +
                                            mealInformation.totalCarbs.value.toInt()
                                    ).toString()
                            mealInformation.totalProtein.value = (
                                    protein.value.toInt() +
                                            mealInformation.totalProtein.value.toInt()
                                    ).toString()
                            CoroutineScope(Dispatchers.IO).launch {
                                dbman.insertFood(foodEntry)
                            }
                            curNavController.navigate(AddFoodScreens.OVERVIEW.name)
                        } else {
                            Toast.makeText(
                                context,
                                "Заполните все поля",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    Modifier
                        .width(300.dp)
                        .padding(20.dp)
                        .testTag("add-custom-entry"),
                ) {
                    Text(
                        "Добавить",
                        fontSize = 17.sp
                    )
                }
            }
        }
    }

    fun santizeCustomEntry(
        description: String,
        calories: String,
        protein: String,
        carbs: String,
        fat: String
    ): Boolean {
        if (description.isEmpty() && calories.isEmpty() && protein.isEmpty()
            && carbs.isEmpty() && fat.isEmpty()
        ) {
            return false
        }
        try {
            calories.toInt()
            protein.toInt()
            carbs.toInt()
            fat.toInt()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    @Composable
    fun CustomEntryValues(category: String, inputValue: MutableState<String>) {
        TextField(
            value = inputValue.value,
            onValueChange = {
                inputValue.value = it
            },
            label = { Text(category) },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = myColorThemeViewModel.currentColorTheme.textColor,
                unfocusedBorderColor = myColorThemeViewModel.currentColorTheme.textColor,
                focusedTextColor = myColorThemeViewModel.currentColorTheme.textColor,
                unfocusedTextColor = myColorThemeViewModel.currentColorTheme.textColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                .testTag("${category}-input")
        )
    }
}
