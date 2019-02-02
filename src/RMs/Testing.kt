package RMs

val empty = HashMap<Int, Int>()

fun testAdd() {

    // this should add up the values 4 and 5 and print the result 9 in r0, with all other regs 0.
    val regInit : Array<Long> = arrayOf(0, 4, 5)

    // doinit true so that the values to be added are copied in.
    val machine = RegisterMachine(getAdd(), regInit, true)

    machine.run()

    println(machine.regs)

}

fun getAdd() : Array<Body> {
    // performs the add operation
    val l0 = BodyOp(Operation.SUB, 1, 1, 2)
    val l1 = BodyOp(Operation.ADD, 0, 0, null)
    val l2 = BodyOp(Operation.SUB, 2, 3, 4)
    val l3 = BodyOp(Operation.ADD, 0, 2, null)
    val l4 = Body(Operation.HALT)
    val prog : Array<Body> = arrayOf(l0, l1, l2, l3, l4)

    return prog
}

fun getConstTwo(): Array<Body> {
    val l0 = BodyOp(Operation.ADD, 0, 1, null)
    val l1 = BodyOp(Operation.ADD, 1, 2, null)
    val l2 = BodyOp(Operation.ADD, 2, 3, null)
    val l3 = Body(Operation.HALT)

    val prog = arrayOf(l0, l1, l2, l3)
    return prog
}

fun getMoveR0toR1R2(): Array<Body> {
    val l0 = BodyOp(Operation.SUB, 0, 1, 3)
    val l1 = BodyOp(Operation.ADD, 1, 2, null)
    val l2 = BodyOp(Operation.ADD, 2, 0, null)
    val l3 = Body(Operation.HALT)

    return arrayOf(l0, l1, l2, l3)
}

fun testXmult4(){
    val a1 = Module(getAdd(), empty)
    val a2 = Module(getAdd(), empty)
    val m1 = Module(getMoveR0toR1R2(), empty)
    val m2 = Module(getMoveR0toR1R2(), empty)

    val c = combiner(arrayOf(m1, a1, m2, a2), hashMapOf(m1 to a1, a1 to m2, m2 to a2), HashMap())

    val prog = c.combine()
    printProgram(prog)

    val machine = RegisterMachine(prog, arrayOf(3), true)

    machine.run()

    println(machine.regs)

}

fun testEnumValues(){
    println(Regs.A.ordinal)
    println(Regs.ANS.ordinal)
    println(Regs.Z.ordinal)
}

fun testSingleAlias() {
    val add = getAdd()
    val module = Module(add, hashMapOf(2 to 3))
    module.mapAliases()

    printProgram(module.prog)

    val machine = RegisterMachine(module.prog, arrayOf(0,3,0,6), true)

    machine.run()

    println(machine.regs)

}

fun testCombineSinge(){
    // tests that the combiner and alias computation do not cause erroneous calculation
    // from a single module with no aliases.

    var adder = Module(getAdd(), HashMap())
    var c = combiner(arrayOf(adder), HashMap(), HashMap())

    val machine = RegisterMachine(c.combine(), arrayOf(0, 4, 5), true)

    machine.run()

    println(machine.regs)
}

fun testCombineMultipleAdd(){

    var a1 = Module(getConstTwo(), empty)
    var a2 = Module(getConstTwo(), empty)

    val c = combiner(arrayOf(a1, a2), hashMapOf(a1 to a2), HashMap())
    println("a1 = $a1, a2 = $a2")

    val prog = c.combine()
    printProgram(prog)

    val machine = RegisterMachine(prog, arrayOf(), false)

    machine.run()

    println(machine.regs)
}