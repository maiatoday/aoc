package util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Point3Test {
    @Test
    fun `3D point cardinal neighbours`() {
        val current = Point3(1, 1, 1)
        val neighbours = current.neighbours(includeDiagonal = false)
        // cardinal neighbours only
        val expected = setOf(
            Point3(1, 1, 0),
            Point3(1, 1, 2),
            Point3(1, 0, 1),
            Point3(1, 2, 1),
            Point3(0, 1, 1),
            Point3(2, 1, 1),
        )
        assertEquals(expected, neighbours)
    }

    @Test
    fun `3D point cardinal neighbours with extra filter`() {
        val current = Point3(1, 1, 1)
        val neighbours = current.neighbours(includeDiagonal = false) { it.x != 2 && it.y != 2 }
        // cardinal neighbours only
        val expected = setOf(
            Point3(1, 1, 0),
            Point3(1, 1, 2),
            Point3(1, 0, 1),
            Point3(0, 1, 1)
        )
        assertEquals(expected, neighbours)
    }

    @Test
    fun `3D point neighbours include self`() {
        val current = Point3(1, 1, 1)
        val neighbours = current.neighbours(includeSelf = true)
        assertTrue(current in neighbours)
    }

    @Test
    fun `3D point neighbours exclude self`() {
        val current = Point3(1, 1, 1)
        val neighbours = current.neighbours(includeSelf = false)
        assertTrue(current !in neighbours)
    }

    @Test
    fun `3D point neighbours with diagonals`() {
        val current = Point3(1, 1, 1)
        val neighbours = current.neighbours(includeDiagonal = true)
        val expectedSize = 9 + 9 + 8
        assertEquals(expectedSize, neighbours.size)
    }

    @Test
    fun `3D point shares sides`() {
        assertTrue(Point3(1, 1, 1).shareSides(Point3(0, 1, 1)))
        assertTrue(Point3(1, 1, 1).shareSides(Point3(1, 0, 1)))
        assertTrue(Point3(1, 1, 1).shareSides(Point3(1, 1, 0)))
    }

    @Test
    fun `3D point doesn't share sides`() {
        assertFalse(Point3(1, 1, 1).shareSides(Point3(1, 1, 1)))
        assertFalse(Point3(1, 1, 1).shareSides(Point3(0, 0, 0)))
    }
}
