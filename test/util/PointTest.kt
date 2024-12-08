package util

import junit.framework.TestCase.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PointTest {

    @Test
    fun `Point add`() {
        val a = Point(-1, -2)
        val b = Point(4, 5)
        val expected = Point(3, 3)
        assertEquals(expected, a + b)
    }

    @Test
    fun `Point minus`() {
        val a = Point(-1, -2)
        val b = Point(4, 5)
        val expected = Point(-5, -7)
        assertEquals(expected, a - b)
    }

    @Test
    fun `Point in Area`() {
        val input = listOf(
            "..*..", // 2 0
            "..1..", // 2 1
            ".2.2.", // 1 2 3 2
            "3.3.3", // 0 3  2 3  4 3
            "..|..", // 2 4
        )
        val area = Area(input)
        assertTrue(Point(0,0) in area)
        assertTrue(Point(3,3) in area)
        assertTrue(Point(4,4) in area)
        assertFalse(Point(-1,-2) in area)
        assertFalse(Point(-2,3) in area)
        assertFalse(Point(2,5) in area)
        assertFalse(Point(5,5) in area)
    }
}