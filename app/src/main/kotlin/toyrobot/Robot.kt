package toyrobot

data class Robot(var position: Pair<Int, Int>, var facing: Direction) {
    constructor(x: Int, y: Int, facing: Direction) : this(Pair(x, y), facing)

    fun stringify() = "${position.first},${position.second},${facing}"
}