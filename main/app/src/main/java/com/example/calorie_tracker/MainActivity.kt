package com.example.calorie_tracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.roundToInt
import kotlinx.coroutines.withContext

var currentlySelectedMeal: MealTypes? = null
val server = Server()
val currentDateAndWieght: dateAndWieght? = null
var currentColorTheme: ColorTheme = ColorTheme.DARK
val mealInfoList = MutableList(5) { TotalMealInfo("0", "0", "0", "0") }

lateinit var breakfastInformation: AddFoodPage.MealInformation
lateinit var lunchInformation: AddFoodPage.MealInformation
lateinit var dinnerInformation: AddFoodPage.MealInformation
lateinit var desertInformation: AddFoodPage.MealInformation
lateinit var snacksInformation: AddFoodPage.MealInformation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main(this)
        }
    }
}

@Composable
fun Main(context: Context) {
    val dbman = DatabaseManager(context)
    val navController = rememberNavController()
    val myColorThemeViewModel: ColorThemeViewModel = viewModel()
    val databasedColorTheme = dbman.checkIfExistingColorTheme()
    if (databasedColorTheme != -1) {
        val selectedTheme = ColorTheme.entries.getOrNull(databasedColorTheme) ?: ColorTheme.DARK
        myColorThemeViewModel.currentColorTheme = selectedTheme
    }
    breakfastInformation = GetCache(dbman, "Завтрак")
    lunchInformation = GetCache(dbman, "Обед")
    dinnerInformation = GetCache(dbman, "Ужин")
    desertInformation = GetCache(dbman, "Десерт")
    snacksInformation = GetCache(dbman, "Перекусы")

    NavHost(navController, startDestination = Screens.MAINSCREEN.name) {
        composable(Screens.MAINSCREEN.name) {
            MainScreenUI(navController, myColorThemeViewModel, dbman)
        }
        composable(Screens.FOODPAGE.name) {
            val foodPage = AddFoodPage()
            foodPage.FoodPageUI(navController, correctFoodCapitalization(currentlySelectedMeal.toString()), myColorThemeViewModel, dbman)
        }
        composable(Screens.GRAPHPAGE.name) {
            val graphPage = viewGraphPage()
            graphPage.ComplexGraphController(navController, myColorThemeViewModel, dbman)
        }
        composable(Screens.PROFILEPAGE.name) {
            val ProfilePage = ProfilePage()
            ProfilePage.ProfilePageUI(navController, myColorThemeViewModel, server, dbman)
        }
    }
}

@Composable
fun MainScreenUI(navController: NavHostController, myColorThemeViewModel: ColorThemeViewModel, dbman: DatabaseManager) {
    checkPreviousDates(dbman)
    var currentColorTheme = myColorThemeViewModel.currentColorTheme
    packMealInfoList(dbman)
    val recommendedCalories = dbman.getRecommendedCalories()

    Column(modifier = Modifier.fillMaxSize().background(currentColorTheme.backgroundColor)) {
        Row(modifier = Modifier.fillMaxWidth().weight(1f).padding(4.dp)) {
            Text(
                "Калорийность",
                fontSize = 15.sp,
                color = currentColorTheme.textColor,
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
            )
            myNavhostDropDownMenu(navController, myColorThemeViewModel)
        }
        Row(modifier = Modifier.fillMaxWidth().weight(8f)) {
            Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Row {
                    Text(
                        "Итоги: ",
                        fontSize = 30.sp,
                        color = currentColorTheme.textColor,
                        modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
                    )
                }
                summaryUIElements(dbman, myColorThemeViewModel)
            }
        }
        Column(modifier = Modifier.fillMaxSize().weight(7f)) {
            Row(modifier = Modifier.padding(4.dp)) {
                Text(
                    "Питание: ",
                    fontSize = 30.sp,
                    color = currentColorTheme.textColor,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
                myColorDropDownMenu(myColorThemeViewModel, dbman)
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(4.dp).verticalScroll(rememberScrollState())
            ) {
                NutritionRow(
                    R.drawable.breakfastgraphic,
                    mealTypeText = "Завтрак",
                    recommendedCalories = recommendedCalories,
                    dbman,
                    onClick = {
                        navController.navigate(Screens.FOODPAGE.name)
                        currentlySelectedMeal = MealTypes.BREAKFAST
                    }
                )
                NutritionRow(
                    R.drawable.lunchbox,
                    mealTypeText = "Обед",
                    recommendedCalories = recommendedCalories,
                    dbman,
                    onClick = {
                        navController.navigate(Screens.FOODPAGE.name)
                        currentlySelectedMeal = MealTypes.LUNCH
                    }
                )
                NutritionRow(
                    R.drawable.dinnergraphic,
                    mealTypeText = "Ужин",
                    recommendedCalories = recommendedCalories,
                    dbman,
                    onClick = {
                        navController.navigate(Screens.FOODPAGE.name)
                        currentlySelectedMeal = MealTypes.DINNER
                    }
                )
                NutritionRow(
                    R.drawable.desertgraphic,
                    mealTypeText = "Десерт",
                    recommendedCalories = recommendedCalories,
                    dbman,
                    onClick = {
                        navController.navigate(Screens.FOODPAGE.name)
                        currentlySelectedMeal = MealTypes.DESERT
                    }
                )
                NutritionRow(
                    R.drawable.snackgraphic,
                    mealTypeText = "Перекусы",
                    recommendedCalories = recommendedCalories,
                    dbman,
                    onClick = {
                        navController.navigate(Screens.FOODPAGE.name)
                        currentlySelectedMeal = MealTypes.SNACKS
                    }
                )
            }
        }
    }
}

@Composable
fun GetCache(dbman: DatabaseManager, mealName: String): AddFoodPage.MealInformation {
    val totalCalories = remember { mutableStateOf("") }
    val totalCarbs = remember { mutableStateOf("") }
    val totalProtein = remember { mutableStateOf("") }
    val totalFat = remember { mutableStateOf("") }
    var items by remember { mutableStateOf(mutableListOf<ItemInfo>()) }

    val mealInformation = AddFoodPage.MealInformation(
        totalCalories = totalCalories,
        totalCarbs = totalCarbs,
        totalProtein = totalProtein,
        totalFat = totalFat,
        items = items
    )

    LaunchedEffect(true) {
        withContext(Dispatchers.Default) {
            val tempNutrition = dbman.getTotalMealNutrition(mealName)
            val tempItems = dbman.getMealItems(mealName)
            totalCalories.value = tempNutrition.calories
            totalCarbs.value = tempNutrition.carbs
            totalProtein.value = tempNutrition.protein
            totalFat.value = tempNutrition.fat
            items = tempItems
        }
    }
    return mealInformation
}

fun checkPreviousDates(dbman: DatabaseManager) {
    CoroutineScope(Dispatchers.IO).launch {
        val currentDateInstance = LocalDate.now()
        val newestDateInDatabase = dbman.getNewestDateInDatabase()
        if (currentDateInstance != newestDateInDatabase) {
            dbman.addToPastDates(currentDateInstance.dayOfMonth, currentDateInstance.monthValue, currentDateInstance.year)
            dbman.deleteAllFoodItems()
        }
    }
}

fun packMealInfoList(dbman: DatabaseManager) {
    CoroutineScope(Dispatchers.IO).launch {
        val mealTypes = MealTypes.entries.toTypedArray()
        for (i in 0..<mealInfoList.size) {
            val currentMealType = mealTypes[i]
            mealInfoList[i] = dbman.getTotalMealNutrition(currentMealType.name)
        }
    }
}

@Composable
fun myColorDropDownMenu(myColorThemeViewModel: ColorThemeViewModel, dbman: DatabaseManager) {
    currentColorTheme = myColorThemeViewModel.currentColorTheme
    var dropdownIsSelected by remember { mutableStateOf(false) }
    var dropdownSelectedOption by remember { mutableStateOf("Выберите тему") }
    val dropdownMenuOptions = listOf("Тёмная", "Светлая")

    Column(modifier = Modifier.size(40.dp).background(currentColorTheme.backgroundColor)) {
        Icon(
            Icons.Outlined.Settings,
            contentDescription = "",
            Modifier.size(50.dp).background(currentColorTheme.backgroundColor).clickable {
                dropdownIsSelected = !dropdownIsSelected
            }
        )
        DropdownMenu(
            expanded = dropdownIsSelected,
            onDismissRequest = { dropdownIsSelected = false },
            modifier = Modifier.width(150.dp).background(currentColorTheme.rowColor).padding(8.dp)
        ) {
            dropdownMenuOptions.forEach { currentOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            currentOption,
                            color = currentColorTheme.textColor,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onClick = {
                        dropdownSelectedOption = currentOption
                        dropdownIsSelected = false
                        when (currentOption) {
                            "Тёмная" -> myColorThemeViewModel.updateColorTheme(ColorTheme.DARK, dbman)
                            "Светлая" -> myColorThemeViewModel.updateColorTheme(ColorTheme.LIGHT, dbman)
                        }
                    }
                )
            }
        }
        currentColorTheme = myColorThemeViewModel.currentColorTheme
    }
}

@Composable
fun summaryUIElements(dbman: DatabaseManager, myColorThemeViewModel: ColorThemeViewModel) {
    val proteinRatio = 0.30
    val carbsRatio = 0.40
    val fatRatio = 0.30

    Column(
        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)).background(myColorThemeViewModel.currentColorTheme.rowColor)
    ) {
        Row(modifier = Modifier.fillMaxSize().weight(2f)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Всего калорий:",
                    fontSize = 30.sp,
                    color = currentColorTheme.textColor,
                )
                val recommendedCalories = dbman.getRecommendedCalories()
                val dailyConsumedCalories = dbman.getTotalCalories()
                LinearProgressIndicator(
                    progress = { (1.0 * dailyConsumedCalories / recommendedCalories).toFloat() },
                    modifier = Modifier.fillMaxWidth().padding(4.dp).height(15.dp),
                )
                Text(
                    "$dailyConsumedCalories / $recommendedCalories ккал",
                    fontSize = 40.sp,
                    color = currentColorTheme.textColor,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize().weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            summaryProgressBar("Белки", proteinRatio.toFloat(), myColorThemeViewModel, Modifier.fillMaxHeight().weight(1f), dbman)
            summaryProgressBar("Углеводы", carbsRatio.toFloat(), myColorThemeViewModel, Modifier.fillMaxHeight().weight(1f), dbman)
            summaryProgressBar("Жиры", fatRatio.toFloat(), myColorThemeViewModel, Modifier.fillMaxHeight().weight(1f), dbman)
        }
    }
}

@Composable
fun summaryProgressBar(label: String, dietRatio: Float, myColorThemeViewModel: ColorThemeViewModel, modifier: Modifier = Modifier, dbman: DatabaseManager) {
    currentColorTheme = myColorThemeViewModel.currentColorTheme
    var macroCaloriesConsumed = 0
    val totalMacroInGrams: Int
    val totalRecommendedMacros: Int
    val progress = (macroCaloriesConsumed / (dbman.getRecommendedCalories() * dietRatio))

    if (label == "Жиры") {
        macroCaloriesConsumed = dbman.getTotalFat() * 9
        totalMacroInGrams = dbman.getTotalFat()
        totalRecommendedMacros = (dbman.getRecommendedCalories() * dietRatio).roundToInt() / 9
    } else if (label == "Белки") {
        macroCaloriesConsumed = dbman.getTotalProtein() * 4
        totalMacroInGrams = dbman.getTotalProtein()
        totalRecommendedMacros = (dbman.getRecommendedCalories() * dietRatio).roundToInt() / 4
    } else {
        macroCaloriesConsumed = dbman.getTotalCarbs() * 4
        totalMacroInGrams = dbman.getTotalCarbs()
        totalRecommendedMacros = (dbman.getRecommendedCalories() * dietRatio).roundToInt() / 4
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            label,
            fontSize = 20.sp,
            color = currentColorTheme.textColor,
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
        )
        Text(
            "$totalMacroInGrams / $totalRecommendedMacros г",
            fontSize = 15.sp,
            color = currentColorTheme.textColor,
        )
        Text(
            "$macroCaloriesConsumed / ${(dbman.getRecommendedCalories() * dietRatio).roundToInt()} ккал",
            fontSize = 15.sp,
            color = currentColorTheme.textColor,
        )
    }
}

@Composable
fun myNavhostDropDownMenu(navController: NavHostController, myColorThemeViewModel: ColorThemeViewModel) {
    currentColorTheme = myColorThemeViewModel.currentColorTheme
    var dropdownIsSelected by remember { mutableStateOf(false) }
    var dropdownSelectedOption by remember { mutableStateOf("Выберите страницу") }
    val dropdownMenuOptions = listOf("Профиль", "График")

    Column(modifier = Modifier.size(40.dp).background(currentColorTheme.backgroundColor)) {
        Icon(
            Icons.Outlined.Menu,
            contentDescription = "",
            Modifier.size(50.dp).clickable { dropdownIsSelected = !dropdownIsSelected }
                .testTag("NavhostDropdownMenuIcon")
        )
        DropdownMenu(
            expanded = dropdownIsSelected,
            onDismissRequest = { dropdownIsSelected = false },
            modifier = Modifier.width(300.dp).background(currentColorTheme.rowColor).padding(8.dp)
                .testTag("NavhostDropdownMenu")
        ) {
            dropdownMenuOptions.forEach { currentOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            currentOption,
                            color = currentColorTheme.textColor,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onClick = {
                        dropdownSelectedOption = currentOption
                        dropdownIsSelected = false
                        when (currentOption) {
                            "Профиль" -> navController.navigate(Screens.PROFILEPAGE.name)
                            "График" -> navController.navigate(Screens.GRAPHPAGE.name)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NutritionRow(imageID: Int, mealTypeText: String, recommendedCalories: Int, dbman: DatabaseManager, onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(currentColorTheme.rowColor)
    ) {
        Image(
            painter = painterResource(id = imageID),
            contentDescription = "Изображение для данных о питании",
            modifier = Modifier.size(100.dp).padding(4.dp).clip(RoundedCornerShape(16.dp)).align(Alignment.CenterVertically),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.align(Alignment.CenterVertically).weight(1f).padding(4.dp)) {
            Text(
                mealTypeText,
                fontSize = 25.sp,
                color = currentColorTheme.textColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${dbman.getTotalMealNutrition(mealTypeText).calories} / $recommendedCalories ккал",
                fontSize = 20.sp,
                color = currentColorTheme.textColor,
            )
        }
        Button(
            onClick = onClick,
            modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp)
                .testTag("${mealTypeText}-Button")
        ) {
            Text(
                text = "+",
                fontSize = 20.sp,
                color = currentColorTheme.textColor
            )
        }
    }
}

fun correctFoodCapitalization(input: String): String {
    return if (input.isNotEmpty()) {
        input[0].uppercase() + input.substring(1).lowercase()
    } else {
        input
    }
}
