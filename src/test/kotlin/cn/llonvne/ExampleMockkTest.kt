package cn.llonvne

class ExampleMockkTest {
    enum class Direction {
        NORTH, SOUTH
    }

    enum class Outcome {
        OK, NOT_OK
    }

    interface Car {
        fun drive(dir: Direction): Outcome
    }


}