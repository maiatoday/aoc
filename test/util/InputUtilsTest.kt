package util

import kotlin.test.Test
import kotlin.test.assertEquals

class InputUtilsTest {

    @Test
    fun `test  multi section input`() {
        // given
        val input = listOf(
                "aaa",
                "aaa",
                "aaa",
                "",
                "bbb",
                "bbb",
                "bbb",
                "",
                "ccc",
                "",
                "",
                "",
        )

        // when
        val actual = input.splitByBlankLine()

        // expected
        val expected = listOf(
                listOf("aaa", "aaa", "aaa"),
                listOf("bbb", "bbb", "bbb"),
                listOf("ccc")
        )

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `test long sequence`() {
        // given
        val input = "1 2 -3 abcd 123456 0  "
        val expected = listOf(1L, 2L, -3L, 123456L, 0L)

        //then
        assertEquals(expected, input.readLongs())
    }

    @Test
    fun `test int sequence`() {
        // given
        val input = "1 2 -3 abcd 123456 0   "
        val expected = listOf(1, 2, -3, 123456, 0)

        //then
        assertEquals(expected, input.readInts())

    }
}

