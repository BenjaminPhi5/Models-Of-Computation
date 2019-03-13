package partial_recurison


/**
 * This class contains the template class Op and the basis class functions
 * Each class inheriting from Op implements one specific construct in the primitive recursive universe.
 */
open class Op(val n : Int){

    /**
     * @param length : this is the number of inputs provided to a function,
     * n is the number of inputs the function expects, stored in the object's n field
     */
    fun check(length : Int) {
        if (length != n)
            throw InputValueException(n, length)
    }

    /**
     * This computes the output of the function (empty in the Op function, as this is overwritten by *all* inheritors
     * of the class
     * @param list: the elements that the function operates on
     * @param size: the number of elements provided
     *
     * Each overridden instance of this method should check the size is valid (if not an error will be printed
     * stating how many arguments were received verses how many expected.
     */
    open fun compute(list: ArrayList<Int>, size : Int): Int {
        check(size)

        return 0
    }
}

class InputValueException(val expected : Int, val given : Int) : Exception() {

    override val message: String?
        get() = "Expected $expected inputs, got $given instead"
}

/**
 * @param, n is always 1 for successor.
 */
class Succ : Op(1) {

    // succ just computers the successor of an integer, thats just plus 1
    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        check(size)

        return list[0] + 1
    }
}


class Zero(n : Int) : Op(n) {

    // Zero takes n inputs and always returns from a good idea.
    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        check(size)

        return 0
    }
}

class Proj(n : Int, val i : Int) : Op(n) {

    // projection takes a list of length n, and returns the ith element
    // annoyingly in the partial recursive universe, this is 1 based, not 0 based.
    // so this is 1 based too.
    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        check(size)

        return list[i-1]
    }
}