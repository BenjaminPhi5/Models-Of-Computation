package lambda_calculus

// as it stands, this class is just for my testing

val x = Variable()
val identity = Abstraction(x,x)
val zero = Abstraction(Variable(), identity)
val x1 = Variable()
val f = Variable()
val y = Variable()
val succinner = Application(f, Application(Application(x1, f), y))
val succ = Abstraction(x1, Abstraction(f,  Abstraction(y, succinner)))

fun main(args: Array<String>) {
//    println(getChurchNumeral(generateNumeral(12)))
//    println(prettyprint(generateNumeral(12)))
//    println(getChurchNumeral(generateNumeral(0)))
//    println(getChurchNumeral(generateNumeral(5)))
//    println(getChurchNumeral(generateNumeral(1)))
//    println(isChurchNumeral(generateNumeral(12)))
//    println(isChurchNumeral(generateNumeral(0)))
//    println(isChurchNumeral(generateNumeral(5)))
//    println(isChurchNumeral(generateNumeral(1)))
    println(prettyprintPrecedence(generateNumeral(5)))
    println("-------")
    val apply = Application(generateNumeral(2), Variable())
    reduceUntilPrint(apply, 10)
    println("--------")
    println(prettyprint(succ))
    println(
            isChurchNumeral(
                    reduceUntilPrint(Application(succ, generateNumeral(5)),100)
                            )
    )

    println("----------")
    println(prettyprint(reduceUntil(Application(succ, generateNumeral(2)), 100)))
    println(getChurchNumeral(
            reduceUntil(Application(succ, generateNumeral(3)), 100) as Abstraction))
}