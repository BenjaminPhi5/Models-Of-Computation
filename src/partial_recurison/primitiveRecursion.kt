package partial_recurison

/**
 * this class stores the recursive function
 */
class RecFunc : Op {

    var func = Op(0)

    constructor(n : Int, func : Op) : super(n) {
        this.func = func
    }

    // here n is the number of elements the rec function takes, so normal n + 1
    //g(x, y, h(x,y))

    /**
     * This method computes the recursion, by taking the elements provided and calling the function using the
     * arguments in
     * @param list,
     * @param h: This is the recusive function. A primitive recursive style function may actually not need it
     * (may be using primitive recursion for flow control instead) and so it tries to execute the function func
     * without explicitly calculating h.
     */
    fun computeRec(list: ArrayList<Int>, size : Int, h : Op): Int {
        check(size+1)

        try {
            return func.compute(list, size+1)
        } catch (e : IndexOutOfBoundsException) {
            val hValue = h.compute(list.clone() as ArrayList<Int>, size)
            list.add(hValue)
            return func.compute(list, size+1)
        }

    }

}

// if they ask for the extra element, compute it and give it to them yeah


class PrimitiveRecursion : Op {

    // base case and recursive functions
    // base takes one less argument that our normal function
    // rec takes one more argument
    // however, the last argument is a function with arguments passed in not an int and you may not need to
    //calculate the last argument (see comments in above class)
    var base = Op(0)
    var rec =  RecFunc(0, Op(0))

    constructor(base : Op, rec : RecFunc) : super(base.n+1) {

        if(base.n != rec.n - 2)
            throw InputValueException(base.n+2, rec.n)

        this.base = base
        this.rec = rec
    }

    // decides if to use the base case or the recursive case and calls their compute/computeRec functions
    // respectively.
    override fun compute(list: ArrayList<Int>, size : Int): Int {
        check(size)

        if(list[list.lastIndex] == 0){
            list.removeAt(list.lastIndex)
            return base.compute(list, size-1)

        } else {
            list[list.lastIndex] -= 1
            return rec.computeRec(list, size, this)
        }

    }

}