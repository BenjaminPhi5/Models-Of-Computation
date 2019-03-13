package lambda_calculus

private var reduced = false

open class Lambda {

    // reduce will be overridden by all classes extending lambda
    // and will either reduce the expression if it can be reduced and returns the new result
    // (reducing using normal order reduction)
    open fun reduce() : Lambda{
        return this
    }

    // computes substitution
    fun substitution(bound : Variable, output : Lambda, argument : Lambda) : Lambda {
        reduced = true

        // if substitution has occurred, the we know some reduction has occured, so set the reduction flag to true.
       // replace a variable if the bound variable matches the output
        // in an application or an abstraction, recursively call on their components.
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

// the basic variable in the lambda calculus.
class Variable : Lambda() {

    // names can be assigned, used in pretty printing.
    // only allows a name to be assigned if it has already been assigned.
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

// an abstraction is a lambda of the form lambda x. M,
/**
 * @constructor takes the variable x and an output lambda lambda
 */
class Abstraction(val variable: Variable, var lambda: Lambda) : Lambda() {

    // to reduce, reduce the lambda within
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

    // similar to the reduction function above, but prints the result of each reduction.

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

    // reduce the equation continually until it cannot be reduced anymore
    // or until the reductions cap has run out
    // (since some reductions never reduce there is a cap, think the omega lambda = (λx.x x)(λx.x x) ).

    for(i in 0 until reductions) {
        newLambda = oldLambda.reduce()

        if(!reduced)
            break

        oldLambda = newLambda
        reduced = false
    }

    return newLambda
}