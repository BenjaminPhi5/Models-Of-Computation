package RMs

// register names in the Universal Register Machine
enum class Regs{ANS, P, A, PC, N, C, R, S, T, Z, L, X}

// The following gives the required sub programs for the universal reg machine

// Assign S to R, computes S ::= R, that is copy over the contents of R to S
val l0 = BodyOp(Operation.SUB, Regs.S.ordinal, 0, 1)
val l1 = BodyOp(Operation.SUB, Regs.R.ordinal, 2, 4)
val l2 = BodyOp(Operation.ADD, Regs.Z.ordinal, 3, null)
val l3 = BodyOp(Operation.ADD, Regs.S.ordinal, 1, null)
val l4 = BodyOp(Operation.SUB, Regs.Z.ordinal, 5, 6)
val l5 = BodyOp(Operation.ADD, Regs.R.ordinal, 4, null)
val l6 = Body(Operation.HALT)
val assignSR = arrayOf(l0, l1, l2, l3, l4, l5, l6)

// Push S to A, this performs (X, L) ::= (0, X::L), so it cons x onto head of L
// and empties X
val l7 = BodyOp(Operation.ADD, Regs.Z.ordinal, 1, null)
val l8 = BodyOp(Operation.SUB, Regs.L.ordinal, 2, 3)
val l9 = BodyOp(Operation.ADD, Regs.Z.ordinal, 0, null)
val l10 = BodyOp(Operation.SUB, Regs.Z.ordinal, 4, 5)
val l11 = BodyOp(Operation.ADD, Regs.L.ordinal, 3, null)
val l12 = BodyOp(Operation.SUB, Regs.X.ordinal, 1, 6)
val l13 = Body(Operation.HALT)
val xConsL = arrayOf(l7, l8, l9, l10, l11, l12, l13)

/* Pop L to X, which gets the head off of L and puts it in X
* Involves decoding the encoded L list and performs
* if L = 0 then (X ::= 0; goto EXIT) else
* let L = <<x,l>> in (X::= x; L::=l; goto HALT)
* */
val l14 = BodyOp(Operation.SUB, Regs.X.ordinal, 0, 1)
val l15 = BodyOp(Operation.SUB, Regs.L.ordinal, 3, 2)
val l16 = Body(Operation.EXIT)
val l17 = BodyOp(Operation.ADD, Regs.L.ordinal, 4, null)
val l18 = BodyOp(Operation.SUB, Regs.L.ordinal, 5, 6)
val l19 = BodyOp(Operation.ADD, Regs.Z.ordinal, 4, null)
val l20 = BodyOp(Operation.SUB, Regs.Z.ordinal, 8, 7)
val l21 = BodyOp(Operation.ADD, Regs.X.ordinal, 4, null)
val l22 = BodyOp(Operation.SUB, Regs.Z.ordinal, 9, 10)
val l23 = BodyOp(Operation.ADD, Regs.L.ordinal, 6, null)
val l24 = Body(Operation.HALT)
val popLtoX = arrayOf(l14, l15, l16, l17, l18, l19, l20, l21, l22, l23, l24)

// Decrement PC, will use as an alias for both dec C's and the Dec R
val l25 = BodyOp(Operation.SUB, Regs.PC.ordinal, 1, 2)
val l26 = Body(Operation.HALT)
val l27 = Body(Operation.EXIT)
val DecPC = arrayOf(l25, l26, l27)

// Inc R, will also use as a alias module for inc N
val l31 = BodyOp(Operation.ADD, Regs.R.ordinal, 1, null)
val l32 = Body(Operation.HALT)
val IncR = arrayOf(l31, l32)

val emptyAlias = HashMap<Int, Int>()

// Now I will create all the modules required, using aliases to assign different registers.
val push0toA = Module(xConsL, hashMapOf(Regs.X.ordinal to Regs.ANS.ordinal, Regs.L.ordinal to Regs.A.ordinal))
val assignTP = Module(assignSR, hashMapOf(Regs.S.ordinal to Regs.T.ordinal, Regs.R.ordinal to Regs.P.ordinal))
val popTtoN = Module(popLtoX, hashMapOf(Regs.X.ordinal to Regs.N.ordinal, Regs.L.ordinal to Regs.T.ordinal))
val popAtoR0 = Module(popLtoX, hashMapOf(Regs.X.ordinal to Regs.ANS.ordinal, Regs.L.ordinal to Regs.A.ordinal))
val popNtoC = Module(popLtoX, hashMapOf(Regs.X.ordinal to Regs.C.ordinal, Regs.L.ordinal to Regs.N.ordinal))
val popStoR = Module(popLtoX, hashMapOf(Regs.X.ordinal to Regs.R.ordinal, Regs.L.ordinal to Regs.S.ordinal))
val pushRtoA = Module(xConsL, hashMapOf(Regs.X.ordinal to Regs.R.ordinal, Regs.L.ordinal to Regs.A.ordinal))
val assignPCN = Module(assignSR, hashMapOf(Regs.S.ordinal to Regs.PC.ordinal, Regs.R.ordinal to Regs.N.ordinal))
val decCeven = Module(DecPC, hashMapOf(Regs.PC.ordinal to Regs.C.ordinal))
val decCodd = Module(DecPC, hashMapOf(Regs.PC.ordinal to Regs.C.ordinal))
val popAtoR  = Module(popLtoX, hashMapOf(Regs.X.ordinal to Regs.R.ordinal, Regs.L.ordinal to Regs.A.ordinal))
val decR = Module(DecPC, hashMapOf(Regs.PC.ordinal to Regs.R.ordinal))
val decPC = Module(DecPC, emptyAlias)
val popNtoPC = Module(popLtoX, hashMapOf(Regs.X.ordinal to Regs.PC.ordinal, Regs.L.ordinal to Regs.N.ordinal))
val incN = Module(IncR, hashMapOf(Regs.R.ordinal to Regs.N.ordinal))
val incR = Module(IncR, emptyAlias)
val pushRtoS = Module(xConsL, hashMapOf(Regs.X.ordinal to Regs.R.ordinal, Regs.L.ordinal to Regs.S.ordinal))

// Now I will combine all the modules together, in a combiner, mapping their halts and exits to the relevant modules
// to construct the universal register machine

val U = combiner(arrayOf(push0toA, assignTP, popTtoN, popAtoR0, decPC, popNtoC, popAtoR, decCeven, decCodd, pushRtoS,
        incR, assignPCN, pushRtoA, incN, popNtoPC, decR, popStoR), // all modules input
        // map of halts to next module after halt
        hashMapOf(push0toA to assignTP, assignTP to popTtoN, popTtoN to decPC, decPC to popTtoN,
                popNtoC to popAtoR, popAtoR to decCeven, decCeven to decCodd, incR to assignPCN,
                assignPCN to pushRtoA, pushRtoA to popStoR, popStoR to pushRtoA, pushRtoS to popAtoR,
                decCodd to pushRtoS, incN to popNtoPC, popNtoPC to decR, decR to pushRtoA),
        // map of exits to next module after exit
        hashMapOf(popTtoN to popAtoR0, decPC to popNtoC, popNtoC to popAtoR0, popAtoR to decCeven,
                decCeven to incR, popStoR to assignTP, decCodd to incN, popNtoPC to decR, decR to assignPCN))

val prog = U.combine() // this also calculates all the aliases for the modules and retains them

// This prints out the register machine code for the Universal Register Machine!!!!
fun printU() {
        printProgram(prog)
}

fun runUniversalMachine(progToSimulate : Array<Body>, initialRegs : Array<Long>){

    val machine = RegisterMachine(prog, arrayOf(0, encodeProgram(progToSimulate), encodeList(initialRegs, 0)),
            true)

    machine.run()

    println("regs: ${machine.regs}")

}

fun sampleProgram(){
    // does addition of 4 and 5 in the universal register machine
    runUniversalMachine(getConst3(), arrayOf(1,1,1))
}