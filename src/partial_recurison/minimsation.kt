package partial_recurison

class Minimisation(n : Int, var func : Op) : Op(n) {

    // computes minimisation for the function provided
    // where the function f takes size+1 inputs
    override fun compute(list: ArrayList<Int>, size : Int) : Int {
        // note that here size should be one less than the function itself takes
        check(size)

        var result = 1

        // gets incremented to zero before call in the while loop
        list.add(-1)

        // calls the function, incrementing the last argument, until the result is zero
        // note this introduces the "partial" to our partial recursive universe
        // since this while loop may never terminate
        while(result != 0){
            list[list.lastIndex]++

            result = func.compute(list.clone() as ArrayList<Int>, size+1)
        }

        return list[list.lastIndex]

    }
}