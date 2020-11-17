package toyrobot

operator fun Pair<Int, Int>.plus(direction: Direction) = when (direction) {
    Direction.NORTH -> first to second + 1
    Direction.EAST -> first + 1 to second
    Direction.SOUTH -> first to second - 1
    Direction.WEST -> first - 1 to second
}

