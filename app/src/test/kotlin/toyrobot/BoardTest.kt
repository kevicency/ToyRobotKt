/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package toyrobot

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BoardTest {
    @Test
    fun `contains for valid points`() {
        val sut = Board(5, 5)

        for (point in listOf(
                Pair(0, 0),
                Pair(4, 0),
                Pair(0, 4),
                Pair(4, 4)
        )) {
            assertTrue { sut.contains(point) }
        }
    }

    @Test
    fun `contains for invalid points`() {
        val sut = Board(5, 5)

        for (point in listOf(
                Pair(-1, 1),
                Pair(5, 0),
                Pair(0, 5),
                Pair(5, 5)
        )) {
            assertFalse { sut.contains(point) }
        }
    }
}