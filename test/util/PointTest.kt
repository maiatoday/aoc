package util

import kotlin.test.Test
import kotlin.test.assertEquals

class PointTest {

    @Test
    fun `Check rose indices`() {
        val p = Point(0, 0)
        val neighbours = p.roseNeighbours()
        println(neighbours)
        assertEquals(Point(0, -1), neighbours[RoseDirections.N.index])
        assertEquals(Point(1, -1), neighbours[RoseDirections.NE.index])
        assertEquals(Point(1, 0), neighbours[RoseDirections.E.index])
        assertEquals(Point(1, 1), neighbours[RoseDirections.SE.index])
        assertEquals(Point(0, 1), neighbours[RoseDirections.S.index])
        assertEquals(Point(-1, 1), neighbours[RoseDirections.SW.index])
        assertEquals(Point(-1, 0), neighbours[RoseDirections.W.index])
        assertEquals(Point(-1, -1), neighbours[RoseDirections.NW.index])
    }

    @Test
    fun `Check cardinal indices`() {
        val p = Point(0, 0)
        val neighbours = p.neighbours(diagonal = false, onlyPositive = false, stayBelowMax = false)
        println(neighbours)
        assertEquals(Point(0, -1), neighbours[CardinalDirections.N.index])
        assertEquals(Point(1, 0), neighbours[CardinalDirections.E.index])
        assertEquals(Point(0, 1), neighbours[CardinalDirections.S.index])
        assertEquals(Point(-1, 0), neighbours[CardinalDirections.W.index])
    }
}