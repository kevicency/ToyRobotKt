package toyrobot

class Board(val width: Int = 5, val height: Int = 5) {
    operator fun contains(point: Pair<Int, Int>): Boolean = point.first in 0 until width && point.second in 0 until height
}