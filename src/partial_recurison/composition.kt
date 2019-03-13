package partial_recurison

import kotlin.properties.Delegates

class FuncList : Op {

    // This stores a list of functions that are applied in the composition
    // f o [g1, ..., gm]

    var gList = ArrayList<Op>()
    var fn = 0

    // takes the number of arguments each g function expects, and the list of functions
    constructor(gn : Int, gList: ArrayList<Op>) : super(gn) {
        //check that the function arities match.
        for(func in gList) {
            if(func.n != gn)
                throw InputValueException(gn, func.n)
        }
        fn = gList.size
        this.gList = gList
    }


    // computes the output of every function on the given arguments and returns a list of the results.
    fun computeAll(list: ArrayList<Int>, size : Int) : ArrayList<Int> {
        check(size)

        val results = ArrayList<Int>()
        for(g in gList) {
            results.add(g.compute(list, size))
        }

        return results
    }

}

class Composition : Op {

    // takes a function f and a FuncList object, containing the functions in the g list
    // f o [g1,...,gn]
    var g = FuncList(0, ArrayList())
    var f = Op(0)

    constructor(f : Op, g : FuncList) : super(g.n) {
        if(f.n != g.fn){
            throw InputValueException(f.n, g.fn)
        }
        this.f = f
        this.g = g

    }

    // calls compute all on g and then calls the result on f
    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        check(size)

        val results = g.computeAll(list, size)

        return f.compute(results, results.size)

    }
}