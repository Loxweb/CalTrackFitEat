package com.example.calorie_tracker

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ViewGraphPageUITest {
    @Test
    fun testNumberIncremented() {
        val lst = listOf(205, 218, 215, 200, 215, 213, 234)
        val srtdLst = lst.sorted()
        val numInc = viewGraphPage.NumberIncremented(lst, srtdLst)
        assertEquals(numInc, 6)
    }
    @Test
    fun testNumberIncremented2() {
        val lst = listOf(205, 206, 207, 208, 209, 210, 211)
        val srtdLst = lst.sorted()
        val numInc = viewGraphPage.NumberIncremented(lst, srtdLst)
        assertEquals(numInc, 1)
    }
    @Test
    fun testNumberIncremented3() {
        val lst = listOf(205, 218, 215, 200, 215, 213, 234, 230, 228, 225, 226, 217)
        val srtdLst = lst.sorted()
        val numInc = viewGraphPage.NumberIncremented(lst, srtdLst)
        assertEquals(numInc, 4)
    }
    @Test
    fun testUnitToColumLabel() {
        val lst = listOf(205, 218, 215, 200, 215, 213, 234)
        val columnLabelAns = listOf(200, 206, 212, 218, 224, 230, 236)

        val srtdLst = lst.sorted()
        val numInc = viewGraphPage.NumberIncremented(lst, srtdLst)
        val columLabels = viewGraphPage.unitToColumLabel(srtdLst, numInc, 7)
        assertEquals(columnLabelAns, columLabels)
    }
    @Test
    fun testUnitToColumLabel2() {
        val lst = listOf(205, 218, 215, 200, 215, 213, 234, 230, 228, 225, 226, 217)
        val columnLabelAns = listOf(200, 204, 208, 212, 216, 220, 224, 228, 232, 236, 240, 244)

        val srtdLst = lst.sorted()
        val numInc = viewGraphPage.NumberIncremented(lst, srtdLst)
        val columLabels = viewGraphPage.unitToColumLabel(srtdLst, numInc, 12)
        assertEquals(columnLabelAns, columLabels)
    }
    @Test
    fun testUnitToGridComplex() {
        val lst = listOf(205, 218, 215, 200, 215, 213, 234, 230, 228, 225, 226, 217)
        val ans = listOf(2, 5, 4, 0, 4, 4, 9, 8, 8, 7, 7, 5)
        val srtdLst = lst.sorted()

        val numInc = viewGraphPage.NumberIncremented(lst, srtdLst)
        val columLabels = viewGraphPage.unitToColumLabel(srtdLst, numInc, 12)
        val ugc = viewGraphPage.unitToGridComplex(lst, columLabels)
        assertEquals(ans, ugc)
    }
    @Test
    fun testUnitToGridComplex2() {
        val lst = listOf(190, 191, 191, 192, 194, 195, 200)
        val ans = listOf(0, 1, 1, 2, 3, 3, 6)
        val srtdLst = lst.sorted()

        val numInc = viewGraphPage.NumberIncremented(lst, srtdLst)
        val columLabels = viewGraphPage.unitToColumLabel(srtdLst, numInc, 7)
        val ugc = viewGraphPage.unitToGridComplex(lst, columLabels)
        assertEquals(ans, ugc)
    }
    @Test
    fun testGetDailyWeightList() {
        val WI1 = WeightInfo(1, 1, 2024, 206.0)
        val WI2 = WeightInfo(2, 1, 2024, 210.0)
        val WI3 = WeightInfo(3, 1, 2024, 215.0)
        val WI4 = WeightInfo(4, 1, 2024, 201.0)
        val WI = listOf(WI1, WI2, WI3, WI4)

        val WD1 = WeightDate(206, "1/1/2024")
        val WD2 = WeightDate(210, "2/1/2024")
        val WD3 = WeightDate(215, "3/1/2024")
        val WD4 = WeightDate(201, "4/1/2024")

        val ans = listOf(WD1, WD2, WD3, WD4)

        val dailyWeightLst = viewGraphPage.getDailyWeightList(WI)
        assertEquals(ans, dailyWeightLst)
    }
    @Test
    fun testGetDailyWeightList2() {
        val WI1 = WeightInfo(1, 1, 2024, 206.0)
        val WI2 = WeightInfo(2, 1, 2024, 210.0)
        val WI3 = WeightInfo(3, 1, 2024, 215.0)
        val WI4 = WeightInfo(4, 1, 2024, 201.0)
        val WI5 = WeightInfo(5, 1, 2024, 205.0)
        val WI6 = WeightInfo(6, 1, 2024, 211.0)
        val WI7 = WeightInfo(7, 1, 2024, 219.0)
        val WI8 = WeightInfo(8, 1, 2024, 220.0)


        val WI = listOf(WI1, WI2, WI3, WI4, WI5, WI6, WI7, WI8)

        val WD1 = WeightDate(210, "2/1/2024")
        val WD2 = WeightDate(215, "3/1/2024")
        val WD3 = WeightDate(201, "4/1/2024")
        val WD4 = WeightDate(205, "5/1/2024")
        val WD5 = WeightDate(211, "6/1/2024")
        val WD6 = WeightDate(219, "7/1/2024")
        val WD7 = WeightDate(220, "8/1/2024")


        val ans = listOf(WD1, WD2, WD3, WD4, WD5, WD6, WD7)

        val dailyWeightLst = viewGraphPage.getDailyWeightList(WI)
        assertEquals(ans, dailyWeightLst)
    }

    @Test
    fun testGetWeeklyWeightList() {
        val WI1 = WeightInfo(1, 1, 2024, 206.0)
        val WI2 = WeightInfo(2, 1, 2024, 210.0)
        val WI3 = WeightInfo(3, 1, 2024, 215.0)
        val WI4 = WeightInfo(4, 1, 2024, 201.0)
        val WI5 = WeightInfo(5, 1, 2024, 205.0)
        val WI6 = WeightInfo(6, 1, 2024, 211.0)
        val WI7 = WeightInfo(7, 1, 2024, 219.0) //7
        val WI8 = WeightInfo(8, 1, 2024, 206.0)
        val WI9 = WeightInfo(9, 1, 2024, 210.0)
        val WI10 = WeightInfo(10, 1, 2024, 215.0)
        val WI11 = WeightInfo(11, 1, 2024, 201.0)
        val WI12 = WeightInfo(12, 1, 2024, 205.0)
        val WI13 = WeightInfo(13, 1, 2024, 211.0)
        val WI14 = WeightInfo(14, 1, 2024, 219.0) //7
        val WI15 = WeightInfo(15, 1, 2024, 206.0)
        val WI16 = WeightInfo(16, 1, 2024, 210.0)
        val WI17 = WeightInfo(17, 1, 2024, 215.0)
        val WI18 = WeightInfo(18, 1, 2024, 201.0)
        val WI19 = WeightInfo(19, 1, 2024, 205.0)
        val WI20 = WeightInfo(20, 1, 2024, 211.0)
        val WI21 = WeightInfo(21, 1, 2024, 219.0) //7

        val WI = listOf(
            WI1, WI2, WI3, WI4, WI5, WI6, WI7,
            WI8, WI9, WI10, WI11, WI12, WI13, WI14,
            WI15, WI16, WI17, WI18, WI19, WI20, WI21
        )

        val WD1 = WeightDate(209, "7/1/2024")
        val WD2 = WeightDate(209, "14/1/2024")
        val WD3 = WeightDate(209, "21/1/2024")

        val ans = listOf(WD1, WD2, WD3)

        val dailyWeightLst = viewGraphPage.getWeeklyWeightList(WI)
        assertEquals(ans, dailyWeightLst)
    }
    @Test
    fun testGetWeeklyWeightList2() {
        val WI1 = WeightInfo(1, 1, 2024, 206.0)
        val WI2 = WeightInfo(2, 1, 2024, 210.0)
        val WI3 = WeightInfo(3, 1, 2024, 215.0)
        val WI4 = WeightInfo(4, 1, 2024, 201.0)
        val WI5 = WeightInfo(5, 1, 2024, 205.0)
        val WI6 = WeightInfo(6, 1, 2024, 211.0)
        val WI7 = WeightInfo(7, 1, 2024, 219.0) //7
        val WI8 = WeightInfo(8, 1, 2024, 206.0)
        val WI9 = WeightInfo(9, 1, 2024, 210.0)
        val WI10 = WeightInfo(10, 1, 2024, 215.0)
        val WI11 = WeightInfo(11, 1, 2024, 201.0)
        val WI12 = WeightInfo(12, 1, 2024, 205.0)
        val WI13 = WeightInfo(13, 1, 2024, 211.0)
        val WI14 = WeightInfo(14, 1, 2024, 219.0) //7
        val WI15 = WeightInfo(15, 1, 2024, 206.0)
        val WI16 = WeightInfo(16, 1, 2024, 210.0)
        val WI17 = WeightInfo(17, 1, 2024, 215.0)
        val WI18 = WeightInfo(18, 1, 2024, 201.0)
        val WI19 = WeightInfo(19, 1, 2024, 205.0)
        val WI20 = WeightInfo(20, 1, 2024, 211.0)
        val WI21 = WeightInfo(21, 1, 2024, 219.0) //7
        val WI22 = WeightInfo(22, 1, 2024, 206.0)
        val WI23 = WeightInfo(23, 1, 2024, 210.0)
        val WI24 = WeightInfo(24, 1, 2024, 215.0)
        val WI25 = WeightInfo(25, 1, 2024, 201.0)
        val WI26 = WeightInfo(26, 1, 2024, 205.0)
        val WI27 = WeightInfo(27, 1, 2024, 211.0)
        val WI28 = WeightInfo(28, 1, 2024, 219.0) // 7
        val WI29 = WeightInfo(29, 1, 2024, 206.0)
        val WI30 = WeightInfo(30, 1, 2024, 210.0)
        val WI31 = WeightInfo(31, 1, 2024, 215.0)
        val WI32 = WeightInfo(1, 2, 2024, 201.0)
        val WI33 = WeightInfo(2, 2, 2024, 205.0)
        val WI34 = WeightInfo(3, 2, 2024, 211.0)
        val WI35 = WeightInfo(4, 2, 2024, 219.0) // 7
        val WI36 = WeightInfo(5, 2, 2024, 206.0)
        val WI37 = WeightInfo(6, 2, 2024, 210.0)
        val WI38 = WeightInfo(7, 2, 2024, 215.0)
        val WI39 = WeightInfo(8, 2, 2024, 201.0)
        val WI40 = WeightInfo(9, 2, 2024, 205.0)
        val WI41 = WeightInfo(10, 2, 2024, 211.0)


        val WI = listOf(
            WI1, WI2, WI3, WI4, WI5, WI6, WI7,
            WI8, WI9, WI10, WI11, WI12, WI13, WI14,
            WI15, WI16, WI17, WI18, WI19, WI20, WI21,
            WI22, WI23, WI24, WI25, WI26, WI27, WI28,
            WI29, WI30, WI31, WI32, WI33, WI34, WI35,
            WI35, WI36, WI37, WI38, WI39, WI40, WI41,
        )

        val WD1 = WeightDate(209, "14/1/2024")
        val WD2 = WeightDate(209, "21/1/2024")
        val WD3 = WeightDate(209, "28/1/2024")
        val WD4 = WeightDate(209, "4/2/2024")
        val WD5 = WeightDate(209, "10/2/2024")

        val ans = listOf(WD1, WD2, WD3, WD4, WD5)

        val dailyWeightLst = viewGraphPage.getWeeklyWeightList(WI)
        assertEquals(ans, dailyWeightLst)
    }
    @Test
    fun testGetYearlyWeightList() {
        val WI1 = WeightInfo(1, 1, 2024, 206.0)
        val WI2 = WeightInfo(2, 1, 2024, 210.0)
        val WI3 = WeightInfo(3, 1, 2024, 215.0)
        val WI4 = WeightInfo(4, 1, 2024, 201.0)
        val WI5 = WeightInfo(5, 1, 2024, 205.0)
        val WI6 = WeightInfo(6, 1, 2024, 211.0)
        val WI7 = WeightInfo(7, 1, 2024, 219.0) //7
        val WI8 = WeightInfo(1, 2, 2024, 206.0)
        val WI9 = WeightInfo(2, 2, 2024, 210.0)
        val WI10 = WeightInfo(3, 2, 2024, 215.0)
        val WI11 = WeightInfo(4, 2, 2024, 201.0)
        val WI12 = WeightInfo(5, 2, 2024, 205.0)
        val WI13 = WeightInfo(6, 2, 2024, 211.0)
        val WI14 = WeightInfo(7, 2, 2024, 219.0) //7
        val WI15 = WeightInfo(1, 2, 2024, 206.0)
        val WI16 = WeightInfo(2, 2, 2024, 210.0)
        val WI17 = WeightInfo(3, 2, 2024, 215.0)
        val WI18 = WeightInfo(4, 2, 2024, 201.0)
        val WI19 = WeightInfo(5, 2, 2024, 205.0)
        val WI20 = WeightInfo(6, 2, 2024, 211.0)
        val WI21 = WeightInfo(7, 2, 2024, 219.0) //7
        val WI22 = WeightInfo(1, 3, 2024, 206.0)
        val WI23 = WeightInfo(2, 3, 2024, 210.0)
        val WI24 = WeightInfo(3, 3, 2024, 215.0)
        val WI25 = WeightInfo(4, 3, 2024, 201.0)
        val WI26 = WeightInfo(5, 3, 2024, 205.0)
        val WI27 = WeightInfo(6, 3, 2024, 211.0)
        val WI28 = WeightInfo(7, 3, 2024, 219.0) // 7
        val WI29 = WeightInfo(1, 4, 2024, 200.0)
        val WI30 = WeightInfo(2, 4, 2024, 205.0)
        val WI31 = WeightInfo(3, 4, 2024, 208.0)
        val WI32 = WeightInfo(4, 4, 2024, 207.0)
        val WI33 = WeightInfo(5, 4, 2024, 209.0)
        val WI34 = WeightInfo(6, 4, 2024, 210.0)
        val WI35 = WeightInfo(7, 4, 2024, 201.0) // 7
        val WI36 = WeightInfo(1, 5, 2024, 200.0)
        val WI37 = WeightInfo(2, 5, 2024, 205.0)
        val WI38 = WeightInfo(3, 5, 2024, 208.0)
        val WI39 = WeightInfo(4, 5, 2024, 207.0)
        val WI40 = WeightInfo(5, 5, 2024, 209.0)
        val WI41 = WeightInfo(6, 5, 2024, 201.0) // 7

        val WI = listOf(
            WI1, WI2, WI3, WI4, WI5, WI6, WI7,
            WI8, WI9, WI10, WI11, WI12, WI13, WI14,
            WI15, WI16, WI17, WI18, WI19, WI20, WI21,
            WI22, WI23, WI24, WI25, WI26, WI27, WI28,
            WI29, WI30, WI31, WI32, WI33, WI34, WI35,
            WI35, WI36, WI37, WI38, WI39, WI40, WI41
        )

        val WD1 = WeightDate(209, "1/2024")
        val WD2 = WeightDate(209, "2/2024")
        val WD3 = WeightDate(209, "3/2024")
        val WD4 = WeightDate(205, "4/2024")
        val WD5 = WeightDate(205, "5/2024")

        val ans = listOf(WD1, WD2, WD3, WD4, WD5)

        val dailyWeightLst = viewGraphPage.getYearlyWeightList(WI)
        assertEquals(ans, dailyWeightLst)
    }

    @Test
    fun testGetYearlyWeightList2() {
        val WI1 = WeightInfo(1, 1, 2024, 206.0)
        val WI2 = WeightInfo(2, 1, 2024, 210.0)
        val WI3 = WeightInfo(3, 1, 2024, 215.0)
        val WI4 = WeightInfo(4, 1, 2024, 201.0)
        val WI5 = WeightInfo(5, 1, 2024, 205.0)
        val WI6 = WeightInfo(6, 1, 2024, 211.0)
        val WI7 = WeightInfo(7, 1, 2024, 219.0) //7
        val WI8 = WeightInfo(1, 2, 2024, 206.0)
        val WI9 = WeightInfo(2, 2, 2024, 210.0)
        val WI10 = WeightInfo(3, 2, 2024, 215.0)
        val WI11 = WeightInfo(4, 2, 2024, 201.0)
        val WI12 = WeightInfo(5, 2, 2024, 205.0)
        val WI13 = WeightInfo(6, 2, 2024, 211.0)
        val WI14 = WeightInfo(7, 2, 2024, 219.0) //7
        val WI15 = WeightInfo(1, 2, 2024, 206.0)
        val WI16 = WeightInfo(2, 2, 2024, 210.0)
        val WI17 = WeightInfo(3, 2, 2024, 215.0)
        val WI18 = WeightInfo(4, 2, 2024, 201.0)
        val WI19 = WeightInfo(5, 2, 2024, 205.0)
        val WI20 = WeightInfo(6, 2, 2024, 211.0)
        val WI21 = WeightInfo(7, 2, 2024, 219.0) //7
        val WI22 = WeightInfo(1, 3, 2024, 206.0)
        val WI23 = WeightInfo(2, 3, 2024, 210.0)
        val WI24 = WeightInfo(3, 3, 2024, 215.0)
        val WI25 = WeightInfo(4, 3, 2024, 201.0)
        val WI26 = WeightInfo(5, 3, 2024, 205.0)
        val WI27 = WeightInfo(6, 3, 2024, 211.0)
        val WI28 = WeightInfo(7, 3, 2024, 219.0) // 7
        val WI29 = WeightInfo(1, 4, 2024, 200.0)
        val WI30 = WeightInfo(2, 4, 2024, 205.0)
        val WI31 = WeightInfo(3, 4, 2024, 208.0)
        val WI32 = WeightInfo(4, 4, 2024, 207.0)
        val WI33 = WeightInfo(5, 4, 2024, 209.0)
        val WI34 = WeightInfo(6, 4, 2024, 210.0)
        val WI35 = WeightInfo(7, 4, 2024, 201.0) // 7
        val WI36 = WeightInfo(1, 5, 2024, 200.0)
        val WI37 = WeightInfo(2, 5, 2024, 205.0)
        val WI38 = WeightInfo(3, 5, 2024, 208.0)
        val WI39 = WeightInfo(4, 5, 2024, 207.0)
        val WI40 = WeightInfo(5, 5, 2024, 209.0)
        val WI41 = WeightInfo(6, 5, 2024, 201.0) // 7
        val WI42 = WeightInfo(1, 6, 2024, 200.0)
        val WI43 = WeightInfo(2, 6, 2024, 205.0)
        val WI44 = WeightInfo(3, 6, 2024, 208.0)
        val WI45 = WeightInfo(4, 6, 2024, 207.0)
        val WI46 = WeightInfo(5, 6, 2024, 209.0)
        val WI47 = WeightInfo(6, 6, 2024, 201.0) // 7
        val WI48 = WeightInfo(1, 7, 2024, 200.0)
        val WI49 = WeightInfo(2, 7, 2024, 205.0)
        val WI50 = WeightInfo(3, 7, 2024, 208.0)
        val WI51 = WeightInfo(4, 7, 2024, 207.0)
        val WI52 = WeightInfo(5, 7, 2024, 209.0)
        val WI53 = WeightInfo(6, 7, 2024, 201.0) // 7
        val WI54 = WeightInfo(1, 8, 2024, 200.0)
        val WI55 = WeightInfo(2, 8, 2024, 205.0)
        val WI56 = WeightInfo(3, 8, 2024, 208.0)
        val WI57 = WeightInfo(4, 8, 2024, 207.0)
        val WI58 = WeightInfo(5, 8, 2024, 209.0)
        val WI59 = WeightInfo(6, 8, 2024, 201.0) // 7
        val WI60 = WeightInfo(1, 9, 2024, 200.0)
        val WI61 = WeightInfo(2, 9, 2024, 205.0)
        val WI62 = WeightInfo(3, 9, 2024, 208.0)
        val WI63 = WeightInfo(4, 9, 2024, 207.0)
        val WI64 = WeightInfo(5, 9, 2024, 209.0)
        val WI65 = WeightInfo(6, 9, 2024, 201.0) // 7
        val WI66 = WeightInfo(1, 10, 2024, 200.0)
        val WI67 = WeightInfo(2, 10, 2024, 205.0)
        val WI68 = WeightInfo(3, 10, 2024, 208.0)
        val WI69 = WeightInfo(4, 10, 2024, 207.0)
        val WI70 = WeightInfo(5, 10, 2024, 209.0)
        val WI71 = WeightInfo(6, 10, 2024, 201.0) // 7
        val WI72 = WeightInfo(1, 11, 2024, 200.0)
        val WI73 = WeightInfo(2, 11, 2024, 205.0)
        val WI74 = WeightInfo(3, 11, 2024, 208.0)
        val WI75 = WeightInfo(4, 11, 2024, 207.0)
        val WI76 = WeightInfo(5, 11, 2024, 209.0)
        val WI77 = WeightInfo(6, 11, 2024, 201.0) // 7
        val WI78 = WeightInfo(1, 12, 2024, 200.0)
        val WI79 = WeightInfo(2, 12, 2024, 205.0)
        val WI80 = WeightInfo(3, 12, 2024, 208.0)
        val WI81 = WeightInfo(4, 12, 2024, 207.0)
        val WI82 = WeightInfo(5, 12, 2024, 209.0)
        val WI83 = WeightInfo(6, 12, 2024, 201.0) // 7

        val WI = listOf(
            WI1, WI2, WI3, WI4, WI5, WI6, WI7,
            WI8, WI9, WI10, WI11, WI12, WI13, WI14,
            WI15, WI16, WI17, WI18, WI19, WI20, WI21,
            WI22, WI23, WI24, WI25, WI26, WI27, WI28,
            WI29, WI30, WI31, WI32, WI33, WI34, WI35,
            WI35, WI36, WI37, WI38, WI39, WI40, WI41,
            WI42, WI43, WI44, WI45, WI46, WI47, WI48,
            WI49, WI50, WI51, WI52, WI53, WI54, WI55,
            WI56, WI57, WI58, WI59, WI60, WI61, WI62,
            WI63, WI64, WI65, WI66, WI67, WI68, WI69,
            WI70, WI71, WI72, WI73, WI74, WI75, WI76,
            WI77, WI78, WI79, WI80, WI81, WI82, WI83
        )

        val WD1 = WeightDate(209, "1/2024")
        val WD2 = WeightDate(209, "2/2024")
        val WD3 = WeightDate(209, "3/2024")
        val WD4 = WeightDate(205, "4/2024")
        val WD5 = WeightDate(205, "5/2024")
        val WD6 = WeightDate(205, "6/2024")
        val WD7 = WeightDate(205, "7/2024")
        val WD8 = WeightDate(205, "8/2024")
        val WD9 = WeightDate(205, "9/2024")
        val WD10 = WeightDate(205, "10/2024")
        val WD11 = WeightDate(205, "11/2024")
        val WD12 = WeightDate(205, "12/2024")

        val ans = listOf(WD1, WD2, WD3, WD4, WD5, WD6, WD7, WD8, WD9, WD10, WD11, WD12)

        val dailyWeightLst = viewGraphPage.getYearlyWeightList(WI)
        assertEquals(ans, dailyWeightLst)
    }

    @Test
    fun testGetDates() {
        val WD1 = WeightDate(209, "1/2024")
        val WD2 = WeightDate(209, "2/2024")
        val WD3 = WeightDate(209, "3/2024")
        val WD4 = WeightDate(205, "4/2024")
        val WD5 = WeightDate(205, "5/2024")

        val lstOfWD = listOf(WD1, WD2, WD3, WD4, WD5)
        val ans = listOf("1/2024", "2/2024", "3/2024", "4/2024", "5/2024", "0/0/0000", "0/0/0000")

        val lstOfDate = viewGraphPage.getDates(lstOfWD, 7)
        assertEquals(ans, lstOfDate)
    }


    fun dateToString(year: Int, month: Int, day: Int): String{
        val date = "$day/$month/$year"
        return date
    }

    fun dateToString(year: Int, month: Int): String{
        val yearFormat = year
        val date = "$month/$yearFormat"
        return date
    }
}