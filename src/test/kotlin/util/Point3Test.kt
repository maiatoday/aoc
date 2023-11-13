package util

import kotlin.test.Test

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
        assert(neighbours.size == expected.size) { "too many neighbours" }
        neighbours.forEach {
            assert(it in expected) {
                "bad neighbour"
            }
        }
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
        assert(neighbours.size == expected.size) { "too many neighbours" }
        neighbours.forEach {
            assert(it in expected) {
                "bad neighbour"
            }
        }
    }

    @Test
    fun `3D point neighbours include self`() {
        val current = Point3(1, 1, 1)
        val neighbours = current.neighbours(includeSelf = true)
        assert(current in neighbours) { "missing self" }
    }

    @Test
    fun `3D point neighbours exclude self`() {
        val current = Point3(1, 1, 1)
        val neighbours = current.neighbours(includeSelf = false)
        assert(current !in neighbours) { "shouldn't include self" }
    }

    @Test
    fun `3D point neighbours with diagonals`() {
        val current = Point3(1, 1, 1)
        val neighbours = current.neighbours(includeDiagonal = true)
        val expectedSize = 9 + 9 + 8
        assert(neighbours.size == expectedSize) { "too many neighbours" }
    }

    @Test
    fun `3D point shares sides`() {
        assert(Point3(1, 1, 1).shareSides(Point3(0, 1, 1)))
        assert(Point3(1, 1, 1).shareSides(Point3(1, 0, 1)))
        assert(Point3(1, 1, 1).shareSides(Point3(1, 1, 0)))
    }

    @Test
    fun `3D point doesn't share sides`() {
        assert(!Point3(1, 1, 1).shareSides(Point3(1, 1, 1))) { "Point3 shouldn't share sides with self" }
        assert(!Point3(1, 1, 1).shareSides(Point3(0, 0, 0))) { "Point3 shouldn't share sides with diagonal" }
    }
}
