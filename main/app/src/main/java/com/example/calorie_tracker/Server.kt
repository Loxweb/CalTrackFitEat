package com.example.calorie_tracker

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

data class UserData(
    val name: String,
    val emailAddress: String,
    val age: Int,
    val weight: Double,
    val inchHeight: Double,
    val gender: Int,
    var recommendedCalories: Int = 2000
)

data class foodMealInformation(
    val mealType: MealTypes,
    val Calories: Double,
    val Protein: Double,
    val Carbs: Double,
    val Fat: Double,
    val foodName: String
)

data class dateAndWieght(
    var mealType: MealTypes,
    var weight: Double
)

class Server {
    var currentUserData: UserData? = null

    fun setUserData(name: String, email: String, age: Int, weight: Double, inchHeight: Double, gender: Int) {
        currentUserData = UserData(
            name = name,
            emailAddress = email,
            age = age,
            weight = weight,
            inchHeight = inchHeight,
            gender = gender,
            recommendedCalories = 0
        )
        if (gender == 1) {
            currentUserData!!.recommendedCalories = ((655 + (4.35 * weight) + (4.7 * inchHeight) - (4.67 * age)) * 1.55).roundToInt()
        } else {
            currentUserData!!.recommendedCalories = ((66 + (6.23 * weight) + (12.7 * inchHeight) - (6.75 * age)) * 1.55).roundToInt()
        }
    }

    fun getUserData(): UserData? {
        return currentUserData
    }

    fun userDataForSQL(): String {
        return "'${currentUserData?.name}', '${currentUserData?.emailAddress}', ${currentUserData?.age}, ${currentUserData?.weight}, ${currentUserData?.inchHeight}, ${currentUserData?.gender}, ${currentUserData?.recommendedCalories}"
    }

    fun userDataToString(): String {
        return "Name: ${currentUserData?.name}, Email: ${currentUserData?.emailAddress}, Weight: ${currentUserData?.weight}, Age: ${currentUserData?.age}, Height: ${currentUserData?.inchHeight}"
    }
}

enum class Screens {
    MAINSCREEN,
    FOODPAGE,
    GRAPHPAGE,
    WORKOUTPAGE,
    PROFILEPAGE
}

enum class MealTypes {
    BREAKFAST,
    LUNCH,
    DINNER,
    DESERT,
    SNACKS,
}

enum class Gender {
    MALE,
    FEMALE
}
