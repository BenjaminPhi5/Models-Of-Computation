package lambda_calculus

fun generateNumeral(n : Int) : Abstraction {

    var x = Variable()
    var f = Variable()

    var current : Lambda = f

    // deal with zeroes first
    if(n == 0)
        return Abstraction(f, Abstraction(x, x))

    if(n > 1) {
        for (i in 2..n) {
            val new = Application(current, f)
            current = new
        }
    }
    current = Application(current, x)
    var xAbstraction = Abstraction(x, current)
    var fAbstraction = Abstraction(f, xAbstraction)

    return fAbstraction

}

fun isChurchNumeral(lambda : Lambda) : Boolean {

    //check it is actually an abstraction, this is the λf abstraction
    if(lambda !is Abstraction)
        return false

    // check its lambda is an abstraction, the λx abstraction
    if(lambda.lambda !is Abstraction)
        return false

    var inner : Abstraction = lambda.lambda as Abstraction

    // our f and x
    val f = lambda.variable
    val x = inner.variable

    val list = ArrayList<Lambda>()

    // get the list of child elements, recursing if we find an application
    recurseNum(inner.lambda, list)

    // check all elements other than last are our function f
    for(i in 0 until list.size - 1)
        if (list[i] != f)
            return false

    // check that the last element is x
    return (list[list.lastIndex] == x)

}

private fun recurseNum(lambda: Lambda, list : ArrayList<Lambda>) {

    // builds up a list of elements left to right
    // only care about application children, if given other children it will show up during check
    // after the fact

    if(lambda is Application){
        recurseNum(lambda.func, list)
        recurseNum(lambda.input, list)
    } else {
        list.add(lambda)
    }

}


// assumes you have already checked that the lambda is actually a church numeral
fun getChurchNumeral(lambda: Abstraction) : Int {

    // MAKE SURE YOU USE THE BOOLEAN CHECK METHOD FIRST BEFORE USING THIS

    // get the inner variable
    val inner : Lambda = ((lambda.lambda) as Abstraction).lambda

    // init the list
    val list = ArrayList<Lambda>()

    // generate the list
    recurseNum(inner, list)

    // assumes list of form f f f .... f x (so size-1 f's gives the numeral value)
    return list.size - 1

}