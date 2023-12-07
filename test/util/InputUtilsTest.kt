package util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InputUtilsTest {

    @Test
    fun `Test  multi section input`() {
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
        val expectedFirst = listOf("aaa", "aaa", "aaa")
        val expectedSecond = listOf("bbb", "bbb", "bbb")
        val expectedThird = listOf("ccc")
        
        // then
        assertEquals(3, actual.size)
        assertEquals(expectedFirst, actual[0])
        assertEquals(expectedSecond, actual[1])
        assertEquals(expectedThird, actual[2])
    }

}

