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

    if(lambda !is Abstraction)
        return false

    // recurse into lambda, assuming always get this
    // structure lambda x. (app
    val f = lambda.variable
    var inner: Abstraction = lambda.lambda as? Abstraction ?: return false
    val x = inner.variable
    // check inner.lambda is an application, if so second element is x, or its just 0
    if(inner.lambda is Application) {
        if ((inner.lambda as Application).input != x)
            return false
    }
    else if(inner.lambda is Variable && inner.lambda != x)
        return false

    var current = inner.lambda

    // if its numeral 0
    if(current is Variable)
        return true
    // if its numeral 1
    else if(current is Application){
        if(current.func == f && current.input == x)
            return true
        current=current.func
    }

    // for numerals greater than 1
    while(current is Application){
        if(current.input != f)
            return false

        else
            current = current.input

    }

    return (current == f)

}

// assumes you have already checked that the lambda is actually a church numeral
fun getChurchNumeral(lambda: Abstraction) : Int {

    if(lambda.lambda !is Abstraction) {
        print("error, not a church numeral")
        return 0
    }

    val inner = (lambda.lambda as Abstraction).lambda
    var current = inner

    var count = 0
    while(current is Application){
        count++
        current = current.func
    }

    return count

}