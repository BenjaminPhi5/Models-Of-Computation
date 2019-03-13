package lambda_calculus

import java.util.Random

private val usedNames : ArrayList<String> = ArrayList()
private var nameSize = 1
private var inAbstraction = false
private val random = Random()
private val alphabet = "abcdefghijklmnopqrstuvwxyz"
private val alphaLength = 26

// returns a nice string output of our lambda function
fun prettyprint(lambda : Lambda) : String {

    when (lambda) {
        // VARIABLE CASE
        is Variable -> {
            if(lambda.notNamed())
                lambda.setName(nextName())

            return lambda.getName()

        }

        // APPLICATION CASE
        is Application -> return prettyprint(lambda.func) + " " + prettyprint(lambda.input)

        // ABSTRACTION CASE
        is Abstraction -> {
            var result = ""

            // put opening bracket and lambda symbol
            if(!inAbstraction)
                result = "(λ "

            // get inner variable
            result += prettyprint(lambda.variable) + " "

            // check if inner lambda is an abstraction
            inAbstraction = lambda.lambda is Abstraction

            // if it isn't add dot before inner lambda add closing bracket at end of inner print and
            result += if(!inAbstraction){
                "." + prettyprint(lambda.lambda) + ")"
            } else
                prettyprint(lambda.lambda)

            // return the combined string for the internal components of the abstraction
            return result

        }
        // generic lambda case not used
        else -> return "i"
    }

}


fun prettyprintPrecedence(lambda : Lambda) : String {

    // This is similar to the pretty printer, but puts the explicit order of application
    // in the printed lambdas

    when (lambda) {
    // VARIABLE CASE
        is Variable -> {
            if(lambda.notNamed())
                lambda.setName(nextName())

            return lambda.getName()

        }

    // APPLICATION CASE
        is Application ->
            return "(" + prettyprintPrecedence(lambda.func) + "-" + prettyprintPrecedence(lambda.input) + ")"

    // ABSTRACTION CASE
        is Abstraction -> {
            var result = ""

            // put opening bracket and lambda symbol
            if(!inAbstraction)
                result = "(λ "

            // get inner variable
            result += prettyprintPrecedence(lambda.variable) + " "

            // check if inner lambda is an abstraction
            inAbstraction = lambda.lambda is Abstraction

            // if it isn't add dot before inner lambda add closing bracket at end of inner print and
            result += if(!inAbstraction){
                "." + prettyprintPrecedence(lambda.lambda) + ")"
            } else
                prettyprintPrecedence(lambda.lambda)

            // return the combined string for the internal components of the abstraction
            return result

        }
    // generic lambda case not used
        else -> return ""
    }

}

private fun genName(length : Int) : String {
// generate a new name, of the given length, of characters ranging from a-z

    var result = ""
    for (i in 1..length) {
        result += alphabet[random.nextInt(alphaLength)]
    }

    return result

}

private fun nextName() : String {

    if(usedNames.size / 26 == nameSize + 1)
        nameSize += 1

    var name = genName(nameSize)
    while(name in usedNames)
        name = genName(nameSize)

    usedNames.add(name)

    return name

}