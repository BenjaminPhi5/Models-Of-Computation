package partial_recurison

class Minimisation(n : Int, var func : Op) : Op(n) {

    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        // note that here size should be one less than the function itself takes
        check(size)

        var result = 1

        list.add(-1)

        while(result != 0){
            list[list.lastIndex]++

            result = func.compute(list.clone() as ArrayList<Int>, size+1)
        }

        return list[list.lastIndex]

    }
}