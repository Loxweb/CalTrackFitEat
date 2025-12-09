package com.example.calorie_tracker

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ProfilePageUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testEmptyProfilePageUpdate() {
        composeTestRule.setContent {
            Main(LocalContext.current)
        }
        composeTestRule.onNodeWithTag("NavhostDropdownMenuIcon").performClick()
        composeTestRule.onNodeWithTag("NavhostDropdownMenu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithTag("updateProfileButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("updateProfileButton").performClick()
    }

    @Test
    fun testIncorrectProfilePageData() {
        composeTestRule.setContent {
            Main(LocalContext.current)
        }
        composeTestRule.onNodeWithTag("NavhostDropdownMenuIcon").performClick()
        composeTestRule.onNodeWithTag("NavhostDropdownMenu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithTag("updateProfileButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Name-input").performTextInput("testFailName")
        composeTestRule.onNodeWithTag("Email-input").performTextInput("testFailEmail")
        composeTestRule.onNodeWithTag("Weight (lbs)-input").performTextInput("Non-Doublables...")
        composeTestRule.onNodeWithTag("Age-input").performTextInput("your moms weight hehehe")
        composeTestRule.onNodeWithTag("Height (Inches)-input").performTextInput("goliath")
        composeTestRule.onNodeWithTag("updateProfileButton").performClick()
    }

    @Test
    fun testCorrectProfilePageDataAndDatabase() {
        composeTestRule.setContent {
            val dbman = DatabaseManager(LocalContext.current)
            Main(LocalContext.current)
            var name = "testName"
            var email = "testEmail"
            var age = 25
            var weight = 160.0
            var inchHeight = 62.0
            server.setUserData(name, email, age, weight, inchHeight, 1)
            val addedString = server.userDataForSQL()
            dbman.insertUserProfileData(addedString)
            val cursor = dbman.readableDatabase.rawQuery("SELECT * FROM USERPROFILE", null)
            while (cursor.moveToNext()) {
                name = cursor.getString(0)
                email = cursor.getString(1)
                age = cursor.getInt(2)
                weight = cursor.getDouble(3)
                inchHeight = cursor.getDouble(4)
            }
            cursor.close()
            assertEquals("testName", name)
            assertEquals("testEmail", email)
            assertEquals(25, age)
            val delta = 0.01
            assertEquals(160.0, weight, delta)
            assertEquals(62.0, inchHeight, delta)
        }
    }

    @Test
    fun testEmptyTargetWeights() {
        composeTestRule.setContent {
            Main(LocalContext.current)
        }
        composeTestRule.onNodeWithTag("NavhostDropdownMenuIcon").performClick()
        composeTestRule.onNodeWithTag("NavhostDropdownMenu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithTag("updateProfileButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("updateTargetWeightButton").performClick()
    }

    @Test
    fun testStringedTargetWeights() {
        composeTestRule.setContent {
            Main(LocalContext.current)
        }
        composeTestRule.onNodeWithTag("NavhostDropdownMenuIcon").performClick()
        composeTestRule.onNodeWithTag("NavhostDropdownMenu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithTag("updateProfileButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Target Weight-input").performTextInput("testFailWeight")
        composeTestRule.onNodeWithTag("updateTargetWeightButton").performClick()
    }

    @Test
    fun testCorrectWeightDataInDatabase() {
        composeTestRule.setContent {
            val dbman = DatabaseManager(LocalContext.current)
            Main(LocalContext.current)
            val targetWeight = 180
            var checkTargetWeight = 0
            dbman.insertUpdatedWeight(targetWeight, ColorTheme.DARK)
            val cursor = dbman.readableDatabase.rawQuery("SELECT * FROM STOREDAPPINFORMATION", null)
            while (cursor.moveToNext()) {
                checkTargetWeight = cursor.getInt(0)
            }
            cursor.close()
            assertEquals(targetWeight, checkTargetWeight)
        }
    }
}
