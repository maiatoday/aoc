package util

import org.junit.Test
import kotlin.test.assertEquals

class GridUtilsTest {
    @Test
    fun `row points created`() {
        val expected = listOf(
            Point(0, 0),
            Point(1, 0),
            Point(2, 0),
            Point(3, 0),
        )
        assertEquals(expected, rowPoints(0, 0, 4))
    }

    @Test
    fun `column points created`() {
        val expected = listOf(
            Point(0, 0),
            Point(0, 1),
            Point(0, 2),
            Point(0, 3),
        )
        assertEquals(expected, columnPoints(0, 0, 4))
    }

    @Test
    fun `forward diagonal points created`() {
        val expected = listOf(
            Point(3, 0),
            Point(2, 1),
            Point(1, 2),
            Point(0, 3),
        )
        assertEquals(expected, diagonalPoints(3, 0, 4, true))
    }

    @Test
    fun `backward diagonal points created`() {
        val expected = listOf(
            Point(0, 0),
            Point(1, 1),
            Point(2, 2),
            Point(3, 3),
        )
        assertEquals(expected, diagonalPoints(0, 0, 4, false))
    }

    @Test
    fun `extract row`() {
        val grid = listOf(
            ".....",
            ".1234",
            ".....",
            ".....",
            ".....",
        )

        val expected = "1234"
        assertEquals(expected, grid.extractString(rowPoints(1, 1, 4)))
    }

    @Test
    fun `extract column`() {
        val grid = listOf(
            ".....",
            ".1...",
            ".2...",
            ".3...",
            ".4...",
        )

        val expected = "1234"
        assertEquals(expected, grid.extractString(columnPoints(1, 1, 4)))

    }

    @Test
    fun `extract forward diagonal`() {
            val grid = listOf(
                ".....",
                "....1",
                "...2.",
                "..3..",
                ".4...",
            )

            val expected = "1234"
            assertEquals(expected, grid.extractString(diagonalPoints(4, 1, 4, true)))

    }

    @Test
    fun `extract backward diagonal`() {
        val grid = listOf(
            ".....",
            ".1...",
            "..2...",
            "...3.",
            "....4",
        )

        val expected = "1234"
        assertEquals(expected, grid.extractString(diagonalPoints(1, 1, 4, false)))

    }

    @Test
    fun `extract row with fencepost`() {
        val grid = listOf(
            ".....",
            ".1234",
            ".....",
            ".....",
            ".....",
        )

        val expected = "1234"
        assertEquals(expected, grid.extractString(rowPoints(1, 1, 5)))
    }

    @Test
    fun `extract column with fencepost`() {
        val grid = listOf(
            ".....",
            ".1...",
            ".2...",
            ".3...",
            ".4...",
        )

        val expected = "1234"
        assertEquals(expected, grid.extractString(columnPoints(1, 1, 5)))

    }

    @Test
    fun `extract forward diagonal with fencepost`() {
        val grid = listOf(
            ".....",
            "....1",
            "...2.",
            "..3..",
            ".4...",
        )

        val expected = "1234"
        assertEquals(expected, grid.extractString(diagonalPoints(4, 1, 5, true)))

    }

    @Test
    fun `extract forward diagonal with fencepost negative`() {
        val grid = listOf(
            ".....",
            "..1..",
            ".2...",
            "3....",
            ".....",
        )

        val expected = "123"
        assertEquals(expected, grid.extractString(diagonalPoints(2, 1, 4, true)))
    }

    @Test
    fun `extract backward diagonal with fencepost`() {
        val grid = listOf(
            ".....",
            ".1...",
            "..2...",
            "...3.",
            "....4",
        )

        val expected = "1234"
        assertEquals(expected, grid.extractString(diagonalPoints(1, 1, 5, false)))

    }

    @Test
    fun `extract backward diagonal with fencepost positive`() {
        val grid = listOf(
            ".....",
            "..1..",
            "...2.",
            "....3",
            ".....",
        )

        val expected = "123"
        assertEquals(expected, grid.extractString(diagonalPoints(2, 1, 4, false)))

    }

}