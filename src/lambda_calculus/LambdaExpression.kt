package lambda_calculus

private var reduced = false

open class Lambda {

    open fun reduce() : Lambda{
        return this
    }

    fun substitution(bound : Variable, output : Lambda, argument : Lambda) : Lambda {
        reduced = true

        when (output) {
            is Variable -> return if(bound == output)
                argument
                else output
            is Application -> {
                output.func = substitution(bound, output.func, argument)
                output.input = substitution(bound, output.input, argument)
                return output

            }
            is Abstraction -> {
                output.lambda = substitution(bound, output.lambda, argument)
                return output
            }
            else -> {
                // generic lambda never used
                return this
            }
        }
    }
}

class Variable : Lambda() {

    private var name : String? = null

    fun notNamed() : Boolean {
        return (name == null)
    }

    fun setName(newName : String) {
        if(name == null)
            name = newName
    }

    fun getName() : String {
        if(name == null)
            throw NullPointerException("name has not been initialised")

        return name!!
    }


}

class Abstraction(val variable: Variable, var lambda: Lambda) : Lambda() {

    override fun reduce() : Lambda{
        lambda = lambda.reduce()
        return this
    }

}

class Application(var func : Lambda, var input : Lambda) : Lambda() {

    override fun reduce() : Lambda {

        when (func) {
            // if variable, no reductions left to LHS but substitution cannot be performed
            is Variable -> {
                input = input.reduce()
                return this
            }

            // if func is an Application we need to reduce that first before doing this application
            is Application -> {
                func = func.reduce()
                return this
            }

            // if func is an abstraction we can finally do our substitution
            is Abstraction -> {
                val funcAbs = (func as Abstraction)
                return substitution(funcAbs.variable, funcAbs.lambda, input)
            }
            else -> {
                // for a generic lambda only
                return this
            }
        }
    }
}

fun reduceUntilPrint(lambda: Lambda, reductions : Int) : Lambda {

    println(prettyprint(lambda))

    var oldLambda = lambda
    var newLambda: Lambda = lambda

    for(i in 0 until reductions) {
        newLambda = oldLambda.reduce()

        if(!reduced)
            break

        println(prettyprint(newLambda))
        oldLambda = newLambda
        reduced = false
    }

    println(prettyprint(newLambda))
    return newLambda
}

fun reduceUntil(lambda: Lambda, reductions: Int) : Lambda {

    var oldLambda = lambda
    var newLambda: Lambda = lambda

    for(i in 0 until reductions) {
        newLambda = oldLambda.reduce()

        if(!reduced)
            break

        oldLambda = newLambda
        reduced = false
    }

    return newLambda
}