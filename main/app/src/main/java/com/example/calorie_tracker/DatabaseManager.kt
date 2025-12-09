package com.example.calorie_tracker

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TotalMealInfo(
    var calories: String,
    var protein: String,
    var carbs: String,
    var fat: String
)

data class ItemInfo(
    var description: String,
    var calories: String,
    var protein: String,
    var carbs: String,
    var fat: String
)

data class WeightInfo(
    var Day: Int,
    var month: Int,
    var year: Int,
    var weight: Double
)

open class DatabaseManager(context: Context) : SQLiteOpenHelper(context, "MyDb", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS MEALS(mealName TEXT, foodName Text, calories INT, protein INT, carbs INT, fat INT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS USERPROFILE(name TEXT, emailAddress TEXT, age INT, weight DOUBLE, inchHeight DOUBLE, gender INT, recommendedCalories INT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS STOREDAPPINFORMATION(targetWeight INT, colorTheme INT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS PASTDATES(dateDay INT, dateMonth Int, dateYear Int, targetWeightAtTime INT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun checkForExistingUserData(
        nameRememberable: MutableState<String>,
        emailRememberable: MutableState<String>,
        ageRememberable: MutableState<String>,
        weightRememberable: MutableState<String>,
        inchHeightRememberable: MutableState<String>
    ) {
        val cursor = readableDatabase.rawQuery("SELECT * FROM USERPROFILE", null)
        while (cursor.moveToNext()) {
            nameRememberable.value = cursor.getString(0)
            emailRememberable.value = cursor.getString(1)
            ageRememberable.value = cursor.getInt(2).toString()
            weightRememberable.value = cursor.getDouble(3).toString()
            inchHeightRememberable.value = cursor.getDouble(4).toString()
        }
        cursor.close()
    }

    fun clearUserProfile() {
        writableDatabase.execSQL("DELETE FROM USERPROFILE")
    }

    fun insertUserProfileData(data: String) {
        writableDatabase.execSQL("INSERT INTO USERPROFILE(name, emailAddress, age, weight, inchHeight, gender, recommendedCalories) VALUES ($data)")
    }

    fun clearStoredAppData() {
        writableDatabase.execSQL("DELETE FROM STOREDAPPINFORMATION")
    }

    fun insertUpdatedWeight(targetWeight: Int, currentColorTheme: ColorTheme) {
        writableDatabase.execSQL("INSERT INTO STOREDAPPINFORMATION(targetWeight, colorTheme) VALUES ($targetWeight, ${currentColorTheme.ordinal})")
    }

    fun addToPastDates(dateDay: Int, dateMonth: Int, dateYear: Int) {
        writableDatabase.execSQL("INSERT INTO PASTDATES(dateDay, dateMonth, dateYear, targetWeightAtTime) VALUES ($dateDay, $dateMonth, $dateYear, ${getCurrentTargetWeight()})")
    }

    fun addToPastDatesTesting(dateDay: Int, dateMonth: Int, dateYear: Int, weight: Double) {
        writableDatabase.execSQL("INSERT INTO PASTDATES(dateDay, dateMonth, dateYear, targetWeightAtTime) VALUES ($dateDay, $dateMonth, $dateYear, ${weight})")
    }

    fun getNewestDateInDatabase(): LocalDate {
        var newestDate = LocalDate.now()
        val cursor = readableDatabase.rawQuery("SELECT * FROM PASTDATES", null)
        var tableHasData = false
        while (cursor.moveToNext()) {
            if (cursor.isLast) {
                newestDate = LocalDate.of(cursor.getInt(2), cursor.getInt(1), cursor.getInt(0))
                tableHasData = true
            }
        }
        if (!tableHasData) {
            addToPastDates(newestDate.dayOfMonth, newestDate.monthValue, newestDate.year)
        }
        cursor.close()
        return newestDate
    }

    fun getCurrentTargetWeight(): Int {
        var currentTargetWeight = 0
        val cursor = readableDatabase.rawQuery("SELECT * FROM STOREDAPPINFORMATION", null)
        while (cursor.moveToNext()) {
            currentTargetWeight = cursor.getInt(0)
        }
        cursor.close()
        return currentTargetWeight
    }

    fun getAllWeight(): List<WeightInfo> {
        val lstOfWeights = mutableListOf<WeightInfo>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM PASTDATES", null)
        while (cursor.moveToNext()) {
            if (cursor.getDouble(3).toInt() != 0) {
                val weightInfo = WeightInfo(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getDouble(3)
                )
                lstOfWeights.add(weightInfo)
            }
        }
        cursor.close()
        return lstOfWeights
    }

    fun checkIfExistingColorTheme(): Int {
        var desiredColorTheme = -1
        val cursor = readableDatabase.rawQuery("SELECT * FROM STOREDAPPINFORMATION", null)
        if (cursor.moveToFirst()) {
            desiredColorTheme = cursor.getInt(cursor.getColumnIndexOrThrow("colorTheme"))
        }
        cursor.close()
        return desiredColorTheme
    }

    fun insertFood(foodEntry: AddFoodPage.FoodEntry) {
        writableDatabase.execSQL(
            "INSERT INTO MEALS VALUES(\"${foodEntry.meal}\", \"${foodEntry.description}\", " +
                    "${foodEntry.calories}, ${foodEntry.protein}, ${foodEntry.carbs}, ${foodEntry.fat})"
        )
    }

    fun deleteFoodItem(foodEntry: AddFoodPage.FoodEntry) {
        writableDatabase.execSQL(
            "DELETE FROM MEALS WHERE mealName = ? AND foodName = ? AND  calories = ? AND protein = ? AND carbs = ? AND fat = ?",
            arrayOf(foodEntry.meal, foodEntry.description, foodEntry.calories, foodEntry.protein, foodEntry.carbs, foodEntry.fat)
        )
    }

    fun deleteAllFoodItems() {
        writableDatabase.execSQL("DELETE FROM MEALS")
    }

    fun updateColorThemeInDB(colorTheme: Int) {
        val cursor = readableDatabase.rawQuery("SELECT * FROM STOREDAPPINFORMATION", null)
        if (cursor.moveToNext()) {
            writableDatabase.execSQL("UPDATE STOREDAPPINFORMATION SET colorTheme = $colorTheme")
        } else {
            writableDatabase.execSQL("INSERT INTO STOREDAPPINFORMATION(targetWeight, colorTheme) VALUES (null, $colorTheme)")
        }
        cursor.close()
    }

    @SuppressLint("Recycle")
    fun getTotalMealNutrition(mealName: String): TotalMealInfo {
        var calories = 0
        var protein = 0
        var carbs = 0
        var fat = 0
        val info = TotalMealInfo("0", "0", "0", "0")
        val cursor = readableDatabase.rawQuery("SELECT * FROM MEALS WHERE mealName = ?", arrayOf(mealName))
        while (cursor.moveToNext()) {
            calories += cursor.getInt(2)
            protein += cursor.getInt(3)
            carbs += cursor.getInt(4)
            fat += cursor.getInt(5)
        }
        info.calories = calories.toString()
        info.protein = protein.toString()
        info.carbs = carbs.toString()
        info.fat = fat.toString()
        cursor.close()
        return info
    }

    fun getMealItems(mealName: String): MutableList<ItemInfo> {
        val items: MutableList<ItemInfo> = ArrayList()
        val cursor = readableDatabase.rawQuery("SELECT * FROM MEALS WHERE mealName = ?", arrayOf(mealName))
        while (cursor.moveToNext()) {
            val info = ItemInfo(
                cursor.getString(1),
                cursor.getInt(2).toString(),
                cursor.getInt(3).toString(),
                cursor.getInt(4).toString(),
                cursor.getInt(5).toString()
            )
            items.add(info)
        }
        return items
    }

    fun getTotalCalories(): Int {
        var totalCalories = 0
        val cursor = readableDatabase.rawQuery("SELECT * FROM MEALS", null)
        while (cursor.moveToNext()) {
            totalCalories += cursor.getInt(2)
        }
        cursor.close()
        return totalCalories
    }

    fun getTotalProtein(): Int {
        var totalProtein = 0
        val cursor = readableDatabase.rawQuery("SELECT * FROM MEALS", null)
        while (cursor.moveToNext()) {
            totalProtein += cursor.getInt(3)
        }
        cursor.close()
        return totalProtein
    }

    fun getTotalCarbs(): Int {
        var totalCarbs = 0
        val cursor = readableDatabase.rawQuery("SELECT * FROM MEALS", null)
        while (cursor.moveToNext()) {
            totalCarbs += cursor.getInt(4)
        }
        cursor.close()
        return totalCarbs
    }

    fun getTotalFat(): Int {
        var totalFat = 0
        val cursor = readableDatabase.rawQuery("SELECT * FROM MEALS", null)
        while (cursor.moveToNext()) {
            totalFat += cursor.getInt(5)
        }
        cursor.close()
        return totalFat
    }

    fun getRecommendedCalories(): Int {
        val cursor = readableDatabase.rawQuery("SELECT * FROM USERPROFILE", null)
        var recommendCaloriesReturn = 2000
        if (cursor.moveToNext()) {
            recommendCaloriesReturn = cursor.getInt(6)
        }
        cursor.close()
        return recommendCaloriesReturn
    }

    class userDataDM(context: Context) : DatabaseManager(context) {
        fun insertUserData(myUserData: UserData) {
            writableDatabase.execSQL("INSERT INTO USERPROFILE VALUES(${myUserData.name}, ${myUserData.emailAddress}, ${myUserData.age}, ${myUserData.weight}, ${myUserData.inchHeight})")
        }

        @SuppressLint("Recycle")
        fun getFirstMealType(): String {
            val cursor = readableDatabase.rawQuery("SELECT * FROM MEALS", null)
            return cursor.getString(0)
        }
    }
}
