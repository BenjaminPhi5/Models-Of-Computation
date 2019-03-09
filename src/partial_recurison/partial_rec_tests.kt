package partial_recurison

fun main(args: Array<String>) {

    val succ = Succ()

    val zero = Zero(0)
    val zero1 = Zero(1)
    val zero2 = Zero(2)

    val proj1 = Proj(3,1)
    val proj2 = Proj(3,2)
    val proj3 = Proj(3,3)

    val const2 = Composition(succ, FuncList(0, arrayListOf(Composition(succ, FuncList(0, arrayListOf(zero))))))

    val addBase = Proj(1,1)
    val addrecfunc = Composition(succ, FuncList(3, arrayListOf(proj3)))
    val addRec = RecFunc(3, addrecfunc)
    val add = PrimitiveRecursion(addBase, addRec)

    // isZero = sub o [succ o zero1, proj(1,1)]
    // lessThanEqual = isZero o sub

    // sub = p1(zero1, sub1 o proj22)

    // sub1 = p1(zero, proj21)

    val sub1 = PrimitiveRecursion(zero, RecFunc(2, Proj(2,1)))

    val subinner = Composition(sub1, FuncList(3, arrayListOf(Proj(3,3))))

    val sub = PrimitiveRecursion(Proj(1,1), RecFunc(3, subinner))

    val isZero = Composition(sub, FuncList(1, arrayListOf(
            Composition(succ, FuncList(1, arrayListOf(zero1))), Proj(1,1))))

    val lessThanEqual = Composition(isZero, FuncList(2, arrayListOf(sub)))

    // div = u2(lessThanEqual o [mult o [succ o proj33, proj32], proj31])

    // mult = ρ1 (zero1, add ◦ (proj33 , proj31)).
    val multInner = Composition(add, FuncList(3, arrayListOf(proj3, proj1)))
    val mult = PrimitiveRecursion(zero1, RecFunc(3, multInner))

    val divinin = Composition(succ, FuncList(3, arrayListOf(proj3)))
    val divinner = Composition(mult, FuncList(3, arrayListOf(divinin, proj2)))
    val divcomp = Composition(lessThanEqual, FuncList(3, arrayListOf(divinner, proj1)))
    val div = Minimisation(2, divcomp)

    println(succ.compute(arrayListOf(10),1))
    println(succ.compute(arrayListOf(11),1))
    println(succ.compute(arrayListOf(12),1))
    println(zero.compute(arrayListOf(),0))
    println(zero1.compute(arrayListOf(1),1))
    println(zero2.compute(arrayListOf(1,1),2))
    println(proj1.compute(arrayListOf(1,2,3),3))
    println(proj2.compute(arrayListOf(1,2,3),3))
    println(proj3.compute(arrayListOf(1,2,3),3))
    println(const2.compute(arrayListOf(),0))
    println(add.compute(arrayListOf(7,10), 2))
    println(mult.compute(arrayListOf(2,4), 2))
    println(isZero.compute(arrayListOf(8), 1))
    println(isZero.compute(arrayListOf(0), 1))
    println(sub1.compute(arrayListOf(3), 1))
    println(sub.compute(arrayListOf(6,1), 2))
    println(lessThanEqual.compute(arrayListOf(9,10), 2))
    println(lessThanEqual.compute(arrayListOf(3,3), 2))
    println(lessThanEqual.compute(arrayListOf(6,1), 2))
    println(div.compute(arrayListOf(12,3), 2))
    println(div.compute(arrayListOf(25,5), 2))



}