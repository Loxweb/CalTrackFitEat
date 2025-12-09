package com.example.calorie_tracker

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class AddFoodPageUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun goodCustomEntryTest() {
        composeTestRule.setContent {
            val dbman = DatabaseManager(LocalContext.current)
            dbman.deleteAllFoodItems()
            dbman.clearStoredAppData()
            Main(LocalContext.current)
        }
        composeTestRule.onNodeWithTag("Breakfast-Button").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("add-food").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("custom-entry").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("Description-input").assertIsDisplayed().performTextInput("Donut")
        composeTestRule.onNodeWithTag("Calories-input").assertIsDisplayed().performTextInput("200")
        composeTestRule.onNodeWithTag("Protein-input").assertIsDisplayed().performTextInput("2")
        composeTestRule.onNodeWithTag("Carbohydrates-input").assertIsDisplayed().performTextInput("100")
        composeTestRule.onNodeWithTag("Fat-input").assertIsDisplayed().performTextInput("15")
        composeTestRule.onNodeWithTag("add-custom-entry").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("item-0").assertIsDisplayed()
        composeTestRule.onNodeWithTag("item-name").assertTextEquals("Donut")
        composeTestRule.onNodeWithTag("item-values").assertTextEquals("200 calories, 100g carbs, 2g protein, 15g fat")
        composeTestRule.onNodeWithTag("delete-item").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("custom-entry").assertIsNotDisplayed()
    }

    @Test
    fun badValuesCustomEntry() {
        composeTestRule.setContent {
            val dbman = DatabaseManager(LocalContext.current)
            dbman.clearStoredAppData()
            Main(LocalContext.current)
        }
        composeTestRule.onNodeWithTag("Breakfast-Button").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("add-food").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("custom-entry").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("Description-input").assertIsDisplayed().performTextInput("Donut")
        composeTestRule.onNodeWithTag("Calories-input").assertIsDisplayed().performTextInput("test")
        composeTestRule.onNodeWithTag("Protein-input").assertIsDisplayed().performTextInput("test")
        composeTestRule.onNodeWithTag("Carbohydrates-input").assertIsDisplayed().performTextInput("100")
        composeTestRule.onNodeWithTag("Fat-input").assertIsDisplayed().performTextInput("15")
        composeTestRule.onNodeWithTag("add-custom-entry").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("item-0").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("add-custom-entry").assertIsDisplayed()
    }

    @Test
    fun noValuesCustomEntry() {
        composeTestRule.setContent {
            val dbman = DatabaseManager(LocalContext.current)
            dbman.clearStoredAppData()
            Main(LocalContext.current)
        }
        composeTestRule.onNodeWithTag("Breakfast-Button").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("add-food").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("custom-entry").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("Description-input").assertIsDisplayed().performTextInput("")
        composeTestRule.onNodeWithTag("Calories-input").assertIsDisplayed().performTextInput("")
        composeTestRule.onNodeWithTag("Protein-input").assertIsDisplayed().performTextInput("")
        composeTestRule.onNodeWithTag("Carbohydrates-input").assertIsDisplayed().performTextInput("")
        composeTestRule.onNodeWithTag("Fat-input").assertIsDisplayed().performTextInput("")
        composeTestRule.onNodeWithTag("add-custom-entry").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("item-0").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("add-custom-entry").assertIsDisplayed()
    }

    @Test
    fun multipleEntries() {
        composeTestRule.setContent {
            val dbman = DatabaseManager(LocalContext.current)
            dbman.clearStoredAppData()
            Main(LocalContext.current)
        }
        composeTestRule.onNodeWithTag("Breakfast-Button").assertIsDisplayed().performClick()
        for (i in 0..4) {
            addCustomEntry()
        }
        composeTestRule.onNodeWithTag("item-0").assertIsDisplayed()
        composeTestRule.onNodeWithTag("item-1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("item-2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("item-3").assertIsDisplayed()
        composeTestRule.onNodeWithTag("item-4").assertIsDisplayed()
    }

    fun addCustomEntry() {
        composeTestRule.onNodeWithTag("add-food").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("custom-entry").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithTag("Description-input").assertIsDisplayed().performTextInput("candy")
        composeTestRule.onNodeWithTag("Calories-input").assertIsDisplayed().performTextInput("100")
        composeTestRule.onNodeWithTag("Protein-input").assertIsDisplayed().performTextInput("10")
        composeTestRule.onNodeWithTag("Carbohydrates-input").assertIsDisplayed().performTextInput("75")
        composeTestRule.onNodeWithTag("Fat-input").assertIsDisplayed().performTextInput("50")
        composeTestRule.onNodeWithTag("add-custom-entry").assertIsDisplayed().performClick()
    }

    @Test
    fun addDBAndDelete() {
        composeTestRule.setContent {
            var dbman = DatabaseManager(LocalContext.current)
            var temp = AddFoodPage.FoodEntry(
                meal = "Breakfast",
                description = "donut",
                calories = "200",
                carbs = "100",
                protein = "2",
                fat = "7"
            )
            dbman.insertFood(temp)
            var items = dbman.getMealItems("Breakfast")
            assertEquals(1, items.size)
            assertEquals("200", items[0].calories)
            assertEquals("100", items[0].carbs)
            assertEquals("2", items[0].protein)
            assertEquals("7", items[0].fat)
            dbman.deleteFoodItem(temp)
            var itemsNew = dbman.getMealItems("Breakfast")
            assertEquals(0, itemsNew.size)
        }
    }

    @Test
    fun deleteAllDB() {
        composeTestRule.setContent {
            var dbman = DatabaseManager(LocalContext.current)
            var temp = AddFoodPage.FoodEntry(
                meal = "Breakfast",
                description = "donut",
                calories = "200",
                carbs = "100",
                protein = "2",
                fat = "7"
            )
            for (i in 0..10) {
                dbman.insertFood(temp)
            }
            var items = dbman.getMealItems("Breakfast")
            assertEquals(11, items.size)
            dbman.deleteAllFoodItems()
            var newItems = dbman.getMealItems("Breakfast")
            assertEquals(0, newItems.size)
        }
    }

    @Test
    fun getTotalNutrition() {
        composeTestRule.setContent {
            var dbman = DatabaseManager(LocalContext.current)
            var temp = AddFoodPage.FoodEntry(
                meal = "Breakfast",
                description = "donut",
                calories = "200",
                carbs = "100",
                protein = "2",
                fat = "7"
            )
            for (i in 0..9) {
                dbman.insertFood(temp)
            }
            var totalNutrition = dbman.getTotalMealNutrition("Breakfast")
            assertEquals("2000", totalNutrition.calories)
            assertEquals("1000", totalNutrition.carbs)
            assertEquals("20", totalNutrition.protein)
            assertEquals("70", totalNutrition.fat)
        }
    }
}
