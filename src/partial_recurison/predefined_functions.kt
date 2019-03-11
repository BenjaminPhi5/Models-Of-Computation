package partial_recurison


private val succ = Succ()

private val zero = Zero(0)
private val zero1 = Zero(1)
private val zero2 = Zero(2)

private val proj1 = Proj(3,1)
private val proj2 = Proj(3,2)
private val proj3 = Proj(3,3)

private val addBase = Proj(1,1)
private val addrecfunc = Composition(succ, FuncList(3, arrayListOf(proj3)))
private val addRec = RecFunc(3, addrecfunc)
val add = PrimitiveRecursion(addBase, addRec)

fun add(x: Int, y: Int) : Int{
    return add.compute(arrayListOf(x,y), 2)
}

private val sub1 = PrimitiveRecursion(zero, RecFunc(2, Proj(2,1)))

private val subinner = Composition(sub1, FuncList(3, arrayListOf(Proj(3,3))))

val sub = PrimitiveRecursion(Proj(1,1), RecFunc(3, subinner))

private val isZero = Composition(sub, FuncList(1, arrayListOf(
        Composition(succ, FuncList(1, arrayListOf(zero1))), Proj(1,1))))

private val lessThanEqual = Composition(isZero, FuncList(2, arrayListOf(sub)))

// div = u2(lessThanEqual o [mult o [succ o proj33, proj32], proj31])

// mult = ρ1 (zero1, add ◦ (proj33 , proj31)).
private val multInner = Composition(add, FuncList(3, arrayListOf(proj3, proj1)))
private val mult = PrimitiveRecursion(zero1, RecFunc(3, multInner))

private val divinin = Composition(succ, FuncList(3, arrayListOf(proj3)))
private val divinner = Composition(mult, FuncList(3, arrayListOf(divinin, proj2)))
private val divcomp = Composition(lessThanEqual, FuncList(3, arrayListOf(divinner, proj1)))
val div = Minimisation(2, divcomp)

fun sub(x: Int, y: Int) : Int{
    return sub.compute(arrayListOf(x,y), 2)
}

fun mult(x: Int, y: Int) : Int{
    return mult.compute(arrayListOf(x,y), 2)
}

fun div(x: Int, y: Int) : Int{
    return div.compute(arrayListOf(x,y), 2)
}