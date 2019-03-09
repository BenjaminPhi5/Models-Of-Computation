package partial_recurison

import kotlin.properties.Delegates

class FuncList : Op {

    var gList = ArrayList<Op>()
    var fn = 0

    constructor(gn : Int, gList: ArrayList<Op>) : super(gn) {
        for(func in gList) {
            if(func.n != gn)
                throw InputValueException(gn, func.n)
        }
        fn = gList.size
        this.gList = gList
    }

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

    var g = FuncList(0, ArrayList())
    var f = Op(0)

    constructor(f : Op, g : FuncList) : super(g.n) {
        if(f.n != g.fn){
            throw InputValueException(f.n, g.fn)
        }
        this.f = f
        this.g = g

    }


    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        check(size)

        val results = g.computeAll(list, size)

        return f.compute(results, results.size)

    }
}