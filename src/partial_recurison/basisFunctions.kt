package partial_recurison

open class Op(val n : Int){

    fun check(length : Int) {
        if (length != n)
            throw InputValueException(n, length)
    }

    open fun compute(list: ArrayList<Int>, size : Int): Int {
        check(size)

        return 0
    }
}

class InputValueException(val expected : Int, val given : Int) : Exception() {

    override val message: String?
        get() = "Expected $expected inputs, got $given instead"
}


class Succ : Op(1) {

    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        check(size)

        return list[0] + 1
    }
}

class Zero(n : Int) : Op(n) {

    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        check(size)

        return 0
    }
}

class Proj(n : Int, val i : Int) : Op(n) {

    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        check(size)

        return list[i-1]
    }
}